package com.ourMenu.backend.domain.menu.application;

import com.ourMenu.backend.domain.menu.domain.*;
import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.dto.request.*;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menu.exception.MenuNotFoundException;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.user.application.UserService;
import com.ourMenu.backend.domain.user.domain.User;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuListService menuListService;
    private final PlaceService placeService;
    private final TagService tagService;
    private final UserService userService;
    private final EntityManager em;
    private final S3Client s3Client;

    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;

    // 모두 조회
    @Transactional
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }


    @Transactional
    // 메뉴 등록 * (이미지 제외)
    public PostMenuResponse createMenu(PostMenuRequest postMenuRequest, Long userId) {

        // 유저 정보 가져오기
        User finduser = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 유저가 없습니다."));

        // 메뉴판 정보 가져오기
        MenuList findMenuList = menuListService.getMenuListByName(postMenuRequest.getMenuListTitle(), userId);

        // 장소 가져오기(식당이 없는 경우 새로 생성)
        Place place = placeService.createPlace(postMenuRequest.getStoreInfo(), userId);

        boolean exists = menuRepository.existsByPlaceIdAndMenuListIdAndTitle(place.getId(), findMenuList.getId(), postMenuRequest.getTitle());
        if (exists) {
            throw new RuntimeException("해당 식당의 메뉴판에 동일한 메뉴명이 이미 존재합니다.");
        }

        // 메뉴 생성
        Menu menu = Menu.builder()
                .title(postMenuRequest.getTitle())
                .price(postMenuRequest.getPrice())
                .user(finduser)
                .memo(postMenuRequest.getMemo())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        // 식당, 메뉴판 연관관계 설정
        menu.confirmMenuList(findMenuList);
        menu.confirmPlace(place);

        // MenuTag 생성 및 연관관계 설정
        List<MenuTag> menuTags = postMenuRequest.getTagInfo().stream()
                .map(tagInfo -> {
                    Tag tag = tagService.findByName(tagInfo.getTagTitle())
                            .orElseGet(() -> tagService.createTag(tagInfo)); // 태그가 존재하지 않으면 생성

                    // 중간 테이블 생성
                    MenuTag menuTag = MenuTag.builder()
                            .tag(tag)
                            .menu(menu)
                            .build();
                    // 연관관계 설정
                    menuTag.confirmTag(tag);
                    menuTag.confirmMenu(menu);

                    return menuTag;
                })
                .collect(Collectors.toList());


        Menu savedMenu = menuRepository.save(menu);
        placeService.save(place);

        return new PostMenuResponse(savedMenu.getId());
    }

    // 메뉴 추가
    public Menu getMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));
    }

    // 메뉴 이미지 등록
    @Transactional
    public void createMenuImage(PostPhotoRequest request) {
        List<MultipartFile> imgs = request.getImgs();
        long menuId = request.getMenuId();

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile img : imgs) {
            String fileUrl = "";
            try {
                if (img != null && !img.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + URLEncoder.encode(img.getOriginalFilename(), StandardCharsets.UTF_8.toString());

                    s3Client.putObject(PutObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(fileName)
                                    .build(),
                            RequestBody.fromBytes(img.getBytes()));

                    fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
                    fileUrls.add(fileUrl); // 변환된 URL 추가해줌
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

        List<MenuImage> menuImages = fileUrls.stream()
                .map(url -> MenuImage.builder()
                        .url(url)
                        .menu(menu)
                        .build())
                .collect(Collectors.toList());

        for (MenuImage menuImage : menuImages) {
            menuImage.confirmMenu(menu);
        }
    }

    @Transactional
    public void updateMenu(Long menuId, Long userId, PatchMenuRequest patchMenuRequest) {
        Menu menu = menuRepository.findAllWithUserAndMenuListAndPlace(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 매뉴가 없습니다."));

        String MenuListTitle = patchMenuRequest.getMenuListTitle();

        // 메뉴 필드 값 업데이트
        if (patchMenuRequest.getTitle() != null) {
            menu.changeTitle(patchMenuRequest.getTitle());
        }

        if (patchMenuRequest.getPrice() > 0) { // 가격이 0보다 큰 경우만 업데이트
            menu.changePrice(patchMenuRequest.getPrice());
        }

        if (patchMenuRequest.getMemo() != null) {
            menu.changeMemo(patchMenuRequest.getMemo());
        }
        if (patchMenuRequest.getIcon() != null) {
            menu.changeIcon(patchMenuRequest.getIcon());
        }

        // 메뉴판 변경
        if(!patchMenuRequest.getMenuListTitle().equals(menu.getMenuList().getTitle())){
            MenuList menulist = menuListService.getMenuListByName(patchMenuRequest.getTitle(), userId);
            menu.removeMenuList(menu.getMenuList());
            menu.confirmMenuList(menulist);
        }

        // 식당 운영정보 변경
        String storeInfo = patchMenuRequest.getStoreInfo().getStoreInfo();
        if(storeInfo != null){
            menu.getPlace().changeInfo(storeInfo);
        }

        // 태그 정보 변경
        List<TagRequestDto> tagInfo = patchMenuRequest.getTagInfo();
        if(tagInfo != null){
            menu.getTags().clear();
            List<MenuTag> menuTags = tagInfo.stream()
                    .map(mt -> {
                        Tag tag = tagService.findByName(mt.getTagTitle())
                                .orElseGet(() -> tagService.createTag(mt)); // 태그가 존재하지 않으면 생성

                        // 중간 테이블 생성
                        MenuTag menuTag = MenuTag.builder()
                                .tag(tag)
                                .menu(menu)
                                .build();
                        // 연관관계 설정
                        menuTag.confirmTag(tag);
                        menuTag.confirmMenu(menu);

                        return menuTag;
                    })
                    .collect(Collectors.toList());
        }

    }

    @Transactional
    public String updateMenuImage(PatchMenuImage patchMenuImage, long id, long userId) {
        log.info("현재 id 값은 : " + id);
        Menu menu = menuRepository.findMenuAndImages(id)
                .orElseThrow(() -> new RuntimeException("해당하는 매뉴가 없습니다."));

        List<MultipartFile> imgs = patchMenuImage.getImgs();

        List<String> fileUrls = new ArrayList<>();
        if(imgs != null) {
            removeImages(menu);
            log.info("메뉴 이미지 삭제 ");
            for (MultipartFile img : imgs) {
                String fileUrl = "";
                try {
                    if (img != null && !img.isEmpty()) {
                        String fileName = UUID.randomUUID() + "_" + URLEncoder.encode(img.getOriginalFilename(), StandardCharsets.UTF_8.toString());

                        s3Client.putObject(PutObjectRequest.builder()
                                        .bucket(bucketName)
                                        .key(fileName)
                                        .build(),
                                RequestBody.fromBytes(img.getBytes()));

                        fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
                        fileUrls.add(fileUrl); // 변환된 URL 추가해줌
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }

            List<MenuImage> menuImages = fileUrls.stream()
                    .map(url -> MenuImage.builder()
                            .url(url)
                            .menu(menu)
                            .build())
                    .collect(Collectors.toList());

            for (MenuImage menuImage : menuImages) {
                menuImage.confirmMenu(menu);
            }

        }

        return "OK";
    }

    @Transactional
    public void removeImages(Menu menu){
        menu.removeImage();
        em.flush();
    }



    @Transactional
    public String removeMenu(Long id, Long userId){
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));

        User user = menu.getUser();
        user.getId(); // 프록시 초기화

        Place place = menu.getPlace();
        String placeName = place.getTitle(); // 프록시 초기화

        MenuList menuList = menu.getMenuList();
        String title = menuList.getTitle(); // 프록시 초기화

        // 삭제 시 연관관계 제거
        menu.removeMenuList(menuList);
        menu.removePlace(place);
        menu.removeUser(user);
        menu.getTags().forEach(menuTag -> menuTag.removeTag());

        menuRepository.delete(menu);

        return "OK";
    }

    @Transactional
    public List<Menu> findMenuByPlace(Long placeId){
        List<Menu> menuList = menuRepository.findMenuByPlaceId(placeId, Arrays.asList(MenuStatus.CREATED, MenuStatus.UPDATED))
                .orElseThrow(() -> new MenuNotFoundException());

        if(menuList.isEmpty()){
            throw new MenuNotFoundException();
        }

        return menuList;
    }

    @Transactional
    public Menu findMenuInfo(Long menuId, Long userId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new MenuNotFoundException());
    }



    /*

    @Transactional
    public Menu updateMenu(Long id, PatchMenuRequest patchMenuRequest) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        if (patchMenuRequest.getTitle() != null) {
            menu.setTitle(patchMenuRequest.getTitle());
        }
        if (patchMenuRequest.getImgUrl() != null) {
            menu.setImgUrl(patchMenuRequest.getImgUrl());
        }
        if (patchMenuRequest.getPrice() != 0) { // 가격이 0이 아닌 경우에만 업데이트
            menu.setPrice(patchMenuRequest.getPrice());
        }
        if (patchMenuRequest.getMemo() != null) {
            menu.setMemo(patchMenuRequest.getMemo());
        }

        return menuRepository.save(menu);
    }

    // 메뉴 삭제 *
    @Transactional
    public Menu deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));
        if (menu != null) {
            menu.setStatus(MenuStatus.DELETED); // 상태를 'DELETED'로 변경
            return menuRepository.save(menu); //  상태를 저장
        } else {
            return null;
        }
    }
    */
}

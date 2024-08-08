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
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    @Transactional(readOnly = true)
    public Long findMaxGroupId(Long userId){
        List<Menu> menus = menuRepository.findByUserId(userId);

        Long maxGroupId = null;

        for (Menu menu : menus) {
            Long groupId = menu.getGroupId(); // 각 메뉴의 groupId를 가져옵니다.

            // maxGroupId가 null이거나 현재 groupId가 maxGroupId보다 크면 업데이트
            if (maxGroupId == null || groupId > maxGroupId) {
                maxGroupId = groupId;
            }
        }

        // 최고 groupId가 null일 경우 1 반환
        return (maxGroupId != null) ? maxGroupId : 1L;
    }



    @Transactional
    // 메뉴 등록 * (이미지 제외)
    public PostMenuResponse createMenu(PostMenuRequest postMenuRequest, Long userId) {

        // 메뉴 이름
        String menuTitle = postMenuRequest.getMenuTitle();

        //
        Long maxGroupId = findMaxGroupId(userId);

        // 유저 정보 가져오기
        User finduser = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 유저가 없습니다."));

        // 장소 가져오기(식당이 없는 경우 새로 생성)
        Place place = placeService.createPlace(postMenuRequest.getStoreInfo(), userId);

        boolean isMenuExists = false;

        for (Menu menu : place.getMenus()) {
            if (menu.getTitle().equals(menuTitle)) {
                isMenuExists = true; // 동일한 메뉴 이름이 있으면 true
                break; // 하나라도 찾으면 루프 종료
            }
        }

        if(isMenuExists){
            throw new RuntimeException("해당 식당에 동일한 메뉴명이 이미 존재합니다");
        }


        List<Long> menuFolderIds = postMenuRequest.getMenuFolderIds();

        // 식당, 메뉴판 연관관계 설정

        for (Long menuFolderId : menuFolderIds) {
            // findMenuList에서 해당 menuFolderId에 맞는 메뉴판을 찾아서 confirmMenuList 호출
            MenuList menuList = menuListService.findMenuListById(menuFolderId, userId); // 해당 ID로 MenuList를 찾는 메서드 호출
            Menu menu = Menu.builder()
                    .title(postMenuRequest.getMenuTitle())
                    .price(postMenuRequest.getMenuPrice())
                    .user(finduser)
                    .memo(postMenuRequest.getMenuMemo())
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .groupId(maxGroupId)
                    .build();

            menu.confirmPlace(place); // 메뉴, 식당 연관관계
            menu.confirmMenuList(menuList); // 메뉴, 메뉴판 연관관계

            // 태그 연관관계
            List<MenuTag> menuTags = createMenuTags(postMenuRequest, menu);

            menuRepository.save(menu);
        }

        return new PostMenuResponse(maxGroupId);
    }


    // 메뉴 추가
    public Menu getMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));
    }


    // 메뉴 태그 등록
    private List<MenuTag> createMenuTags(PostMenuRequest postMenuRequest, Menu menu) {
        return postMenuRequest.getTagInfo().stream()
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
    }


    @Transactional
    public void createMenuImage(PostPhotoRequest request, long userId) {
        List<MultipartFile> imgs = request.getMenuImgs();
        long menuGroupId = request.getMenuGroupId();

        List<Menu> findGroupMenu = menuRepository.findByUserIdAndGroupId(menuGroupId, userId);


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
                e.printStackTrace(); // 예외가 발생하면 스택 트레이스를 출력
            }
        }

        for (Menu menu : findGroupMenu) {
            List<MenuImage> menuImages = fileUrls.stream()
                    .map(url -> MenuImage.builder()
                            .url(url)
                            .build())
                    .collect(Collectors.toList());

            for (MenuImage menuImage : menuImages) {
                menuImage.confirmMenu(menu);
                // 필요하다면 menuImage를 저장하는 로직도 추가할 수 있습니다.
            }
        }
    }

    @Transactional
    public void updateMenu(Long groupId, Long userId, PostMenuRequest postMenuRequest) {

        List<Menu> findGroupMenu = menuRepository.findByUserIdAndGroupId(userId, groupId);

        List<Menu> menus = menuRepository.findByGroupIdWithFetch(groupId);

        for (Menu menu : findGroupMenu) {
            removeMenu(menu);
        }

        createMenu(postMenuRequest, userId);

    }






    @Transactional
    public String updateMenuImage(PatchMenuImage patchMenuImage, long id, long userId) {
        log.info("현재 id 값은 : " + id);
        Menu menu = menuRepository.findMenuAndImages(id)
                .orElseThrow(() -> new RuntimeException("해당하는 매뉴가 없습니다."));

        List<MultipartFile> imgs = patchMenuImage.getMenuImgs();

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
    public String removeMenu(Menu menu){

        User user = menu.getUser();
        Place place = menu.getPlace();
        MenuList menuList = menu.getMenuList();

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

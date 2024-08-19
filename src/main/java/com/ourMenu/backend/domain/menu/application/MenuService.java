package com.ourMenu.backend.domain.menu.application;

import com.ourMenu.backend.domain.menu.domain.*;
import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.dto.request.*;
import com.ourMenu.backend.domain.menu.dto.response.MenuDetailDto;
import com.ourMenu.backend.domain.menu.dto.response.MenuDto;
import com.ourMenu.backend.domain.menu.dto.response.MenuIdDto;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menu.exception.MenuNotFoundException;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.user.application.UserService;
import com.ourMenu.backend.domain.user.domain.User;
import com.ourMenu.backend.global.util.S3Service;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final S3Service s3Service;

    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;

    // 모두 조회
    @Transactional
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }


    public List<Long> getMenuIdsByUserId(Long userId) {
        List<Menu> menus = menuRepository.findByUserId(userId); // 유저 ID로 메뉴 조회
        return menus.stream()
                .map(Menu::getId) // 메뉴 ID를 추출
                .collect(Collectors.toList()); // 리스트로 변환
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
        return (maxGroupId != null) ? maxGroupId + 1 : 1L;
    }


    @Transactional
    // 메뉴 등록 * (이미지 제외)
    public PostMenuResponse createMenu(PostMenuRequest postMenuRequest, Long userId) {

        // 메뉴 이름
        String menuTitle = postMenuRequest.getMenuTitle();

        List<Long> menuFolderIds = postMenuRequest.getMenuFolderIds();
        //
        Long maxGroupId = findMaxGroupId(userId);

        // 유저 정보 가져오기
        User finduser = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 유저가 없습니다."));

        // 장소 가져오기(식당이 없는 경우 새로 생성)
        Place place = placeService.createPlace(postMenuRequest.getStoreInfo(), userId);

        boolean isMenuExists = false;
        Long existingId = null;

        for (Menu menu : place.getMenus()) {
            // 메뉴 제목과 menuFolderId가 모두 일치하는 경우
            if (menu.getTitle().equals(menuTitle) && menuFolderIds.contains(menu.getMenuList().getId())) {
                existingId = menu.getMenuList().getId(); // 기존 메뉴의 ID를 저장
                isMenuExists = true; // 제목과 folderId가 모두 일치하면 true
                break; // 하나라도 찾으면 루프 종료
            }
        }

        if (isMenuExists) {
            throw new RuntimeException("해당 식당에 이미 동일한 메뉴명이 존재합니다. 메뉴판 ID: " + existingId + " (해당 메뉴판에서 확인해주세요.)");
        }


        // 식당, 메뉴판 연관관계 설정

        for (Long menuFolderId : menuFolderIds) {
            // findMenuList에서 해당 menuFolderId에 맞는 메뉴판을 찾아서 confirmMenuList 호출
            MenuList menuList = menuListService.findMenuListById(menuFolderId, userId); // 해당 ID로 MenuList를 찾는 메서드 호출
            Menu menu = Menu.builder()
                    .title(postMenuRequest.getMenuTitle())
                    .price(postMenuRequest.getMenuPrice())
                    .user(finduser)
                    .memo(postMenuRequest.getMenuMemo())
                    .menuIconType(postMenuRequest.getMenuIconType())
                    .memoTitle(postMenuRequest.getMenuMemoTitle())
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


    // 메뉴 태그 등록
    @Transactional
    public List<MenuTag> createMenuTags(PostMenuRequest postMenuRequest, Menu menu) {
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
    public void createMenuImage(PostPhotoRequest request, long userId, long groupId) {

        List<MultipartFile> imgs = request.getMenuImgs();



        List<Menu> findGroupMenu = menuRepository.findByUserIdAndGroupId(userId, groupId);
        if (findGroupMenu == null || findGroupMenu.isEmpty()) {
            throw new RuntimeException("해당하는 메뉴가 없습니다");
        }

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile img : imgs) {
            String fileUrl = "";
            try {
                if (img != null && !img.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" +
                            URLEncoder.encode(img.getOriginalFilename(), StandardCharsets.UTF_8.toString());

                    // S3Service의 uploadFile 메서드를 사용하여 파일 업로드
                    fileUrl = s3Service.uploadFile(fileName, img); // S3Service를 통해 업로드
                    fileUrls.add(fileUrl); // 변환된 URL 추가
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

        Menu menu = menuRepository.findMenuAndImages(id)
                .orElseThrow(() -> new RuntimeException("해당하는 매뉴가 없습니다."));

        List<MultipartFile> imgs = patchMenuImage.getMenuImgs();

        List<String> fileUrls = new ArrayList<>();
        if(imgs != null) {
            removeImages(menu);

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
    public List<Menu> findMenuByPlace(Long placeId, Long userId){
        List<Menu> menuList = menuRepository.findMenuByPlaceIdAndUserId(placeId, userId, Arrays.asList(MenuStatus.CREATED, MenuStatus.UPDATED))
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


    @Transactional
    public MenuDetailDto getCertainMenu(Long userId, Long groupId) {
        List<Menu> menu = menuRepository.findCertainMenuByUserIdAndGroupId(userId, groupId);
        return MenuDetailDto.toDto(menu);
    }

//    @Transactional
//    public List<MenuDto> getAllMenusByCriteria(String title, String tag, Integer menuFolderId, Long userId) {
//        List<Menu> menus = menuRepository.findingMenusByCriteria(title, tag, menuFolderId, userId);
//        return MenuDto.toDto(menus); // List<MenuDto> 반환
//    }

    @Transactional
    public Page<MenuDto> getAllMenusByCriteria2(String[] tags, Integer menuFolderId, Long userId, int minPrice, int maxPrice, Pageable pageable){
        // 메뉴를 페이징 처리하여 조회

        Integer tagCount = (tags != null && tags.length > 0) ? Integer.valueOf(tags.length) : Integer.valueOf(0); // 태그가 없으면 0으로 설정

        if (minPrice == 5000) {
            minPrice = 0; // 기본값: 5,000원 (최소)
        }
        if (maxPrice == 50000) {
            maxPrice = 999999; // 기본값: 무한대 (최대)
        }

        Page<Menu> menuPage = menuRepository.findingMenusByCriteria2(tags, menuFolderId, userId, minPrice, maxPrice, tagCount, pageable);

        // Menu 엔티티를 MenuDto로 변환
        List<MenuDto> menuDtos = MenuDto.toDto(menuPage.getContent());

        // Page<MenuDto> 객체로 변환하여 반환
        return new PageImpl<>(menuDtos, pageable, menuPage.getTotalElements());
    }

    @Transactional
    public void removeCertainMenu(Long menuId, Long userId) {
        Menu menu = menuRepository.findByIdAndUserId(menuId, userId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다"));

        removeMenu(menu);
    }

    @Transactional
    public void removeAllMenus(Long groupId, Long userId) {
        List<Menu> byUserIdAndGroupId = menuRepository.findByUserIdAndGroupId(userId, groupId);
        for (Menu menu : byUserIdAndGroupId) {
            removeMenu(menu);
        }
    }


    @Transactional
    public List<Menu> getAllMenusByTagName(String tag, Long userId){
        String[] integers = {tag};
        int tagCount = integers.length;
        Pageable pageable = PageRequest.of(0, 5);
        Page<Menu> menuPage = menuRepository.findingMenusByCriteria2(integers, null,  userId, 0, 999999, tagCount, pageable);
        List<Menu> menuList = menuPage.getContent();
        return menuList; // List<MenuDto> 반환
    }

    @Transactional
    public List<Menu> getAllOtherMenusByTagName(String tag, Long userId){

        return menuRepository.findingMenusByCriteria3(tag);
    }

    public List<Menu> getAllMenusByTagNameAndUserIdNot(String tagName, Long userId) {
        return null;
        //return menuRepository.findMenusByTagNameAndUserIdNot(tagName, userId);
    }

    @Transactional
    public List<Menu> getAllMenusByGroupIdAndUserId(Long groupId, Long userId){
        List<Menu> menuList = menuRepository.findByUserIdAndGroupId(userId, groupId);
        for (Menu menu : menuList) {
            menu.updateModifiedAt();
            menuRepository.save(menu);
        }
        return menuList;

    }

    public MenuIdDto getCertainMenuId(Long userId, Long menuFolderId, Long groupId) {
        Menu menu = menuRepository.findByUserIdAndMenuListIdAndGroupId(userId, menuFolderId, groupId)
                .orElseThrow(() -> new MenuNotFoundException("해당하는 메뉴가 없습니다"));
        return MenuIdDto.toDto(menu);
    }

    public void updateModifiedAt(Menu menu){
        menu.updateModifiedAt();
    }

    public List<Menu> getMenuByGroupId(List<Long>groupIdList, Long userId) {
        Map<Long,Menu> map = new HashMap<>();
        for (Long groupId : groupIdList) {
            List<Menu> menuList = menuRepository.findOneByGroupIdAndUserId(groupId, userId);
            if(menuList.size()==0)
                throw new RuntimeException("해당하는 메뉴가 없습니다");
            map.put(menuList.get(0).getGroupId(),menuList.get(0));
        }
        return map.values().stream().toList();
    }

}

package com.ourMenu.backend.domain.menulist.application;

import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menulist.exception.ImageLoadException;
import com.ourMenu.backend.domain.menulist.exception.MenuListException;
import com.ourMenu.backend.domain.menulist.dao.MenuListRepository;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menulist.dto.request.MenuListRequestDTO;
import com.ourMenu.backend.domain.user.application.UserService;
import com.ourMenu.backend.domain.user.domain.User;
import com.ourMenu.backend.domain.user.exception.UserException;
import com.ourMenu.backend.global.exception.ErrorCode;
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
import java.nio.file.Paths;
import java.util.*;

import static com.ourMenu.backend.global.common.Status.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class MenuListService {

    private final MenuListRepository menuListRepository;
    private final MenuRepository menuRepository;
    private final S3Client s3Client;
    private final UserService userService;

    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;

    /** 새 메뉴판 생성 */
//    @Transactional
//    public MenuList createMenuList(MenuList menuList) {
//        return menuListRepository.save(menuList);
//    }

    //메뉴판 생성
//    @Transactional
//    public MenuList createMenuList(MenuListRequestDTO request){
//        MenuList menuList = MenuList.builder().title(request.getTitle()).imgUrl(request.getImg()).iconType(request.getIconType()).build();
//        return menuListRepository.save(menuList);
//    }

    @Transactional
    public MenuList createMenuList(MenuListRequestDTO request, Long userId) {
        MultipartFile file = request.getMenuFolderImg();
        String fileUrl = null;
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
        Long maxPriority = menuListRepository.findMaxPriorityByUserId(userId).orElse(0L);
        Long newPriority = maxPriority + 1;

        try {
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());

                s3Client.putObject(PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(fileName)
                                .build(),
                        RequestBody.fromBytes(file.getBytes()));

                fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();

            }
        }catch (Exception e) {
            throw new ImageLoadException();
        }

        MenuList menuList = MenuList.builder()
                .title(request.getMenuFolderTitle())
                .imgUrl(fileUrl)
                .user(user)
                .iconType(request.getMenuFolderIcon())
                .priority(newPriority)
                .build();

        return menuListRepository.save(menuList);

    }

    // 메뉴판 조회 //
    @Transactional
    public MenuList getMenuListByName(String title, Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return menuListRepository.findMenuListByTitle(title, userId, Arrays.asList(CREATED, UPDATED))
                .orElseThrow(() -> new MenuListException());
    }

    //메뉴판 전체 조회
    @Transactional
    public List<MenuList> getAllMenuList(Long userId){

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return menuListRepository.findAllMenuList(Arrays.asList(CREATED, UPDATED), userId)
                .orElseThrow(() -> new MenuListException());
    }

    //메뉴판 업데이트
    @Transactional
    public MenuList updateMenuList(Long menuFolderId, MenuListRequestDTO request, Long userId) {

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        MenuList menuList = menuListRepository.findMenuListsById(menuFolderId, userId, Arrays.asList(CREATED, UPDATED))
                .orElseThrow(() -> new MenuListException());

        MenuList.MenuListBuilder updateMenuListBuilder = menuList.toBuilder();


        if (request.getMenuFolderTitle() != null) {
            updateMenuListBuilder.title(request.getMenuFolderTitle());
        }
        if (request.getMenuFolderImg() != null) {
            MultipartFile file = request.getMenuFolderImg();
            String fileUrl = "";

            try {
                if (file != null && !file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());
                    s3Client.putObject(PutObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(fileName)
                                    .build(),
                            Paths.get(file.getOriginalFilename()));

                    fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
                    updateMenuListBuilder.imgUrl(fileUrl);
                }
            } catch (Exception e) {
                throw new ImageLoadException();
            }
        }

            if (request.getMenuFolderIcon() != null) {
                updateMenuListBuilder.iconType(request.getMenuFolderIcon());
            }

            MenuList updateMenuList = updateMenuListBuilder.build();

            return menuListRepository.save(updateMenuList);
    }


    //메뉴판 삭제
    @Transactional
    public String removeMenuList(Long menuFolderId, Long userId){

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        MenuList menuList = menuListRepository.findMenuListsById(menuFolderId, userId, Arrays.asList(CREATED, UPDATED))
                .orElseThrow(() -> new MenuListException("해당 메뉴판이 존재하지 않습니다."));

//        MenuList.MenuListBuilder removeMenuListBuilder = menuList.toBuilder();
//
//        removeMenuListBuilder.status(Status.DELETED);
//
//        MenuList removeMenuList = removeMenuListBuilder.build();
//

        Long currentPriority = menuList.getPriority();
        menuList.softDelete();
        menuListRepository.save(menuList);

        menuListRepository.decreasePriorityGreaterThan(currentPriority);

        return "OK";
    }


    @Transactional
    public String hardDeleteMenuList(Long menuFolderId, Long userId){
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        MenuList menuList = menuListRepository.findMenuListsById(menuFolderId, userId, Arrays.asList(CREATED, UPDATED))
                .orElseThrow(() -> new MenuListException("해당 메뉴판이 존재하지 않습니다."));

        Long priority = menuList.getPriority();

        List<Menu> menus = menuList.getMenus();


        for (Menu menu : menus) {
            menuList.removeMenu(menu);
            menu.removeMenuList(menuList);
            menuRepository.delete(menu);
        }

        menuList.removeUser(user);
        menuListRepository.delete(menuList);
        menuListRepository.decreasePriorityGreaterThan(priority);
        return "OK";
    }


    @Transactional
    public String setPriority(Long menuFolderId, Long newPriority, Long userId) {
        MenuList menuList = menuListRepository.findById(menuFolderId)
                .orElseThrow(() -> new MenuListException());

        Long currentPriority = menuList.getPriority();
        Long maxPriority = menuListRepository.findMaxPriorityByUserId(userId).orElseThrow(() -> new RuntimeException("메뉴판 존재 X"));

        if (newPriority <= 0 || newPriority > maxPriority) {
            throw new RuntimeException("유효하지 않은 우선순위입니다.");
        }

        if(currentPriority < newPriority){
            menuListRepository.decreasePriorityBetween(currentPriority, newPriority);
        }else{
            menuListRepository.increasePriorityBetween(currentPriority, newPriority);
        }

        MenuList updateMenuList = menuList.toBuilder().priority(newPriority).build();
        menuListRepository.save(updateMenuList);

        return "OK";
    }

    /** 메뉴판 메뉴 추가 */
    /*


    @Transactional
    public MenuList deleteMenu(Long menuId, Long menuListId ) {

        MenuList menuList = menuListRepository.findById(menuListId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));


        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));

        // 삭제할 메뉴와 관련된 중간테이블 찾기
        MenuMenuList menuMenuList = menuList.getMenuMenuLists().stream()
                .filter(m -> m.getMenu().getId().equals(menuId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 메뉴가 메뉴판에 없습니다."));

        // 메뉴판에서 삭제
        menuList.removeMenuMenuList(menuMenuList);

        return menuListRepository.save(menuList);
    }


    // 모든 메뉴판 조회
    @Transactional
    public List<MenuList> getAllMenuLists() {
        return menuListRepository.findAll();
    }

    // 특정 메뉴판 조회
    @Transactional
    public MenuList getMenuListById(Long id) {
        return menuListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));
    }


    // 메뉴판 업데이트
    @Transactional
    public MenuList updateMenuList(Long id, MenuList menuListDetails) {
        MenuList menuList = menuListRepository.findById(id).orElse(null);
        if (menuList != null) {
            menuList.setTitle(menuListDetails.getTitle());
            menuList.setModifiedAt(menuListDetails.getModifiedAt());
            menuList.setStatus(menuListDetails.getStatus());
            menuList.setImgUrl(menuListDetails.getImgUrl());

            return menuListRepository.save(menuList);
        } else {
            return null;
        }
    }


    @Transactional
    public MenuList updateMenuList(Long id, PatchMenuListRequest patchMenuListRequest) {
        MenuList menuList = menuListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuList not found"));

        if (patchMenuListRequest.getTitle() != null){
            menuList.setTitle(patchMenuListRequest.getTitle());
        }
        if (patchMenuListRequest.getImgUrl() != null){
            menuList.setImgUrl(patchMenuListRequest.getImgUrl());
        }

        return menuListRepository.save(menuList);
    }

    // 메뉴판 삭제
    @Transactional
    public MenuList deleteMenuList(Long id) {
        MenuList menuList = menuListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));
        if (menuList != null) {
            menuList.setStatus(MenuListStatus.DELETED); // 상태를 'DELETED'로 변경
            return menuListRepository.save(menuList); //  상태를 저장
        } else {
            return null;
        }
    }

    */
}

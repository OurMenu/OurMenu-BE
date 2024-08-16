package com.ourMenu.backend.domain.menulist.api;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.response.MenuDto;
import com.ourMenu.backend.domain.menulist.dto.request.PatchMenuListRequestDTO;
import com.ourMenu.backend.domain.menulist.dto.response.*;
import com.ourMenu.backend.domain.menulist.exception.ImageLoadException;
import com.ourMenu.backend.domain.menulist.exception.MenuListException;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menulist.dto.request.MenuListRequestDTO;
import com.ourMenu.backend.domain.menulist.exception.PriorityException;
import com.ourMenu.backend.domain.user.application.UserService;
import com.ourMenu.backend.domain.user.exception.UserException;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.exception.ErrorCode;
import com.ourMenu.backend.global.exception.ErrorResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/menuFolder")
public class MenuListApiController {

    private final MenuListService menuListService;
    private final UserService userService;

    @ExceptionHandler(MenuListException.class)
    public ResponseEntity<?> menuListException(MenuListException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.MENU_LIST_NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> userException(UserException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.USER_NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(ImageLoadException.class)
    public ResponseEntity<?> imageLoadException(ImageLoadException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.IMAGE_NOT_LOADED_ERROR, e.getMessage()));
    }

    @ExceptionHandler(PriorityException.class)
    public ResponseEntity<?> priorityException(PriorityException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.PRIORITY_NOT_VALID, e.getMessage()));
    }

    //메뉴판 등록
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MenuListResponseDTO> createMenuList(@ModelAttribute MenuListRequestDTO request, @UserId Long userId){
        MenuList menuList = menuListService.createMenuList(request, userId);

        List<MenuGroupIdDTO> menuIdGroupIdList = menuList.getMenus().stream()
                .map(menu -> MenuGroupIdDTO.builder()
                        .menuId(menu.getId())
                        .groupId(menu.getGroupId())
                        .build())
                .collect(Collectors.toList());

        MenuListResponseDTO response = MenuListResponseDTO.builder()
                .menuFolderId(menuList.getId())
                .menuFolderTitle(menuList.getTitle())
                .menuFolderImgUrl(menuList.getImgUrl())
                .menuFolderIcon(menuList.getIconType())
                .menuFolderPriority(menuList.getPriority())
                .menuIds(menuIdGroupIdList)
                .build();

//        log.info("menuId = " + menuList.getMenus().stream().map(menu -> menu.getId()));

        return ApiUtils.success(response);

    }


//    메뉴판 전체 조회
    @GetMapping("")
    public ApiResponse<GetMenuFolderDTO> findAllMenuList(@UserId Long userId){
        List<MenuList> menuLists = menuListService.getAllMenuList(userId);

        // 고유한 groupId를 저장하기 위한 Set
        Set<Long> uniqueGroupIds = new HashSet<>();

        int menuCount = 0;

        for (MenuList menuList : menuLists) {
            for (Menu menu : menuList.getMenus()) {
                uniqueGroupIds.add(menu.getGroupId()); // 고유한 groupId를 Set에 추가
            }
        }

        menuCount = uniqueGroupIds.size();


        List<GetMenuFolderResponse> responses = menuLists.stream().map(menuList ->

                GetMenuFolderResponse.builder()
                        .menuFolderId(menuList.getId())
                        .menuFolderTitle(menuList.getTitle())
                        .menuCount((long) menuList.getMenus().size())
                        .menuFolderImgUrl(menuList.getImgUrl())
                        .menuFolderIcon(menuList.getIconType())
                        .menuFolderPriority(menuList.getPriority())
                        .menuIds(
                                menuList.getMenus().stream().map(menu ->
                                        MenuGroupIdDTO.builder()
                                                .menuId(menu.getId())
                                                .groupId(menu.getGroupId())
                                                .build()
                                ).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());

        GetMenuFolderDTO response = GetMenuFolderDTO.builder()
                .menuCount(menuCount)
                .menuFolders(responses)
                .build();

        return ApiUtils.success(response);
    }

    //메뉴판

    @PatchMapping(value = "/{menuFolderId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<MenuListResponseDTO> updateMenuList(@PathVariable Long menuFolderId, @UserId Long userId, @ModelAttribute PatchMenuListRequestDTO request){
        MenuList menuList = menuListService.updateMenuList(menuFolderId, request,  userId);

        List<MenuGroupIdDTO> menuIdGroupIdList = menuList.getMenus().stream()
                .map(menu -> MenuGroupIdDTO.builder()
                        .menuId(menu.getId())
                        .groupId(menu.getGroupId())
                        .build())
                .collect(Collectors.toList());

        MenuListResponseDTO response = MenuListResponseDTO.builder()
                .menuFolderId(menuList.getId())
                .menuFolderTitle(menuList.getTitle())
                .menuFolderImgUrl(menuList.getImgUrl())
                .menuFolderIcon(menuList.getIconType())
                .menuFolderPriority(menuList.getPriority())
                .menuIds(menuIdGroupIdList)
                .build();

        return ApiUtils.success(response);
    }


    //SoftDelete
//    @DeleteMapping("/{id}")
//    public ApiResponse<String> removeMenuList(@PathVariable Long id, @UserId Long userId){
//        String response = menuListService.removeMenuList(id, userId);       //STATUS를 DELETED로 변환
//        return ApiUtils.success(response);  //OK 반환
//    }

    //HardDelete
    @DeleteMapping("/{menuFolderId}")
    public ApiResponse<String> removeMenuList(@PathVariable Long menuFolderId, @UserId Long userId){
        String response = menuListService.hardDeleteMenuList(menuFolderId, userId);
        return ApiUtils.success(response);  //OK 반환
    }

    @PatchMapping("/priority/{menuFolderId}")
    public ApiResponse<String> changePriority(@PathVariable Long menuFolderId, @RequestParam Long newPriority, @UserId Long userId){
        String response = menuListService.setPriority(menuFolderId, newPriority ,userId);
        return ApiUtils.success(response);
    }

    @GetMapping("/{menuFolderId}")
    public ApiResponse<GetMenuFolder> getMenuFolder(@PathVariable Long menuFolderId, @UserId Long userId){
        MenuList menuList = menuListService.findMenuListById(menuFolderId, userId);

        List<MenuDto> menus = MenuDto.toDto(menuList.getMenus());

        int menuCount = menuList.getMenus().size();


        GetMenuFolder response = GetMenuFolder.builder()
                .menuFolderId(menuList.getId())
                .menuFolderTitle(menuList.getTitle())
                .menuFolderIcon(menuList.getIconType())
                .menuFolderImg(menuList.getImgUrl())
                .menuCount(menuCount)
                .menus(menus)
                .build();

        return ApiUtils.success(response);
    }

    /*

    메뉴판 메뉴 삭제

    @DeleteMapping("")
    public ApiResponse<String> removeMenuInMenuList(@RequestParam Long menuId, @RequestParam Long menuListId){
        menuListService.deleteMenu(menuId, menuListId);
        return ApiUtils.success("OK");
    }



    Get 메서드 Response 생성 메서드

    private static MenuListDto getMenuListDto(MenuList menuList) {
        MenuListDto menuListDto = MenuListDto.builder()
                .id(menuList.getId())
                .title(menuList.getTitle())
                .status(menuList.getStatus())
                .imgUrl(menuList.getImgUrl())
                .createdAt(menuList.getCreatedAt())
                .modifiedAt(menuList.getModifiedAt())
                .menus(menuList.getMenuMenuLists().stream()
                        .map(menuMenuList -> MenuDto.builder()
                                .id(menuMenuList.getMenu().getId())
                                .title(menuMenuList.getMenu().getTitle())
                                .price(menuMenuList.getMenu().getPrice())
                                .createdAt(menuMenuList.getMenu().getCreatedAt())
                                .modifiedAt(menuMenuList.getMenu().getModifiedAt())
                                .memo(menuMenuList.getMenu().getMemo())
                                .imgUrl(menuMenuList.getMenu().getImgUrl())
                                .status(menuMenuList.getMenu().getStatus())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        return menuListDto;
    }

     */
}






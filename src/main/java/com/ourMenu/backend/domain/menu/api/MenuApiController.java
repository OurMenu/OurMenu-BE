package com.ourMenu.backend.domain.menu.api;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.domain.*;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.PostPhotoRequest;
import com.ourMenu.backend.domain.menu.dto.response.*;
import com.ourMenu.backend.domain.menu.exception.MenuNotFoundException;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
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


import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Slf4j
public class MenuApiController {

    private final MenuService menuService;

    private final MenuListService menuListService;
    private final MenuRepository menuRepository;

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<?> menuNotFoundException(MenuNotFoundException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.MENU_NOT_FOUND, e.getMessage()));
    }

    // 전체 조회
    @GetMapping("")
    public ApiResponse<List<MenuDto>> getAllMenu(@RequestParam(required = false) String title,
                                              @RequestParam(required = false) String tag,
                                              @RequestParam(required = false) Integer menuFolderId, @UserId Long userId) {
        List<MenuDto> menusByCriteria = menuService.getAllMenusByCriteria(title, tag, menuFolderId, userId);

        return ApiUtils.success(menusByCriteria);
    }


    // 특정 메뉴 조회
    @GetMapping("/{groupId}")
    public ApiResponse<MenuDto> getMenu(@UserId Long userId, @PathVariable Long groupId) {
        MenuDto certainMenu = menuService.getCertainMenu(userId, groupId);

        return ApiUtils.success(certainMenu);
    }

    // 메뉴 생성
    @PostMapping("")
    public ApiResponse<PostMenuResponse> saveMenu(@RequestBody PostMenuRequest postMenuRequest, @UserId Long id) {
        PostMenuResponse postMenuResponse = menuService.createMenu(postMenuRequest, id);
        return ApiUtils.success(postMenuResponse);
    }

    // 이미지 추가
    @PostMapping(value = "/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> saveMenuImage(@ModelAttribute PostPhotoRequest photoRequest, @UserId Long userId) {
        menuService.createMenuImage(photoRequest, userId);
        return ApiUtils.success("OK");
    }

//    @GetMapping("")
//    public ApiResponse<List<MenuDto>> getMenu(
//            @RequestParam(required = false) String menuTitle,
//            @RequestParam(required = false) String menuTag,
//            @RequestParam(required = false) Integer menuFolderId,
//
//            @UserId Long userId) {
//
//
//        Page<Menu> menuPage = menuRepository.findMenusByCriteria(menuTitle, menuFolderId, userId);
//
//        List<MenuDto> menuDtos = MenuDto.toDto(menuPage.getContent());
//
//        return ApiUtils.success(menuDtos);
//    }








    // 삭제
    @DeleteMapping("/{menuId}")
    public ApiResponse<String> removeMenu (@PathVariable Long menuId, @UserId Long userId){
        menuService.removeCertainMenu(menuId, userId);       // Hard Delete
        return ApiUtils.success("OK");  //OK 반환
    }

    @DeleteMapping("/group/{groupId}")
    public ApiResponse<String> removeAllMenu(@PathVariable Long groupId, @UserId Long userId){
        menuService.removeAllMenus(groupId, userId);
        return ApiUtils.success("OK");  //OK 반환
    }

    @PatchMapping("/{groupId}")
    public ApiResponse<String> updateMenu (@PathVariable Long groupId, @UserId Long userId, PostMenuRequest postMenuRequest){
        menuService.updateMenu(groupId, userId, postMenuRequest);       // Hard Delete
        return ApiUtils.success("OK");  //OK 반환
    }




    @PatchMapping(value = "/{groupId}/photo",
            consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateMenuImages(@PathVariable Long groupId, @UserId Long userId, @ModelAttribute PostPhotoRequest photoRequest){
        menuService.createMenuImage(photoRequest, userId);

        return ApiUtils.success("OK");
    }



    @GetMapping("/place/{placeId}")
    public ApiResponse<List<PlaceMenuDTO>> findMenuByPlace(@PathVariable Long placeId) {
        List<Menu> menuList = menuService.findMenuByPlace(placeId);
        List<PlaceMenuDTO> response = menuList.stream().map(menu ->
                PlaceMenuDTO.builder()
                        .menuId(menu.getId())
                        .menuTitle(menu.getTitle())
                        .menuPrice(menu.getPrice())
                        .menuIcon(menu.getIcon())
                        .menuTags(menu.getTags().stream().map(tag ->
                                TagDTO.builder()
                                        .tagTitle(tag.getTag().getName())
                                        .isCustom(tag.getTag().getIsCustom())
                                        .build())
                                .collect(Collectors.toList())
                        )
                        .menuImgsUrl(menu.getImages().stream().map(image ->
                                        MenuImageDto.builder() // ImageDTO 클래스에 맞게 수정
                                                .menuImgUrl(image.getUrl()) // 이미지 URL 필드 예시
                                                .build())
                                .collect(Collectors.toList())
                        )
                        .menuFolder(PlaceMenuFolderDTO.builder()
                                .menuFolderTitle(menu.getMenuList().getTitle())
                                .menuIcon(menu.getMenuList().getIconType())
                                .build())
                        .build())
                .collect(Collectors.toList());
        return ApiUtils.success(response);
    }
}

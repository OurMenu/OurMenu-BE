package com.ourMenu.backend.domain.menu.api;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.domain.*;
import com.ourMenu.backend.domain.menu.dto.request.PatchMenuImage;
import com.ourMenu.backend.domain.menu.dto.request.PatchMenuRequest;
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
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 메뉴 생성
    @PostMapping("")
    public ApiResponse<PostMenuResponse> saveMenu(@RequestBody PostMenuRequest postMenuRequest, @UserId Long id) {
        PostMenuResponse postMenuResponse = menuService.createMenu(postMenuRequest, id);
        return ApiUtils.success(postMenuResponse);
    }

    // 이미지 추가
    @PostMapping(value = "/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> saveMenuImage(@ModelAttribute PostPhotoRequest photoRequest) {
        menuService.createMenuImage(photoRequest);
        return ApiUtils.success("OK");
    }

    @GetMapping("")
    public ApiResponse<List<MenuDto>> getMenu(@RequestParam(required = false) String title,
                                              @RequestParam(required = false) String tag,
                                              @RequestParam(required = false) Integer menuFolderId, @UserId Long userId) {
        List<Menu> menus = menuRepository.findMenusByCriteria(title, tag, menuFolderId, userId);
        List<MenuDto> menuDtos = MenuDto.toDto(menus);

        return ApiUtils.success(menuDtos);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<String> removeMenu (@PathVariable Long id, @UserId Long userId){
        String response = menuService.removeMenu(id, userId);       // Hard Delete
        return ApiUtils.success(response);  //OK 반환
    }

    @PatchMapping("/{id}")
    public ApiResponse<String> updateMenu (@PathVariable Long id, @UserId Long userId, PatchMenuRequest patchMenuRequest){
        menuService.updateMenu(id, userId, patchMenuRequest);       // Hard Delete
        return ApiUtils.success("OK");  //OK 반환
    }

    @PatchMapping(value = "/{id}/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> updateMenuImages(@PathVariable Long id, @UserId Long userId, PatchMenuImage patchMenuImage){
        menuService.updateMenuImage(patchMenuImage, id, userId);
        return ApiUtils.success("OK");
    }


    @GetMapping("/place/{placeId}")
    public ApiResponse<List<PlaceMenuDTO>> findMenuByPlace(@PathVariable Long placeId) {
        List<Menu> menuList = menuService.findMenuByPlace(placeId);
        List<PlaceMenuDTO> response = menuList.stream().map(menu ->
                PlaceMenuDTO.builder()
                        .menuId(menu.getId())
                        .menuTitle(menu.getTitle())
                        .price(menu.getPrice())
                        .icon(menu.getIcon())
                        .tags(menu.getTags().stream().map(tag ->
                                TagDTO.builder()
                                        .tagTitle(tag.getTag().getName())
                                        .isCustom(tag.getTag().getIsCustom())
                                        .build())
                                .collect(Collectors.toList())
                        )
                        .images(menu.getImages())
                        .menuFolder(PlaceMenuFolderDTO.builder()
                                .menuFolderTitle(menu.getMenuList().getTitle())
                                .icon(menu.getMenuList().getIconType())
                                .build())
                        .build())
                .collect(Collectors.toList());
        return ApiUtils.success(response);
    }

    @GetMapping("/menuInfo/{menuId}")
    public ApiResponse<MenuInfoDTO> findMenuInfo(@PathVariable Long menuId, @UserId Long userId){
        Menu menu = menuService.findMenuInfo(menuId, userId);
        MenuInfoDTO response = MenuInfoDTO.builder()
                .menuId(menu.getId())
                .placeId(menu.getPlace().getId())
                .menuTitle(menu.getTitle())
                .price(menu.getPrice())
                .memo(menu.getMemo())
                .icon(menu.getIcon())
                .tags(menu.getTags().stream().map(tag ->
                                TagDTO.builder()
                                        .tagTitle(tag.getTag().getName())
                                        .isCustom(tag.getTag().getIsCustom())
                                        .build())
                        .collect(Collectors.toList()))
                .images(menu.getImages())
                .menuFolder(PlaceMenuFolderDTO.builder()
                        .menuFolderTitle(menu.getMenuList().getTitle())
                        .icon(menu.getMenuList().getIconType())
                        .build())
                .build();

        return ApiUtils.success(response);
    }
    /*
    ID를 통한 메뉴 조회

    @GetMapping("/{id}")
    public ApiResponse<MenuDto> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id);

        MenuDto response = menuDto(menu);

        return ApiUtils.success(response);
    }


    @GetMapping("")
    public ApiResponse<List<MenuDto>> getAllMenu() {
        List<Menu> menuList = menuService.getAllMenus();
        List<MenuDto> responseList = menuList.stream().map(menu -> {
            return menuDto(menu);
        }).collect(Collectors.toList());

        return ApiUtils.success(responseList);
    }

    /*
    메뉴 삭제

    @DeleteMapping("/{id}")
    public ApiResponse<String> removeMenu(@PathVariable Long id){
        menuService.deleteMenu(id);
        return ApiUtils.success("OK");
    }

//    @PatchMapping("/{id}")
//    public ApiResponse<PatchMenuResponse> updateMenu(@PathVariable Long id, @RequestParam String title,
//                                                     @RequestParam String imgUrl,
//                                                     @RequestParam int price, @RequestParam String memo){
//        Menu menu = new Menu();
//        menu.setTitle(title);
//        menu.setImgUrl(imgUrl);
//        menu.setPrice(price);
//        menu.setMemo(memo);
//
//        Menu updatedMenu = menuService.updateMenu(id, menu);
//
//        PatchMenuResponse response = new PatchMenuResponse();
//        response.setId(updatedMenu.getId());
//        response.setTitle(updatedMenu.getTitle());
//        response.setImgUrl(updatedMenu.getImgUrl());
//        response.setPrice(updatedMenu.getPrice());
//        response.setMemo(updatedMenu.getMemo());
//        response.setCreatedAt(updatedMenu.getCreatedAt());
//        response.setModifiedAt(updatedMenu.getModifiedAt());
//        response.setStatus(updatedMenu.getStatus());
//
//
//        return ApiUtils.success(response);
//    }


    메뉴 업데이트

    @PatchMapping("/{id}")
    public ApiResponse<MenuDto> updateMenu(@PathVariable Long id, @RequestBody PatchMenuRequest patchMenuRequest){
        Menu updatedMenu = menuService.updateMenu(id, patchMenuRequest);

        MenuDto response = menuDto(updatedMenu);
        return ApiUtils.success(response);
    }



    private static MenuDto menuDto(Menu menu) {
        MenuDto response = MenuDto.builder()
                        .id(menu.getId())
                .title(menu.getTitle())
                .price(menu.getPrice())
                .imgUrl(menu.getImgUrl())
                .createdAt(menu.getCreatedAt())
                .modifiedAt(menu.getModifiedAt())
                .memo(menu.getMemo())
                .status(menu.getStatus())
                .build();
        return response;
    }

     */
}

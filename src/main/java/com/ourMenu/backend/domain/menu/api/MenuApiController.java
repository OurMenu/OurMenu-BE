package com.ourMenu.backend.domain.menu.api;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.domain.*;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuIdRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("")
    public ApiResponse<List<MenuDto>> getAllMenu(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String[] tags, // 태그를 배열로 받도록 수정
            @RequestParam(required = false) Integer menuFolderId,
            @RequestParam(defaultValue = "0") int page, // 페이지 번호, 기본값은 0
            @RequestParam(defaultValue = "5") int size, // 페이지 크기, 기본값은 5
            @RequestParam int minPrice, // 최소 가격
            @RequestParam int maxPrice, // 최대 가격
            @UserId Long userId) {

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        // 서비스 호출
        Page<MenuDto> menusByCriteria = menuService.getAllMenusByCriteria2(title, tags, menuFolderId, userId, minPrice, maxPrice, pageable);

        // ApiResponse로 반환
        return ApiUtils.success(menusByCriteria.getContent());
    }


    // 특정 메뉴 조회
    @GetMapping("/{groupId}")
    public ApiResponse<MenuDetailDto> getMenu(@UserId Long userId, @PathVariable Long groupId) {
        MenuDetailDto certainMenu = menuService.getCertainMenu(userId, groupId);

        return ApiUtils.success(certainMenu);
    }

    // 부분 삭제용 특정 메뉴 아이디 조회
    @PostMapping("/{groupId}")
    public ApiResponse<MenuIdDto> getMenuId(@UserId Long userId, @PathVariable Long groupId, @RequestBody PostMenuIdRequest postMenuIdRequest) {
        Long menuFolderId = postMenuIdRequest.getMenuFolderId();
        MenuIdDto menuIdDto = menuService.getCertainMenuId(userId, menuFolderId, groupId);

        return ApiUtils.success(menuIdDto);
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
        menuService.createMenuImage(photoRequest, userId, photoRequest.getMenuGroupId());
        return ApiUtils.success("OK");
    }


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
    public ApiResponse<String> updateMenu (@PathVariable Long groupId, @UserId Long userId, @RequestBody PostMenuRequest postMenuRequest){
        menuService.updateMenu(groupId, userId, postMenuRequest);       // Hard Delete
        return ApiUtils.success("OK");  //OK 반환
    }




    @PatchMapping(value = "/{groupId}/photo",
            consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateMenuImages(@PathVariable Long groupId, @UserId Long userId, @ModelAttribute PostPhotoRequest photoRequest){
        menuService.createMenuImage(photoRequest, userId, groupId);

        return ApiUtils.success("OK");
    }



    @GetMapping("/place/{placeId}")
    public ApiResponse<List<PlaceMenuDTO>> findMenuByPlace(@PathVariable Long placeId, @UserId Long userId) {
        List<Menu> menus = menuService.findMenuByPlace(placeId, userId);

        int menuFolderCount = 0;


        List<PlaceMenuDTO> response = menus.stream().map(menu ->{
                List<PlaceMenuFolderDTO> menuFolders = menuService.getAllMenusByGroupIdAndUserId(menu.getGroupId(), userId).stream().map(m ->
                    PlaceMenuFolderDTO.builder()
                            .menuFolderTitle(m.getMenuList().getTitle())
                            .menuFolderIcon(m.getMenuList().getIconType())
                            .build()
                      ).collect(Collectors.toList());


                return PlaceMenuDTO.builder()
//                        .menuId(menu.getId())
                        .groupId(menu.getGroupId())
                        .menuTitle(menu.getTitle())
                        .menuPrice(menu.getPrice())
                        .menuIconType(menu.getMenuIconType())
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
                        .menuFolders(menuFolders)
                        .menuFolderCount(menuFolders.size() - 1)
                        .build();
        }).collect(Collectors.toList());

        return ApiUtils.success(response);
    }
}

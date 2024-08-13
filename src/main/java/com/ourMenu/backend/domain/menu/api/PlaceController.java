package com.ourMenu.backend.domain.menu.api;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.application.PlaceService;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.response.*;
import com.ourMenu.backend.domain.menu.exception.MenuNotFoundException;
import com.ourMenu.backend.domain.menu.exception.PlaceNotFoundException;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.exception.ErrorCode;
import com.ourMenu.backend.global.exception.ErrorResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class PlaceController {

    private final PlaceService placeService;
    private final MenuService menuService;

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<?> menuNotFoundException(MenuNotFoundException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.MENU_NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<?> placeNotFountException(PlaceNotFoundException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.PLACE_NOT_FOUND, e.getMessage()));
    }

    // "/map" 으로 변경
//    @GetMapping("/place")
//    public ApiResponse<List<GetPlaceResponse>> GetPlace(@UserId Long userId){
//        List<Place> placeList = placeService.findPlacesByUserId(userId);
//        List<GetPlaceResponse> getPlaceResponseList = placeList.stream().map(GetPlaceResponse::toDto).toList();
//        return ApiUtils.success(getPlaceResponseList);
//    }

    @GetMapping("")
    public ApiResponse<List<MenuPlaceDTO>> getPlaceMenu(@UserId Long userId){
        List<Menu> menuList = placeService.findMenuInPlaceByUserId(userId);
        List<MenuPlaceDTO> response = menuList.stream().map(menu ->
                MenuPlaceDTO.builder()
                        .menuTitle(menu.getTitle())
                        .groupId(menu.getGroupId())
                        .placeId(menu.getPlace().getId())
                        .menuIconType(menu.getMenuIconType())
                        .longitude(menu.getPlace().getLongitude())
                        .latitude(menu.getPlace().getLatitude())
                        .build()
                ).collect(Collectors.toList());
        return ApiUtils.success(response);
    }

    @GetMapping("/search")
    public ApiResponse<List<MenuSearchDTO>> getSearchResult(@RequestParam String title, @UserId Long userId){
        List<Menu> menuByTitle = placeService.findMenuByTitle(title, userId);
        List<MenuSearchDTO> response = menuByTitle.stream().map(menu ->
                MenuSearchDTO.builder()
                        .groupId(menu.getGroupId())
                        .menuTitle(menu.getTitle())
                        .placeTitle(menu.getPlace().getTitle())
                        .placeAddress(menu.getPlace().getAddress())
                        .build()
                ).collect(Collectors.toList());

        return ApiUtils.success(response);
    }

    @GetMapping("/search-history")
    public ApiResponse<List<MenuSearchDTO>> getSearchHistory(@UserId Long userId){
        List<Menu> searchHistory = placeService.findSearchHistory(userId);
        List<MenuSearchDTO> response = searchHistory.stream().map(menu ->
                MenuSearchDTO.builder()
                        .groupId(menu.getGroupId())
                        .menuTitle(menu.getTitle())
                        .placeTitle(menu.getPlace().getTitle())
                        .placeAddress(menu.getPlace().getAddress())
                        .build()
        ).collect(Collectors.toList());

        return ApiUtils.success(response);
    }

    @GetMapping("/{groupId}")
    public ApiResponse<MapMenuDTO> getMenuInfo(@PathVariable Long groupId, @UserId Long userId){
        List<Menu> menus = menuService.getAllMenusByGroupIdAndUserId(groupId, userId);

        // "기본 메뉴판"이 아닌 첫 번째 메뉴를 찾기
        Menu menu = menus.stream()
                .filter(m -> !"기본 메뉴판".equals(m.getTitle()))
                .findFirst()
                .orElse(menus.get(0)); // "기본 메뉴판"이 아닌 메뉴가 없으면 첫 번째 메뉴 사용

        int menuFolderCount = menus.size() - 1;

//        List<PlaceMenuFolderDTO> menuFolders = new ArrayList<>();
//        for (Menu menu : menus) {
//            menuFolders = menuService.getAllMenusByGroupIdAndUserId(menu.getGroupId(), userId).stream().map(m ->
//                    PlaceMenuFolderDTO.builder()
//                            .menuFolderTitle(m.getMenuList().getTitle())
//                            .menuFolderIcon(m.getMenuList().getIconType())
//                            .build()
//            ).collect(Collectors.toList());
//        }


//        Menu menu = placeService.findMenuByGroupIdAndUserId(groupId, userId);


        MapMenuDTO response = MapMenuDTO.builder()
                    .groupId(menu.getGroupId())
                    .menuTitle(menu.getTitle())
                    .menuPrice(menu.getPrice())
                    .menuIconType(menu.getMenuIconType())
                .placeTitle(menu.getPlace().getTitle())
                .latitude(menu.getPlace().getLatitude())
                .longitude(menu.getPlace().getLongitude())
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
                    .menuFolder(
                            MapMenuFolderDTO.builder()
                                    .menuFolderTitle(menu.getMenuList().getTitle())
                                    .menuFolderIcon(menu.getMenuList().getIconType())
                                    .menuFolderCount(menuFolderCount)
                                    .build()
                    ).build();

        return ApiUtils.success(response);
    }
}

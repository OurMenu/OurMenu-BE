package com.ourMenu.backend.domain.menu.api;

import com.ourMenu.backend.domain.menu.api.response.GetPlaceResponse;
import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.application.PlaceService;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.Place;
import com.ourMenu.backend.domain.menu.dto.response.MenuPlaceDTO;
import com.ourMenu.backend.domain.menu.dto.response.MenuSearchDTO;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class PlaceController {

    private final PlaceService placeService;
    private final MenuService menuService;

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
}

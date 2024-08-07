package com.ourMenu.backend.domain.menu.api;

import com.ourMenu.backend.domain.menu.api.response.GetPlaceResponse;
import com.ourMenu.backend.domain.menu.application.PlaceService;
import com.ourMenu.backend.domain.menu.domain.Place;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private PlaceService placeService;

    @GetMapping("/place")
    public ApiResponse<List<GetPlaceResponse>> GetPlace(@UserId Long userId){
        List<Place> placeList = placeService.findPlacesByUserId(userId);
        List<GetPlaceResponse> getPlaceResponseList = placeList.stream().map(GetPlaceResponse::toDto).toList();
        return ApiUtils.success(getPlaceResponseList);
    }
}

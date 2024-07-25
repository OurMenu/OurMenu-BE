package com.ourMenu.backend.domain.menu.api;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.request.PatchMenuRequest;
import com.ourMenu.backend.domain.menu.dto.response.PatchMenuResponse;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menu.dto.response.GetMenuResponse;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;

    /*
    메뉴 생성
     */
    @PostMapping("")
    public ApiResponse<PostMenuResponse> saveMenu(@RequestBody PostMenuRequest postMenuRequest) {
        Menu menu = new Menu();
        menu.setTitle(postMenuRequest.getTitle());
        menu.setPrice(postMenuRequest.getPrice());
        menu.setImgUrl(postMenuRequest.getImgUrl());
        menu.setMemo(postMenuRequest.getMemo());

        Menu savedMenu = menuService.createMenu(menu);
        PostMenuResponse postMenuResponse = new PostMenuResponse();
        postMenuResponse.setId(savedMenu.getId());

        return ApiUtils.success(postMenuResponse);
    }

    /*
    ID를 통한 메뉴 조회
     */
    @GetMapping("/{id}")
    public ApiResponse<GetMenuResponse> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id);

        GetMenuResponse response = new GetMenuResponse();
        response.setId(menu.getId());
        response.setTitle(menu.getTitle());
        response.setPrice(menu.getPrice());
        response.setImgUrl(menu.getImgUrl());
        response.setStatus(menu.getStatus());
        response.setCreatedAt(menu.getCreatedAt());
        response.setModifiedAt(menu.getModifiedAt());
        response.setMemo(menu.getMemo());

        return ApiUtils.success(response);
    }

    @GetMapping("")
    public ApiResponse<List<GetMenuResponse>> getAllMenu() {
        List<Menu> menuList = menuService.getAllMenus();
        List<GetMenuResponse> responseList = menuList.stream().map(menu -> {
            GetMenuResponse response = new GetMenuResponse();
            response.setId(menu.getId());
            response.setTitle(menu.getTitle());
            response.setPrice(menu.getPrice());
            response.setImgUrl(menu.getImgUrl());
            response.setMemo(menu.getMemo());
            response.setStatus(menu.getStatus());
            response.setCreatedAt(menu.getCreatedAt());
            response.setModifiedAt(menu.getModifiedAt());
            return response;
        }).collect(Collectors.toList());

        return ApiUtils.success(responseList);
    }

    /*
    메뉴 삭제
     */
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

    /*
    메뉴 업데이트
     */
    @PatchMapping("/{id}")
    public ApiResponse<PatchMenuResponse> updateMenu(@PathVariable Long id, @RequestBody PatchMenuRequest patchMenuRequest){
        Menu updatedMenu = menuService.updateMenu(id, patchMenuRequest);

        PatchMenuResponse response = new PatchMenuResponse();
        response.setId(updatedMenu.getId());
        response.setTitle(updatedMenu.getTitle());
        response.setImgUrl(updatedMenu.getImgUrl());
        response.setPrice(updatedMenu.getPrice());
        response.setMemo(updatedMenu.getMemo());
        response.setCreatedAt(updatedMenu.getCreatedAt());
        response.setModifiedAt(updatedMenu.getModifiedAt());
        response.setStatus(updatedMenu.getStatus());


        return ApiUtils.success(response);
    }

}

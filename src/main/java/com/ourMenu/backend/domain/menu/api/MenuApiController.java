package com.ourMenu.backend.domain.menu.api;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.request.PatchMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menu.dto.response.MenuDto;
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

    @PostMapping("")
    public ApiResponse<PostMenuResponse> saveMenu(@RequestBody PostMenuRequest postMenuRequest) {
        Menu menu = Menu.builder()  // 빌더 패턴 사용
                .title(postMenuRequest.getTitle())
                .price(postMenuRequest.getPrice())
                .imgUrl(postMenuRequest.getImgUrl())
                .memo(postMenuRequest.getMemo())
                .build();

        return ApiUtils.success(postMenuResponse);
    }

     */

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

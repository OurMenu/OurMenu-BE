package com.ourMenu.backend.domain.menu.api;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.domain.*;
import com.ourMenu.backend.domain.menu.dto.request.PatchMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menu.dto.response.MenuDto;
import com.ourMenu.backend.domain.menulist.dao.MenuListRepository;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Slf4j
public class MenuApiController {

    private final MenuService menuService;

    private final MenuListRepository menuListRepository;


    // 메뉴 생성
    @PostMapping("")
    public ApiResponse<PostMenuResponse> saveMenu(@RequestBody PostMenuRequest postMenuRequest) {
        MenuList findMenuList = menuListRepository.findByTitle(postMenuRequest.getMenuListTitle())
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));

        // 메뉴 생성
        Menu menu = Menu.builder()  // 빌더 패턴 사용
                .title(postMenuRequest.getTitle())
                .price(postMenuRequest.getPrice())
                .memo(postMenuRequest.getMemo())
                .createdAt(LocalDateTime.now()) // 생성일시 설정
                .modifiedAt(LocalDateTime.now()) // 수정일시 설정
                .build();

        // 식당 생성 -> 위도, 경도 정보는 어떻게 처리하는지
        Place place = Place.builder()
                .title(postMenuRequest.getStoreInfo().getStoreName())
                .address(postMenuRequest.getStoreInfo().getStoreAddress())
                .info(postMenuRequest.getStoreInfo().getStoreInfo())
                .createdAt(LocalDateTime.now()) // 생성일시 설정
                .modifiedAt(LocalDateTime.now()) // 수정일시 설정
                .build();

        // 연관관계 설정
        menu.confirmMenuList(findMenuList);
        menu.confirmPlace(place);


        /* 메뉴 태그 생성 */
        List<MenuTag> menuTags = postMenuRequest.getTagInfo().stream()
                .map(tagInfo -> {
                    // Tag 생성
                    Tag tag = Tag.builder()
                            .name(tagInfo.getTagTitle())
                            .isCustom(tagInfo.getIsCustom())
                            .build();

                    // MenuTag 생성
                    MenuTag menuTag = MenuTag.builder()
                            .tag(tag)
                            .menu(menu)
                            .build();

                    // 연관관계 설정
                    menuTag.confirmTag(tag);
                    menuTag.confirmMenu(menu);

                    return menuTag; // MenuTag 객체를 반환
                })
                .collect(Collectors.toList());



        List<MenuImage> menuImages = postMenuRequest.getImageUrls().stream()
                .map(url -> MenuImage.builder()
                        .url(url)
                        .menu(menu)
                        .build()) // MenuImage 생성
                .collect(Collectors.toList());

        for (MenuImage menuImage : menuImages) {
            menuImage.confirmMenu(menu);
        }

        Menu saveMenu = menuService.createMenu(menu);

        PostMenuResponse postMenuResponse = new PostMenuResponse(saveMenu.getId());
        return ApiUtils.success(postMenuResponse);
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

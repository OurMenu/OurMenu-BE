package com.ourMenu.backend.domain.menulist.api;

import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menulist.dto.request.MenuListRequestDTO;
import com.ourMenu.backend.domain.menulist.dto.response.GetMenuListResponse;
import com.ourMenu.backend.domain.menulist.dto.response.MenuListResponseDTO;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menuFolder")
public class MenuListApiController {

    private final MenuListService menuListService;

    //메뉴판 등록
    @PostMapping("")
    public ApiResponse<MenuListResponseDTO> createMenuList(@RequestBody MenuListRequestDTO request){
        MenuList menuList = menuListService.createMenuList(request);
        MenuListResponseDTO response = MenuListResponseDTO.builder()
                .id(menuList.getId())
                .title(request.getTitle())
                .imgUrl(request.getImg())
                .iconType(request.getIconType())
                .build();

        return ApiUtils.success(response);

    }


    //메뉴판 전체 조회
    @GetMapping("")
    public ApiResponse<List<GetMenuListResponse>> findAllMenuList(){
        List<MenuList> menuLists = menuListService.getAllMenuList();
        List<GetMenuListResponse> responses = menuLists.stream().map(menuList ->
                GetMenuListResponse.builder()
                        .title(menuList.getTitle())
                        .menuCount((long) menuList.getMenus().size())
                        .imgUrl(menuList.getImgUrl())
                        .iconType(menuList.getIconType())
                        .build()
        ).collect(Collectors.toList());

        return ApiUtils.success(responses);
    }

    //메뉴판
    @PatchMapping("/{id}")
    public ApiResponse<MenuListResponseDTO> updateMenuList(@PathVariable Long id, @RequestBody MenuListRequestDTO request){
        MenuList menuList = menuListService.updateMenuList(id, request);
        MenuListResponseDTO response = MenuListResponseDTO.builder()
                .id(menuList.getId())
                .title(menuList.getTitle())
                .imgUrl(menuList.getImgUrl())
                .iconType(menuList.getIconType())
                .build();

        return ApiUtils.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> removeMenuList(@PathVariable Long id){
        String response = menuListService.removeMenuList(id);       //STATUS를 DELETED로 변환
        return ApiUtils.success(response);  //OK 반환
    }

    /*
    메뉴판 등록

    @PostMapping("")
    public ApiResponse<PostMenuListResponse> saveMenuList(@RequestBody PostMenuListRequest postMenuListRequest) {
        MenuList menuList = MenuList.builder()
                .title(postMenuListRequest.getTitle())
                .imgUrl(postMenuListRequest.getImgUrl())
                .build();

        MenuList savedMenuList = menuListService.createMenuList(menuList);

        PostMenuListResponse response = PostMenuListResponse.builder()
                .id(savedMenuList.getId())
                .build();
        return ApiUtils.success(response);
    }


    메뉴판 전체 조회

    @GetMapping("")
    public ApiResponse<List<MenuListDto>> findAllMenuList(){
        List<MenuList> menuLists = menuListService.getAllMenuLists();
        List<MenuListDto> responseList = menuLists.stream().map(menuList -> {
            return getMenuListDto(menuList);
        }).collect(Collectors.toList());

        return ApiUtils.success(responseList);
    }


    메뉴판 단건 조회

    @GetMapping("/{id}")
    public ApiResponse<MenuListDto> findMenuListById(@PathVariable Long id){
        MenuList menuList = menuListService.getMenuListById(id);
        MenuListDto response = getMenuListDto(menuList);
        return ApiUtils.success(response);
    }


    메뉴판 수정(제목, 사진)

    @PatchMapping("/{id}")
    public ApiResponse<MenuListDto> updateMenuList(@PathVariable Long id, @RequestBody PatchMenuListRequest patchMenuListRequest){
        MenuList menuList = menuListService.updateMenuList(id, patchMenuListRequest);
        MenuListDto response = getMenuListDto(menuList);
        return ApiUtils.success(response);
    }


    메뉴판 삭제

    @DeleteMapping("/{id}")
    public ApiResponse<String> removeMenuList(@PathVariable Long id){
        menuListService.deleteMenuList(id);
        return ApiUtils.success("OK");
    }


    메뉴판 메뉴 추가

    @PatchMapping("")
    public ApiResponse<MenuListDto> addMenuInMenuList(@RequestParam Long menuId, @RequestParam Long menuListId){
        MenuList menuList = menuListService.addMenu(menuId, menuListId);
        MenuListDto response = getMenuListDto(menuList);
        return ApiUtils.success(response);
    }


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






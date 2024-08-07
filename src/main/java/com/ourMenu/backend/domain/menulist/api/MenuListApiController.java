package com.ourMenu.backend.domain.menulist.api;

import com.ourMenu.backend.domain.menulist.exception.ImageLoadException;
import com.ourMenu.backend.domain.menulist.exception.MenuListException;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menulist.dto.request.MenuListRequestDTO;
import com.ourMenu.backend.domain.menulist.dto.response.GetMenuListResponse;
import com.ourMenu.backend.domain.menulist.dto.response.MenuListResponseDTO;
import com.ourMenu.backend.domain.user.application.UserService;
import com.ourMenu.backend.domain.user.exception.UserException;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.exception.ErrorCode;
import com.ourMenu.backend.global.exception.ErrorResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menuFolder")
public class MenuListApiController {

    private final MenuListService menuListService;
    private final UserService userService;

    @ExceptionHandler(MenuListException.class)
    public ResponseEntity<?> menuListException(MenuListException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.MENU_LIST_NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> userException(UserException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.USER_NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(ImageLoadException.class)
    public ResponseEntity<?> imageLoadException(ImageLoadException e){
        return ApiUtils.error(ErrorResponse.of(ErrorCode.IMAGE_NOT_LOADED_ERROR, e.getMessage()));
    }

    //메뉴판 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<MenuListResponseDTO> createMenuList(@ModelAttribute MenuListRequestDTO request, @UserId Long userId){
        MenuList menuList = menuListService.createMenuList(request, userId);
        MenuListResponseDTO response = MenuListResponseDTO.builder()
                .id(menuList.getId())
                .title(menuList.getTitle())
                .imgUrl(menuList.getImgUrl())
                .iconType(menuList.getIconType())
                .priority(menuList.getPriority())
                .build();

        return ApiUtils.success(response);

    }


    //메뉴판 전체 조회
    @GetMapping("")
    public ApiResponse<List<GetMenuListResponse>> findAllMenuList(@UserId Long userId){
        List<MenuList> menuLists = menuListService.getAllMenuList(userId);
        List<GetMenuListResponse> responses = menuLists.stream().map(menuList ->
                GetMenuListResponse.builder()
                        .title(menuList.getTitle())
                        .menuCount((long) menuList.getMenus().size())
                        .imgUrl(menuList.getImgUrl())
                        .iconType(menuList.getIconType())
                        .priority(menuList.getPriority())
                        .build()
        ).collect(Collectors.toList());

        return ApiUtils.success(responses);
    }

    //메뉴판
    @PatchMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<MenuListResponseDTO> updateMenuList(@PathVariable Long id, @UserId Long userId, @ModelAttribute MenuListRequestDTO request){
        MenuList menuList = menuListService.updateMenuList(id, request,  userId);
        MenuListResponseDTO response = MenuListResponseDTO.builder()
                .id(menuList.getId())
                .title(menuList.getTitle())
                .imgUrl(menuList.getImgUrl())
                .iconType(menuList.getIconType())
                .priority(menuList.getPriority())
                .build();

        return ApiUtils.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> removeMenuList(@PathVariable Long id, @UserId Long userId){
        String response = menuListService.removeMenuList(id, userId);       //STATUS를 DELETED로 변환
        return ApiUtils.success(response);  //OK 반환
    }

    @PatchMapping("/priority/{id}")
    public ApiResponse<String> changePriority(@PathVariable Long id, @RequestParam Long newPriority, @UserId Long userId){
        String response = menuListService.setPriority(id, newPriority ,userId);
        return ApiUtils.success(response);
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






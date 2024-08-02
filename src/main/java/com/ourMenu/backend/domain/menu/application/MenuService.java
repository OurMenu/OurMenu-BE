package com.ourMenu.backend.domain.menu.application;

import com.ourMenu.backend.domain.menu.domain.*;
import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.dto.request.PatchMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.StoreRequestDTO;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.global.common.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuListService menuListService;
    private final PlaceService placeService;
    private final TagService tagService;

    // 모두 조회
    @Transactional
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    /*
    @Transactional
    // 메뉴 등록 * (이미지 제외)
    public PostMenuResponse createMenu(PostMenuRequest postMenuRequest) {
        MenuList findMenuList = menuListService.getMenuListByName(postMenuRequest.getMenuListTitle());

        Place place = placeService.createPlace(postMenuRequest.getStoreInfo());

        Menu menu = Menu.builder()
                .title(postMenuRequest.getTitle())
                .price(postMenuRequest.getPrice())
                .memo(postMenuRequest.getMemo())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        menu.confirmMenuList(findMenuList);
        menu.confirmPlace(place);

        List<MenuTag> menuTags = postMenuRequest.getTagInfo().stream()
                .map(tagInfo -> {
                    Tag tag = tagService.createTag(tagInfo);
                    MenuTag menuTag = MenuTag.builder()
                            .tag(tag)
                            .menu(menu)
                            .build();

                    menuTag.confirmTag(tag);
                    menuTag.confirmMenu(menu);

                    return menuTag;
                })
                .collect(Collectors.toList());


        Menu savedMenu = menuRepository.save(menu);
        placeService.save(place);

        return new PostMenuResponse(savedMenu.getId());
    }
*/
    // 메뉴 추가
    public Menu getMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));
    }

    // 메뉴 이미지 등록
    @Transactional
    public void saveMenuImage(Long menuId, List<String> imageUrls) {
            Menu findMenu = getMenuById(menuId);

            List<MenuImage> menuImages = imageUrls.stream()
                    .map(url -> MenuImage.builder()
                            .url(url)
                            .menu(findMenu)
                            .build())
                    .collect(Collectors.toList());

            for (MenuImage menuImage : menuImages) {
                menuImage.confirmMenu(findMenu);
            }
    }


    @Transactional
    public void updateMenu(Long menuId, PostMenuRequest postMenuRequest) {
        // 메뉴 조회

    }

    @Transactional
    public String removeMenu(Long id){
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));

        Place place = menu.getPlace();
        String placeName = place.getTitle(); // 프록시 초기화

        MenuList menuList = menu.getMenuList();
        String title = menuList.getTitle();

        menu.removeMenuList(menuList);
        menu.removePlace(place);


        menuRepository.delete(menu);

        return "OK";
    }

    // 메뉴 업데이트
    /*
    @Transactional
    public Menu updateMenu(Long id, Menu menuDetails) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));
        if (menu != null) {
            menu.setTitle(menuDetails.getTitle());
            menu.setPrice(menuDetails.getPrice());
            menu.setImgUrl(menuDetails.getImgUrl());
            menu.setModifiedAt(menuDetails.getModifiedAt());
            menu.setStatus(menuDetails.getStatus());
            menu.setMemo(menuDetails.getMemo());
            return menuRepository.save(menu);
        } else {
            return null;
        }
    }


    @Transactional
    public Menu updateMenu(Long id, PatchMenuRequest patchMenuRequest) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        if (patchMenuRequest.getTitle() != null) {
            menu.setTitle(patchMenuRequest.getTitle());
        }
        if (patchMenuRequest.getImgUrl() != null) {
            menu.setImgUrl(patchMenuRequest.getImgUrl());
        }
        if (patchMenuRequest.getPrice() != 0) { // 가격이 0이 아닌 경우에만 업데이트
            menu.setPrice(patchMenuRequest.getPrice());
        }
        if (patchMenuRequest.getMemo() != null) {
            menu.setMemo(patchMenuRequest.getMemo());
        }

        return menuRepository.save(menu);
    }

    // 메뉴 삭제 *
    @Transactional
    public Menu deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));
        if (menu != null) {
            menu.setStatus(MenuStatus.DELETED); // 상태를 'DELETED'로 변경
            return menuRepository.save(menu); //  상태를 저장
        } else {
            return null;
        }
    }
    */
}

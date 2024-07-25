package com.ourMenu.backend.domain.menulist.application;

import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.MenuMenuList;
import com.ourMenu.backend.domain.menulist.dao.MenuListRepository;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menulist.domain.MenuListStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ourMenu.backend.domain.menu.domain.MenuMenuList.createMenuMenuList;

@Service
@RequiredArgsConstructor
public class MenuListService {

    private final MenuListRepository menuListRepository;
    private final MenuRepository menuRepository;

    /** 새 메뉴판 생성 */
    @Transactional
    public MenuList createMenuList(MenuList menuList) {
        return menuListRepository.save(menuList);
    }

    /** 메뉴판 메뉴 추가 */
    @Transactional
    public MenuList addMenu(Long menuId, Long menuListId ) {
        MenuList menuList = menuListRepository.findById(menuListId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));

        MenuMenuList menuMenuList = createMenuMenuList(menu);
        menuList.addMenuMenuList(menuMenuList);

        return menuListRepository.save(menuList);
    }

    /** 메뉴판 메뉴 삭제 */
    @Transactional
    public MenuList deleteMenu(Long menuId, Long menuListId ) {

        MenuList menuList = menuListRepository.findById(menuListId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));


        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));

        // 삭제할 메뉴와 관련된 중간테이블 찾기
        MenuMenuList menuMenuList = menuList.getMenuMenuLists().stream()
                .filter(m -> m.getMenu().getId().equals(menuId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 메뉴가 메뉴판에 없습니다."));

        // 메뉴판에서 삭제
        menuList.removeMenuMenuList(menuMenuList);

        return menuListRepository.save(menuList);
    }


    // 모든 메뉴판 조회
    @Transactional
    public List<MenuList> getAllMenuLists() {
        return menuListRepository.findAll();
    }

    // 특정 메뉴판 조회
    @Transactional
    public MenuList getMenuListById(Long id) {
        return menuListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));
    }


    // 메뉴판 업데이트
    @Transactional
    public MenuList updateMenuList(Long id, MenuList menuListDetails) {
        MenuList menuList = menuListRepository.findById(id).orElse(null);
        if (menuList != null) {
            menuList.setTitle(menuListDetails.getTitle());
            menuList.setModifiedAt(menuListDetails.getModifiedAt());
            menuList.setStatus(menuListDetails.getStatus());
            menuList.setImgUrl(menuListDetails.getImgUrl());

            return menuListRepository.save(menuList);
        } else {
            return null;
        }
    }

    // 메뉴판 삭제
    @Transactional
    public MenuList deleteMenuList(Long id) {
        MenuList menuList = menuListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));
        if (menuList != null) {
            menuList.setStatus(MenuListStatus.DELETED); // 상태를 'DELETED'로 변경
            return menuListRepository.save(menuList); //  상태를 저장
        } else {
            return null;
        }
    }

}

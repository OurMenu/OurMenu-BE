package com.ourMenu.backend.menulist.application;

import com.ourMenu.backend.menu.dao.MenuRepository;
import com.ourMenu.backend.menu.domain.Menu;
import com.ourMenu.backend.menu.domain.MenuMenuList;
import com.ourMenu.backend.menulist.MenuList;
import com.ourMenu.backend.menulist.dao.MenuListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ourMenu.backend.menu.domain.MenuMenuList.createMenuMenuList;

@Service
@RequiredArgsConstructor
public class MenuListService {

    private final MenuListRepository menuListRepository;
    private final MenuRepository menuRepository;

    /** 새 메뉴판 생성 */
    public MenuList createMenuList(MenuList menuList) {
        return menuListRepository.save(menuList);
    }

    /** 메뉴판 메뉴 추가 */
    public MenuList addMenu(Long menuId, Long menuListId ) {
        MenuList menuList = menuListRepository.findById(menuListId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴판이 없습니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));

        MenuMenuList menuMenuList = createMenuMenuList(menu);
        menuList.addMenuMenuList(menuMenuList);

        return menuListRepository.save(menuList);
    }

    public List<MenuList> getAllMenuLists() {
        return menuListRepository.findAll();
    }

    public MenuList getMenuListById(Long id) {
        return menuListRepository.findById(id).orElse(null);
    }



    public MenuList updateMenuList(Long id, MenuList menuListDetails) {
        MenuList menuList = menuListRepository.findById(id).orElse(null);
        if (menuList != null) {
            menuList.setTitle(menuListDetails.getTitle());
            menuList.setModifiedAt(menuListDetails.getModifiedAt());
            menuList.setStatus(menuListDetails.getStatus());
            menuList.setImage(menuListDetails.getImage());

            return menuListRepository.save(menuList);
        } else {
            return null;
        }
    }

    public void deleteMenuList(Long id) {
        menuListRepository.deleteById(id);
    }

}

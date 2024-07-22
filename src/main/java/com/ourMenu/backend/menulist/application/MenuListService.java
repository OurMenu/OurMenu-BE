package com.ourMenu.backend.menulist.application;

import com.ourMenu.backend.menulist.MenuList;
import com.ourMenu.backend.menulist.dao.MenuListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuListService {

    @Autowired
    private MenuListRepository menuListRepository;

    public List<MenuList> getAllMenuLists() {
        return menuListRepository.findAll();
    }

    public MenuList getMenuListById(Long id) {
        return menuListRepository.findById(id).orElse(null);
    }

    public MenuList createMenuList(MenuList menuList) {
        return menuListRepository.save(menuList);
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

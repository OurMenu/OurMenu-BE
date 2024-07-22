package com.ourMenu.backend.menu.application;

import com.ourMenu.backend.menu.dao.MenuRepository;
import com.ourMenu.backend.menu.domain.Menu;
import com.ourMenu.backend.menu.domain.MenuStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    // 모두 조회
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    // 단권 조회 * 예외처리 추가해야함
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    // 메뉴 추가
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    // 메뉴 업데이트
    public Menu updateMenu(Long id, Menu menuDetails) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu != null) {
            menu.setTitle(menuDetails.getTitle());
            menu.setPrice(menuDetails.getPrice());
            menu.setImage(menuDetails.getImage());
            menu.setModifiedAt(menuDetails.getModifiedAt());
            menu.setStatus(menuDetails.getStatus());
            menu.setMemo(menuDetails.getMemo());
            return menuRepository.save(menu);
        } else {
            return null;
        }
    }

    // 메뉴 삭제 * 예외처리 추가해야함
    public Menu deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu != null) {
            menu.setStatus(MenuStatus.DELETED); // 상태를 'DELETED'로 변경
            return menuRepository.save(menu); //  상태를 저장
        } else {
            return null;
        }
    }

}

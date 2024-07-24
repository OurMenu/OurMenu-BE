package com.ourMenu.backend.domain.menu.application;

import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.domain.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    // 모두 조회
    @Transactional
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    // 단권 조회 *
    @Transactional
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴가 없습니다."));
    }

    // 메뉴 추가
    @Transactional
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    // 메뉴 업데이트
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

}

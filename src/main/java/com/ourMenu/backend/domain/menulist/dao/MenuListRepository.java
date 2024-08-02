package com.ourMenu.backend.domain.menulist.dao;

import com.ourMenu.backend.domain.menulist.domain.MenuList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuListRepository extends JpaRepository<MenuList, Long> {
}

package com.ourMenu.backend.menulist.dao;

import com.ourMenu.backend.menulist.MenuList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuListRepository extends JpaRepository<MenuList, Long> {
}

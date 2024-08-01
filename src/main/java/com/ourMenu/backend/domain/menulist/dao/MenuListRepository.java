package com.ourMenu.backend.domain.menulist.dao;

import com.ourMenu.backend.domain.menulist.domain.MenuList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuListRepository extends JpaRepository<MenuList, Long> {
    Optional<MenuList> findByTitle(String title);
}

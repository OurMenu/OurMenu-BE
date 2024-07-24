package com.ourMenu.backend.menu.dao;

import com.ourMenu.backend.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}

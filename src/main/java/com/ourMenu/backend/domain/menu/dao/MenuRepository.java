package com.ourMenu.backend.domain.menu.dao;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m WHERE m.place.id = :placeId AND m.status IN :status")
    List<Menu> findMenuByPlaceId(@Param("placeId") Long placeId, @Param("status")List<MenuStatus> statuses);
}

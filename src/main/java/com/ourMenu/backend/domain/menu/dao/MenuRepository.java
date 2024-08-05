package com.ourMenu.backend.domain.menu.dao;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m WHERE m.place.id = :placeId AND m.status IN :status")
    Optional<List<Menu>> findMenuByPlaceId(@Param("placeId") Long placeId, @Param("status")List<MenuStatus> statuses);

    Optional<Menu> findByIdAndUserId(Long menuId, Long userId);

    @Query("SELECT m FROM Menu m " +
            "JOIN FETCH m.images mi where m.id = :menuId")
    Optional<Menu> findMenuAndImages(@Param("menuId") Long menuId);

    @Query("SELECT m FROM Menu m " +
            "JOIN FETCH m.user u " +
            "JOIN FETCH m.menuList ml " +
            "JOIN FETCH m.place p " +
            "WHERE m.id = :menuId")
    Optional<Menu> findAllWithUserAndMenuListAndPlace(@Param("menuId") Long menuId);



}

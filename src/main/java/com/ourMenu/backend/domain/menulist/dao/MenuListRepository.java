package com.ourMenu.backend.domain.menulist.dao;

import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menulist.domain.MenuListStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuListRepository extends JpaRepository<MenuList, Long> {
    Optional<MenuList> findByTitle(String title);

    @Query("SELECT m FROM MenuList m WHERE m.status IN :status")
    List<MenuList> findAllMenuList(@Param("status")List<MenuListStatus> status);

    @Query("SELECT m FROM MenuList m WHERE m.title = :title AND m.status IN (:status)")
    MenuList findMenuListByTitle(@Param("title") String title, @Param("status")List<MenuListStatus> status);
}

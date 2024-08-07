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

    @Query("SELECT m FROM Menu m " +
            "JOIN FETCH m.place p " +         // 메뉴와 식당 조인
            "JOIN FETCH m.images mi ")        // 메뉴와 메뉴 이미지 조인)           // 특정 메뉴 ID로 필터링
     List<Menu> findMenuWithPlaceAndImages();

    @Query("SELECT DISTINCT m FROM Menu m " +
            "JOIN FETCH m.place p " +         // 메뉴와 식당 조인
            "LEFT JOIN FETCH m.images mi " +  // 메뉴와 메뉴 이미지의 LEFT JOIN
            "JOIN m.tags mt " +               // 중간 테이블을 통해 메뉴 태그 조인
            "JOIN mt.tag t " +                // 태그 엔티티 조인
            "WHERE m.user.id = :userId " +    // 유저 아이디 필터
            "AND (:title IS NULL OR m.title LIKE %:title%) " + // 제목 필터
            "AND (:tag IS NULL OR t.name LIKE %:tag%) " +       // 태그 필터
            "AND (:menuFolderId IS NULL OR m.menuList.id = :menuFolderId)") // 메뉴판 필터
    List<Menu> findMenusByCriteria(@Param("title") String menuTitle,
                                   @Param("tag") String menuTag,
                                   @Param("menuFolderId") Integer menuFolderId,
                                   @Param("userId") Long userId);

    boolean existsByPlaceIdAndMenuListIdAndTitle(Long placeId, Long menuListId, String title);
}

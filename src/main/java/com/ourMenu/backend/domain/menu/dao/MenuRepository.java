package com.ourMenu.backend.domain.menu.dao;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByUserId(Long userId);

    List<Menu> findByUserIdAndGroupId(Long userId, Long groupId);

    @Query("SELECT m FROM Menu m JOIN FETCH m.user u JOIN FETCH m.place p JOIN FETCH m.menuList ml WHERE m.groupId = :groupId")
    List<Menu> findByGroupIdWithFetch(@Param("groupId") Long groupId);

    @Query("SELECT m FROM Menu m WHERE m.place.id = :placeId AND m.status IN :status")
    Optional<List<Menu>> findMenuByPlaceId(@Param("placeId") Long placeId, @Param("status")List<MenuStatus> statuses);

    Optional<Menu> findByIdAndUserId(Long menuId, Long userId);

    @Query("SELECT DISTINCT m FROM Menu m " +
            "JOIN FETCH m.place p " +
            "LEFT JOIN FETCH m.images mi " +
            "JOIN m.tags mt " +
            "JOIN mt.tag t " +
            "WHERE m.user.id = :userId " +
            "AND m.groupId = :groupId")
    List<Menu> findCertainMenuByUserIdAndGroupId(@Param("userId") Long userId,
                                                     @Param("groupId") Long groupId);


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



//    @Query("SELECT DISTINCT m FROM Menu m " +
//            "JOIN FETCH m.place p " +
//            "LEFT JOIN FETCH m.images mi " +
//            "JOIN m.tags mt " +
//            "JOIN mt.tag t " +
//            "WHERE m.user.id = :userId " +
//            "AND (:title IS NULL OR m.title LIKE %:title%) " +
//            "AND (:tag IS NULL OR t.name LIKE %:tag%) " +
//            "AND (:menuFolderId IS NULL OR m.menuList.id = :menuFolderId)") // groupId 필터 제거
//    List<Menu> findingMenusByCriteria(@Param("title") String title,
//                                   @Param("tag") String tag,
//                                   @Param("menuFolderId") Integer menuFolderId,
//                                   @Param("userId") Long userId);

    @Query("SELECT m FROM Menu m " +
            "JOIN m.tags mt " +
            "JOIN mt.tag t " +
            "WHERE m.user.id = :userId " +
            "AND (:title IS NULL OR m.title LIKE %:title%) " +
            "AND (:menuFolderId IS NULL OR m.menuList.id = :menuFolderId) " +
            "AND (:minPrice IS NULL OR m.price >= :minPrice) " + // 최소 가격 조건
            "AND (:maxPrice IS NULL OR m.price <= :maxPrice) " + // 최대 가격 조건
            "AND (:tags IS NULL OR t.name IN :tags) " + // 태그 조건
            "GROUP BY m.id " +
            "HAVING (:tagCount IS NULL OR COUNT(DISTINCT t.name) = :tagCount) " + // tagCount가 null인 경우 조건 무시
            "ORDER BY m.groupId ASC") // groupId를 기준으로 오름차순 정렬
    Page<Menu> findingMenusByCriteria2(@Param("title") String title,
                                       @Param("tags") String[] tags, // 태그 배열
                                       @Param("tagCount") Integer tagCount, // 태그 개수
                                       @Param("menuFolderId") Integer menuFolderId,
                                       @Param("userId") Long userId,
                                       @Param("minPrice") int minPrice,
                                       @Param("maxPrice") int maxPrice,
                                       Pageable pageable);







    @Query("SELECT m FROM Menu m WHERE m.title LIKE %:title% AND m.user.id = :userId")
    List<Menu> findMenusByTitleContainingAndUserId(@Param("title") String title, @Param("userId") Long userId);

    @Query("SELECT m FROM Menu m WHERE m.id = :menuId AND m.user.id = :userId")
    Optional<Menu> findMenuByUserId(@Param("menuId") Long menuId, @Param("userId") Long userId);

    boolean existsByPlaceIdAndMenuListIdAndTitle(Long placeId, Long menuListId, String title);
}

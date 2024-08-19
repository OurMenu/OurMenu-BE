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

    Optional<Menu> findByUserIdAndMenuListIdAndGroupId(Long userId, Long menuFolderId, Long groupId);


    List<Menu> findByUserId(Long userId);

    List<Menu> findByUserIdAndGroupId(Long userId, Long groupId);

    @Query("SELECT m FROM Menu m JOIN FETCH m.user u JOIN FETCH m.place p JOIN FETCH m.menuList ml WHERE m.groupId = :groupId")
    List<Menu> findByGroupIdWithFetch(@Param("groupId") Long groupId);

    @Query("SELECT m FROM Menu m WHERE m.id IN (SELECT MAX(m2.id) FROM Menu m2 WHERE m2.place.id = :placeId AND m2.user.id = :userId AND m2.status IN :status GROUP BY m2.groupId)")
    Optional<List<Menu>> findMenuByPlaceIdAndUserId(@Param("placeId") Long placeId, @Param("userId") Long userId, @Param("status")List<MenuStatus> statuses);

    @Query("SELECT m FROM Menu m WHERE m.user.id = :userId AND m.place.id = :placeId AND m.createdAt = (" +
            "SELECT MAX(subM.createdAt) FROM Menu subM WHERE subM.place.id = m.place.id AND subM.user.id = :userId" +
            ") ORDER BY m.createdAt DESC")
    Optional<Menu> findDistinctByUserIdOrderByCreatedAtDesc(@Param("placeId") Long placeId, @Param("userId") Long userId);
    @Query("SELECT m FROM Menu m WHERE m.place.id = :placeId AND m.status IN :status")
    Optional<List<Menu>> findMenuByPlaceId(@Param("placeId") Long placeId, @Param("status") List<MenuStatus> statuses);

    Optional<Menu> findByIdAndUserId(Long menuId, Long userId);

//    @Query("SELECT m FROM Menu m WHERE m.id IN (" +
//            "(SELECT MIN(m2.id) FROM Menu m2 WHERE m2.user.id = :userId " +
//            "AND m2.groupId = :groupId " + // groupId를 조건에 추가
//            "GROUP BY m2.groupId))")
//    Optional<List<Menu>> findCertainMenuByUserIdAndGroupId(@Param("userId") Long userId,
//                                                 @Param("groupId") Long groupId);


    @Query("SELECT DISTINCT m FROM Menu m " +
            "JOIN FETCH m.place p " +
            "LEFT JOIN FETCH m.images mi " +
            "LEFT JOIN m.tags mt " +
            "LEFT JOIN mt.tag t " +
            "WHERE m.user.id = :userId " +
            "AND m.groupId = :groupId")
    List<Menu> findCertainMenuByUserIdAndGroupId(@Param("userId") Long userId,@Param("groupId") Long groupId);


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
            "JOIN FETCH m.images mi ")
        // 메뉴와 메뉴 이미지 조인)           // 특정 메뉴 ID로 필터링
    List<Menu> findMenuWithPlaceAndImages();

    @Query("SELECT m FROM Menu m WHERE m.id IN (" +
            "SELECT MIN(m2.id) FROM Menu m2 " +
            "JOIN m2.place p " +
            "LEFT JOIN m2.images mi " +
            "LEFT JOIN m2.tags mt " +
            "LEFT JOIN mt.tag t " +
            "WHERE m2.user.id = :userId " +
            "AND (:tags IS NULL OR (t.name IN :tags)) " +
            "AND (:menuFolderId IS NULL OR m2.menuList.id = :menuFolderId) " +
            "AND (:minPrice IS NULL OR m2.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR m2.price <= :maxPrice) " +
            "GROUP BY m2.groupId " +
            "HAVING COUNT(DISTINCT t.name) >= :tagCount" +
            ")")

    Page<Menu> findingMenusByCriteria2(
            @Param("tags") String[] tags, // 태그 배열로 변경
            @Param("menuFolderId") Integer menuFolderId,
            @Param("userId") Long userId,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("tagCount") Integer tagCount, // 태그 개수 추가
            Pageable pageable);






    @Query("SELECT m FROM Menu m WHERE m.title LIKE %:title% AND m.user.id = :userId")
    List<Menu> findMenusByTitleContainingAndUserId(@Param("title") String title, @Param("userId") Long userId);

    List<Menu> findMenusByTitleContainingAndUserIdNot(@Param("title") String title, @Param("userId") Long userId);

    @Query("SELECT m FROM Menu m WHERE m.id = :menuId AND m.user.id = :userId")
    Optional<Menu> findMenuByUserId(@Param("menuId") Long menuId, @Param("userId") Long userId);

    boolean existsByPlaceIdAndMenuListIdAndTitle(Long placeId, Long menuListId, String title);

//    @Query("SELECT m FROM Menu m WHERE m.id IN (SELECT MIN(m2.id) FROM Menu m2 WHERE m2.title LIKE %:title% AND m2.user.id = :userId GROUP BY m2.groupId)")
//    Optional<List<Menu>> findMenuByTitle(@Param("title") String title, @Param("userId") Long userId);

    @Query("SELECT m FROM Menu m WHERE m.id IN " +
            "(SELECT MIN(m2.id) FROM Menu m2 JOIN m2.place p " +
            "WHERE (m2.title LIKE %:title% OR p.title LIKE %:title%) AND m2.user.id = :userId " +
            "GROUP BY m2.groupId)")
    Optional<List<Menu>> findMenuByTitle(@Param("title") String title, @Param("userId") Long userId);

//    Page<Menu> findByUserIdOrderByModifiedAt(Long userId, Pageable pageable);

    @Query("SELECT m FROM Menu m WHERE m.user.id = :userId AND m.modifiedAt = (" +
            "SELECT MAX(subM.modifiedAt) FROM Menu subM WHERE subM.groupId = m.groupId AND subM.user.id = :userId" +
            ") ORDER BY m.modifiedAt DESC")
    List<Menu> findDistinctByUserIdOrderByModifiedAtDesc(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT m FROM Menu m WHERE m.user.id != :userId " +
            "AND m.id IN (" +
            "    SELECT MIN(m2.id) FROM Menu m2 " +  // 그룹 내에서 최소 ID를 선택
            "    JOIN m2.tags mt " +
            "    WHERE mt.tag.name IN :tagNames " +
            "    GROUP BY m2.groupId " +  // groupId로 그룹화
            "    HAVING COUNT(DISTINCT mt.tag.name) >= :tagCount" +
            ")")
    List<Menu> findMenusByTagNamesInAndUserIdNotAndTagCountGreaterThanEqual(
            @Param("tagNames") List<String> tagNames,
            @Param("userId") Long userId,
            @Param("tagCount") int tagCount);

}

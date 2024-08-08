package com.ourMenu.backend.domain.menulist.dao;

import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.global.common.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuListRepository extends JpaRepository<MenuList, Long> {

    Optional<MenuList> findByIdAndUserId(Long id, Long userId);


    @Query("SELECT m FROM MenuList m WHERE m.status IN :status AND m.user.id = :userId")
    Optional<List<MenuList>> findAllMenuList(@Param("status")List<Status> status, @Param("userId") Long userId);

    @Query("SELECT m FROM MenuList m WHERE m.title = :title AND m.user.id = :userId AND m.status IN :status" )
    Optional<MenuList> findMenuListByTitle(@Param("title") String title, @Param("userId") Long userId, @Param("status")List<Status> status);

    @Query("SELECT m FROM MenuList m WHERE m.id = :menulistId AND m.user.id = :userId AND m.status IN :status")
    Optional<MenuList> findMenuListsById(@Param("menulistId") Long menulistId, @Param("userId") Long userId, @Param("status")List<Status> status);

    @Modifying
    @Query("UPDATE MenuList m SET m.priority = m.priority - 1 WHERE m.priority > :currentPriority AND m.priority <= :newPriority")
    void decreasePriorityBetween(@Param("currentPriority") Long currentPriority,@Param("newPriority") Long newPriority);

    @Modifying
    @Query("UPDATE MenuList m SET m.priority = m.priority + 1 WHERE m.priority < :currentPriority AND m.priority >= :newPriority")
    void increasePriorityBetween(@Param("currentPriority") Long currentPriority, @Param("newPriority") Long newPriority);
    @Query("SELECT COALESCE(MAX(m.priority), 0) FROM MenuList m WHERE m.user.id = :userId")
    Optional<Long> findMaxPriorityByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE MenuList m SET m.priority = m.priority - 1 WHERE m.priority > :currentPriority")
    void decreasePriorityGreaterThan(@Param("currentPriority") Long currentPriority);

}

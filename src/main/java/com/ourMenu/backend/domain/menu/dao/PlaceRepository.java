package com.ourMenu.backend.domain.menu.dao;

import com.ourMenu.backend.domain.menu.domain.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByUserIdAndTitleAndAddress(Long userId, String title, String address);

    @EntityGraph(attributePaths = "menu")
    List<Place> findAllByUserId(Long userId);

    @Query("SELECT p FROM Place p WHERE p.user.id = :userId")
    Optional<List<Place>> findPlacesByUserId(Long userId);

    @Query("SELECT p FROM Place p WHERE p.title LIKE %:title% AND p.user.id = :userId")
    Optional<List<Place>> findPlaceByTitle(@Param("title") String title, @Param("userId") Long userId);
}

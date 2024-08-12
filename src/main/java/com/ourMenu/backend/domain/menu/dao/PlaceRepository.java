package com.ourMenu.backend.domain.menu.dao;

import com.ourMenu.backend.domain.menu.domain.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByUserIdAndTitleAndAddress(Long userId, String title, String address);

    @EntityGraph(attributePaths = "menu")
    List<Place> findAllByUserId(Long userId);
}

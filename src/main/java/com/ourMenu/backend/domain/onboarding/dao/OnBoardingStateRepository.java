package com.ourMenu.backend.domain.onboarding.dao;

import com.ourMenu.backend.domain.menu.domain.Tag;
import com.ourMenu.backend.domain.onboarding.domain.OnBoardingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnBoardingStateRepository extends JpaRepository<OnBoardingState, Long> {

    Optional<OnBoardingState> findByOnBoardingState(Long userId);
}

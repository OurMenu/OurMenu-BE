package com.ourMenu.backend.domain.menu.application;

import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.dao.PlaceRepository;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import com.ourMenu.backend.domain.menu.domain.Place;
import com.ourMenu.backend.domain.menu.dto.request.StoreRequestDTO;
import com.ourMenu.backend.domain.menu.exception.MenuNotFoundException;
import com.ourMenu.backend.domain.user.application.UserService;
import com.ourMenu.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final UserService userService;
    private final MenuRepository menuRepository;

    @Transactional
    public Place createPlace(StoreRequestDTO storeInfo, Long userId) {

        Place existingPlace = placeRepository.findByUserIdAndTitleAndAddress(userId, storeInfo.getStoreName(), storeInfo.getStoreAddress()).orElse(null);
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 유저가 없습니다"));


        if (existingPlace != null) {
            // 필드값 업데이트 (null이 아닌 경우에만)
            if (storeInfo.getStoreAddress() != null) {
                existingPlace.setAddress(storeInfo.getStoreAddress());
            }
            if (storeInfo.getStoreMemo() != null) {
                existingPlace.setInfo(storeInfo.getStoreMemo());
            }
            if (storeInfo.getStoreLongitude() != null) {
                existingPlace.setLongitude(storeInfo.getStoreLongitude());
            }
            if (storeInfo.getStoreLatitude() != null) {
                existingPlace.setLatitude(storeInfo.getStoreLatitude());
            }
            existingPlace.setModifiedAt(LocalDateTime.now()); // 수정 시간 업데이트

            // 업데이트된 Place 객체 저장
            return placeRepository.save(existingPlace);
        }

        Place place = Place.builder()
                .title(storeInfo.getStoreName())
                .user(user)
                .address(storeInfo.getStoreAddress())
                .info(storeInfo.getStoreMemo())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .longitude(storeInfo.getStoreLongitude())
                .latitude(storeInfo.getStoreLatitude())
                .build();
        return placeRepository.save(place);
    }

    public Place save(Place place) {
        return placeRepository.save(place); // Place 객체를 저장하고 반환
    }

    /**
     * GET /place를 위한 메서드
     * @param userId
     * @return
     */
    public List<Place> findPlacesByUserId(Long userId){
        return placeRepository.findAllByUserId(userId);
    }

    public List<Menu> findMenuInPlaceByUserId(Long userId){
        List<Place> places = placeRepository.findPlacesByUserId(userId).orElseThrow(() -> new RuntimeException());
        List<Menu> menuList = new ArrayList<>();

        log.info(places.toString());

        for (Place place : places) {
            List<Menu> menus = menuRepository.findMenuByPlaceIdAndUserId(
                    place.getId(),
                    userId,
                    Arrays.asList(MenuStatus.CREATED, MenuStatus.UPDATED)
            ).orElseThrow(() -> new MenuNotFoundException());
            menuList.addAll(menus);
        }

        log.info(menuList.toString());
        return menuList;
    }

    public List<Menu> findMenuByTitle(String title, Long userId){
        List<Menu> result = new ArrayList<>();
        List<Menu> menus = menuRepository.findMenuByTitle(title, userId).orElseThrow(() -> new MenuNotFoundException());
        result.addAll(menus);

        List<Place> places = placeRepository.findPlaceByTitle(title, userId).orElseThrow(() -> new RuntimeException());
        for (Place place : places) {
            result.addAll(menuRepository.findMenuByPlaceIdAndUserId(place.getId(), userId, Arrays.asList(MenuStatus.CREATED, MenuStatus.UPDATED))
                    .orElseThrow(() -> new MenuNotFoundException()));
        }

        return result;
    }
}

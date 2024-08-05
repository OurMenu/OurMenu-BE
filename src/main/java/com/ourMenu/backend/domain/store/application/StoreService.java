package com.ourMenu.backend.domain.store.application;

import com.ourMenu.backend.domain.store.dao.StoreRepository;
import com.ourMenu.backend.domain.store.dao.UserStoreRepository;
import com.ourMenu.backend.domain.store.domain.Store;
import com.ourMenu.backend.domain.store.domain.UserStore;
import com.ourMenu.backend.domain.store.exception.SearchResultNotFoundException;
import com.ourMenu.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserStoreRepository userStoreRepository;

    /**
     * 제목에 name을 포함 하는 음식점을 반환한다.(15개)
     *
     * @param name
     * @return 음식점 갯수
     */
    public List<Store> searchStore(String name) {
        Pageable pageable = PageRequest.of(0, 15);
        //return storeRepository.findByNameContaining(name);
        Page<Store> page = storeRepository.findByNameContaining(name, pageable);
        return page.getContent();
    }


    /**
     * 가게를 조회하고 해당 유저의 엔티티를 수정한다.
     * @param id
     * @param userId
     * @return
     */
    @Transactional
    public Store findOneByUser(String id, Long userId) {
        Optional<Store> optionalStore = storeRepository.findById(id);
        if (optionalStore.isEmpty())
            throw new SearchResultNotFoundException();
        updateUserStore(optionalStore.get(),userId);
        return optionalStore.get();
    }

    /**
     * user의 store 검색 기록을 업데이트한다.
     * @param store
     * @param userId
     * @return 수정된 userStore
     */
    private UserStore updateUserStore(Store store,Long userId) {
        Optional<UserStore> userStoreOptional = userStoreRepository.findByUserIdAndStoreId(userId, store.getId());
        if (userStoreOptional.isEmpty()) {
            UserStore userStore = UserStore.toEntity(store,userId);
            return userStoreRepository.save(userStore);
        }
        return userStoreOptional.get().updateModifiedAt();
    }

    public List<UserStore> findHistory(Long userId) {
        return userStoreRepository.findByUserIdOrderByModifiedAtDesc(userId);
    }
}

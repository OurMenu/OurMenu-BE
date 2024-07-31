package com.ourMenu.backend.domain.store.application;

import com.ourMenu.backend.domain.store.dao.StoreRepository;
import com.ourMenu.backend.domain.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    public final StoreRepository storeRepository;

    /**
     * 제목에 name을 포함 하는 음식점을 반환한다.(5개)
     * @param name
     * @return 음식점 갯수
     */
    public List<Store> searchStore(String name){
        Pageable pageable= PageRequest.of(0,5);
        //return storeRepository.findByNameContaining(name);
        Page<Store> page=storeRepository.findByNameContaining(name,pageable);
        return page.getContent();
    }
}

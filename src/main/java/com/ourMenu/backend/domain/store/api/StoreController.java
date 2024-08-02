package com.ourMenu.backend.domain.store.api;

import com.ourMenu.backend.domain.store.api.response.GetStoresSearch;
import com.ourMenu.backend.domain.store.application.StoreService;
import com.ourMenu.backend.domain.store.domain.Store;
import com.ourMenu.backend.domain.store.dao.StoreRepository;
import com.ourMenu.backend.domain.store.exception.SearchResultNotFoundException;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.exception.ErrorResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ourMenu.backend.global.exception.ErrorCode.SEARCH_RESULT_NOT_FOUND;

@RestController
@RequestMapping("stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/search")
    public ApiResponse<List<GetStoresSearch>>search(@RequestParam String name){
        List<Store> storeList = storeService.searchStore(name);
        if(storeList.size()==0){
           throw new SearchResultNotFoundException();

        }
        return ApiUtils.success(storeList.stream().map(GetStoresSearch::toDto).toList());
    }
}

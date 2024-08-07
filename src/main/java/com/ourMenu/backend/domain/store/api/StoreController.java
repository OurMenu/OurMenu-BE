package com.ourMenu.backend.domain.store.api;

import com.ourMenu.backend.domain.store.api.response.GetSearchHistory;
import com.ourMenu.backend.domain.store.api.response.GetSimpleStoreSearch;
import com.ourMenu.backend.domain.store.api.response.GetStoresSearch;
import com.ourMenu.backend.domain.store.application.StoreService;
import com.ourMenu.backend.domain.store.domain.Store;
import com.ourMenu.backend.domain.store.domain.UserStore;
import com.ourMenu.backend.domain.store.exception.SearchResultNotFoundException;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/search")
    public ApiResponse<List<GetSimpleStoreSearch>> search(@RequestParam String title) {
        List<Store> storeList = storeService.searchStore(title);
        if (storeList.size() == 0) {
            throw new SearchResultNotFoundException();

        }
        return ApiUtils.success(storeList.stream().map(GetSimpleStoreSearch::toDto).toList());
    }

    @GetMapping("/{id}")
    public ApiResponse<GetStoresSearch> findById(@RequestParam("id") String id,
                                                 @UserId Long userId) {
        Store findStore = storeService.findOneByUser(id, userId);
        return ApiUtils.success(GetStoresSearch.toDto(findStore));
    }

    @GetMapping("/search-history")
    public ApiResponse<List<GetSearchHistory>> getSearchHistory(@UserId Long userId) {
        List<UserStore> storeList=storeService.findHistory(userId);

        return ApiUtils.success(storeList.stream().map(GetSearchHistory::toDto).toList());
    }
}

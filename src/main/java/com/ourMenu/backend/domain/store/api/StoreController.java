package com.ourMenu.backend.domain.store.api;

import com.ourMenu.backend.domain.store.api.response.GetStoresSearch;
import com.ourMenu.backend.domain.store.application.StoreService;
import com.ourMenu.backend.domain.store.domain.Store;
import com.ourMenu.backend.domain.store.exception.SearchResultNotFoundException;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/place/info")
    public ApiResponse<List<GetStoresSearch>> search(@RequestParam String title) {
        List<Store> storeList = storeService.searchStore(title);
        if (storeList.size() == 0) {
            throw new SearchResultNotFoundException();

        }
        return ApiUtils.success(storeList.stream().map(GetStoresSearch::toDto).toList());
    }

    @GetMapping("/place/{id}")
    public ApiResponse<GetStoresSearch> findById(@RequestParam("id") String id, @Parameter(hidden = true) @UserId Long userId) {
        Store findStore = storeService.findOneByUser(id, userId);
        return ApiUtils.success(GetStoresSearch.toDto(findStore));
    }
}

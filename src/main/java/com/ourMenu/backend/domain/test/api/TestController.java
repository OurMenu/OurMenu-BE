package com.ourMenu.backend.domain.test.api;

import com.ourMenu.backend.domain.test.api.request.SaveEntityRequest;
import com.ourMenu.backend.domain.test.api.response.FindEntityByIdResponse;
import com.ourMenu.backend.domain.test.api.response.SaveEntityResponse;
import com.ourMenu.backend.domain.test.application.TestService;
import com.ourMenu.backend.domain.test.domain.TestEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/save")
    @Operation(summary = "테스트회원 저장", description = "swagger 테스트를 위한 저장 API")
    public SaveEntityResponse saveTestEntity(@RequestBody SaveEntityRequest saveEntityRequest){
        TestEntity testEntity=TestEntity
                .builder()
                .name(saveEntityRequest.name())
                .build();

        TestEntity saveTestEntity = testService.saveTestEntity(testEntity);

        return SaveEntityResponse
                .builder()
                .id(saveTestEntity.getId())
                .name(saveEntityRequest.name())
                .build();
    }

    @GetMapping("/{SaveEntityId}")
    @Operation(summary = "테스트회원 조회", description = "swagger 테스트를 위한 조회 API")
    public FindEntityByIdResponse findById(@PathVariable("SaveEntityId")Long saveEntityId){
        TestEntity findTestEntity = testService.findTestEntity(saveEntityId);
        return FindEntityByIdResponse
                .builder()
                .id(findTestEntity.getId())
                .name(findTestEntity.getName())
                .build();
    }
}

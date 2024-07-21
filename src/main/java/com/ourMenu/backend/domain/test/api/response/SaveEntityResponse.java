package com.ourMenu.backend.domain.test.api.response;

import com.ourMenu.backend.domain.test.domain.TestEntity;
import lombok.Builder;

@Builder
public record SaveEntityResponse(Long id, String name) {
}

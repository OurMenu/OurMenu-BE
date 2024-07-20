package com.ourMenu.backend.domain.test.api.response;

import lombok.Builder;

@Builder
public record FindJdbcByIdResponse(Long id, String name) {
}

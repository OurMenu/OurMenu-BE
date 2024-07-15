package com.ourMenu.backend.domain.test.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record FindEntityByIdResponse(Long id, String name) {
}

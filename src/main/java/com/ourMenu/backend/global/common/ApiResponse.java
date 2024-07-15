package com.ourMenu.backend.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ourMenu.backend.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    private final T response;

    private final ErrorResponse errorResponse;

    public static <T> ApiResponse<T> create(boolean isSuccess, T response, ErrorResponse errorResponse) {
        return new ApiResponse<>(isSuccess, response, errorResponse);
    }

}

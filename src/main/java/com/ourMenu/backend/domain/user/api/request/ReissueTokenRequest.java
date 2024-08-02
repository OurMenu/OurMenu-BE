package com.ourMenu.backend.domain.user.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueTokenRequest {

    private String refreshToken;

}

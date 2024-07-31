package com.ourMenu.backend.domain.user.api.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "이메일 형식이 아닙니다")
    private String email;
    private String password;
    private String nickname;

}

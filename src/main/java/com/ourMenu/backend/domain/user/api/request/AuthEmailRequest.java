package com.ourMenu.backend.domain.user.api.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthEmailRequest {

    @Email(message = "이메일 형식이 아닙니다")
    private String email;

}

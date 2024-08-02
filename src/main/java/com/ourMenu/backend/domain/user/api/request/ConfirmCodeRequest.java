package com.ourMenu.backend.domain.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class ConfirmCodeRequest {

    @Email(message = "이메일 형식이 아닙니다")
    private String email;
    @Pattern(regexp = "^\\d{6}$", message = "올바른 코드 형식이 아닙니다")
    private String code;

}

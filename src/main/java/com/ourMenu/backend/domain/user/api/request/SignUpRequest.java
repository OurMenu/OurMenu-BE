package com.ourMenu.backend.domain.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "이메일 형식이 아닙니다")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호의 형식이 올바르지 않습니다")
    private String password;
    @Size(min = 1, max = 6, message = "닉네임은 1글자 이상, 최대 6글자까지 가능합니다")
    private String nickname;

}

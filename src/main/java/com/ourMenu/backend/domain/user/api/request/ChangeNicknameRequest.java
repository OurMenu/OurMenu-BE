package com.ourMenu.backend.domain.user.api.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeNicknameRequest {

    @Size(min = 1, max = 6, message = "닉네임은 1글자 이상, 최대 6글자까지 가능합니다")
    private String nickname;

}

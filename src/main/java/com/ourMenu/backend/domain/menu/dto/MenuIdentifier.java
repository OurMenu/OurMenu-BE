package com.ourMenu.backend.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class MenuIdentifier {

    private Long groupId;
    private Long userId;
}

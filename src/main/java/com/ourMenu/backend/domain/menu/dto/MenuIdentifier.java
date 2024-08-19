package com.ourMenu.backend.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class MenuIdentifier {

    private Long groupId;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuIdentifier that = (MenuIdentifier) o;
        return Objects.equals(getGroupId(), that.getGroupId()) && Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupId(), getUserId());
    }
}

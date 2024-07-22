package com.ourMenu.backend.menu.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;


public enum MenuStatus {
    CREATED,
    UPDATED,
    DELETED
}

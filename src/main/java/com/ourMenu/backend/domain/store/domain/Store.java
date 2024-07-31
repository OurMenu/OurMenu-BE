package com.ourMenu.backend.domain.store.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "store") // 실제 몽고 DB 컬렉션 이름
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    @Id
    private String id;
    private String name;
    private String address;
    private String type;
    private List<String> images;
    private List<Menu> menu;
    private String time;
}

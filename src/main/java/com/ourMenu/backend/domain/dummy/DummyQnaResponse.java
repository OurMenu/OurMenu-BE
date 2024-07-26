package com.ourMenu.backend.domain.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DummyQnaResponse {
//    {
//        "isSuccess": true,
//            "response": {
//        "commentImg": "url",
//                "menu": [ {
//            "menuImg": "url",
//                    "menuTitle": "화산돈부리",
//                    "storeName": "화산라멘 홍대점"
//        } ]
//    }
    private String commentImg;
    private List<DummyQnaMenuDTO> dummyQnaMenuDTOList;

}

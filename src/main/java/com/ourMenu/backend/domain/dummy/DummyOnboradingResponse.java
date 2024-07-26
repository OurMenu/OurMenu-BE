package com.ourMenu.backend.domain.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;

@Data
@AllArgsConstructor
public class DummyOnboradingResponse {
    private Long id;
    private String question;
    private String yes;
    private String yesImg;
    private String yesAnswerImg;
    private String no;
    private String noImg;
    private String noAnswerImg;


}

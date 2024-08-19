package com.ourMenu.backend.domain.onboarding.util;

import com.ourMenu.backend.domain.onboarding.domain.AnswerType;

import java.util.List;
import java.util.Random;

public class S3Util {

    private S3Util() {
    }
    public static Random random = new Random();
    ;
    public static String S3Path = "https://hobbytat.s3.ap-northeast-2.amazonaws.com/온보딩/";

    public static int DEFAULT_TAG_SIZE = 26;
    public static int ONBOARDING_ANSWER_SIZE = 10;

    public static String getS3SvgPath(String imgName) {
        return S3Path + imgName + ".svg";
    }

    public static int getRandomQuestionId() {
        return random.nextInt(5) + 1;
    }

    public static AnswerType getRandomAnswerType(){
        int randomInt = random.nextInt(2);
        if(randomInt == 0)
            return AnswerType.YES;
        return AnswerType.NO;
    }
}

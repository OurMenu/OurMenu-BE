package com.ourMenu.backend.domain.onboarding.util;

import java.util.List;

public class S3Util {

    private S3Util() {
    }
    ;
    public static String S3Path = "https://hobbytat.s3.ap-northeast-2.amazonaws.com/온보딩/";

    public static int DEFAULT_TAG_SIZE = 26;
    public static int ONBOARDING_ANSWER_SIZE = 10;

    public static String getS3SvgPath(String imgName) {
        return S3Path + imgName + ".svg";
    }


}

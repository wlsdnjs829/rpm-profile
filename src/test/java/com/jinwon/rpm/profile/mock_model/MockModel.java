package com.jinwon.rpm.profile.mock_model;

public class MockModel {

    public static final String MEMBER =
            "{" +
                    "\"password\": \"1q2w3e4r5t\"," +
                    "\"reTypePassword\": \"1q2w3e4r5t\"," +
                    "\"code\": \"ljw0829code\"," +
                    "\"name\": \"이진원\"," +
                    "\"country\": \"AF\"," +
                    "\"agreeTermsTypes\": [" +
                    "\"UNIFIED_MIDAS_MEMBER\"" +
                    "]," +
                    "\"memberStatus\": \"ACTIVATE\"" +
                    "}";

    public static final String CERT_SEND_DTO =
            "{" +
                    "\"receiver\": \"ljw0829@midasin.com\"" +
                    "}";

    public static final String TERMS_OF_SERVICE =
            "{" +
                    "\"type\": \"TERMS_OF_SERVICE\"," +
                    "\"termsCondition\": \"ESSENTIAL\"," +
                    "\"version\": \"0.1\"" +
                    "}";

    public static final String PRIVACY_COLLECTION =
            "{" +
                    "\"type\": \"PRIVACY_COLLECTION\"," +
                    "\"version\": \"0.1\"" +
                    "}";

    public static final String RECEIVE_MARKETING_INFO =
            "{" +
                    "\"type\": \"RECEIVE_MARKETING_INFO\"," +
                    "\"version\": \"0.1\"" +
                    "}";

    public static final String TERMS_AGREEMENT =
            "{" +
                    "\"termsAgreementId\": 1," +
                    "\"agreeType\": \"USE\"," +
                    "\"lastModifiedDateTime\": \"2022-07-18T02:03:13.871Z\"" +
                    "}";

    public static final String PRIVACY_COLLECTION_TERMS_AGREEMENT =
            "{" +
                    "\"termsAgreementId\": 1," +
                    "\"agreeType\": \"USE\"," +
                    "\"terms\": {" +
                    "\"type\": \"PRIVACY_COLLECTION\"," +
                    "\"version\": \"0.1\"" +
                    "}," +
                    "\"lastModifiedDateTime\": \"2022-07-18T02:03:13.871Z\"" +
                    "}";

    public static final String DELETE_MEMBER_DTO =
            "{" +
                    "\"withdrawTypes\": [" +
                    "\"UNNECESSARY_SERVICE\"," +
                    "\"FREQUENT_EMAILS\"" +
                    "]," +
                    "\"reason\": \"탈퇴 테스트\"," +
                    "\"password\": \"1q2w3e4r5t\"" +
                    "}";

    public static final String SUCCESS_COMMON_PASSWORD_DTO =
            "{" +
                    "\"email\": \"ljw0829@midasin.com\"," +
                    "\"password\": \"1q2w3e4r5t\"," +
                    "\"reTypePassword\": \"1q2w3e4r5t\"" +
                    "}";

    public static final String FAIL_COMMON_PASSWORD_DTO =
            "{" +
                    "\"email\": \"ljw0829@midasin.com\"," +
                    "\"password\": \"1q2w3e4r5t\"," +
                    "\"reTypePassword\": \"1q2w3e4r5t123\"" +
                    "}";

}

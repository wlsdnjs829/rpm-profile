package com.jinwon.rpm.profile.mock_model;

public class MockModel {

    public static final String MEMBER =
            "{" +
                    "\"password\": \"1q2w3e4r5t\"," +
                    "\"email\": \"ljw0829@midasin.com\"," +
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

}

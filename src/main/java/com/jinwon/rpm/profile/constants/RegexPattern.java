package com.jinwon.rpm.profile.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexPattern {

    public final String EMAIL_REGEX =
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]{1,64}@[a-zA-Z0-9.-]{1,255}.[a-zA-Z]{2,3}$";

    public final String PHONE_REGEX = "^\\d{3}-\\d{3,4}-\\d{4}$";

    public final String NAME_REGEX = "^[a-zA-Zㄱ-ㅎ가-힣]{1,30}$";

    public final String NUMBER_REGEX = ".*[0-9].*";

    public final String LOWER_CASE_REGEX = ".*[a-z].*";

    public final String UPPER_CASE_REGEX = ".*[A-Z].*";

    public final String ALLOW_CASE_REGEX = "[^0-9a-zA-Z]+";

}

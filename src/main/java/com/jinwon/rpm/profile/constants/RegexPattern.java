package com.jinwon.rpm.profile.constants;

import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Regex 패턴 Const
 */
@UtilityClass
public class RegexPattern {

    public final String EMAIL_REGEX =
            "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*" +
                    "@[^-][\\p{L}0-9-]{1,254}+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,3})$";

    public final String PHONE_REGEX = "^\\d{3}-\\d{3,4}-\\d{4}$";

    public final String NAME_REGEX = "^[a-zA-Zㄱ-ㅎ가-힣]{1,30}$";

    public final String NUMBER_REGEX = ".*[0-9].*";

    public final String LOWER_CASE_REGEX = ".*[a-z].*";

    public final String UPPER_CASE_REGEX = ".*[A-Z].*";

    public final String ALLOW_CASE_REGEX = "[^0-9a-zA-Z]+";

    public List<String> multiPatterns() {
        return List.of(NUMBER_REGEX, LOWER_CASE_REGEX, UPPER_CASE_REGEX);
    }

}

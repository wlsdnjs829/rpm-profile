package com.jinwon.rpm.profile.enums;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Arrays;

public enum PasswordValidator implements Validator {

    CONSECUTIVE_NUMBERS {

        private static final String REGEX = "0123456789";

        @Override
        public boolean run(String target) {
            return StringUtils.contains(REGEX, target);
        }

    },

    CONSECUTIVE_STRING {

        private static final String REGEX = "abcdefghijklmnopqrstuvwxyz";

        @Override
        public boolean run(String target) {
            return StringUtils.contains(REGEX, target);
        }

    },

    KEYBOARD_FIRST_LINE {

        private static final String REGEX = "qwertyuiop";

        @Override
        public boolean run(String target) {
            return StringUtils.contains(REGEX, target);
        }

    },

    KEYBOARD_SECOND_LINE {

        private static final String REGEX = "asdfghjkl";

        @Override
        public boolean run(String target) {
            return StringUtils.contains(REGEX, target);
        }

    },

    KEYBOARD_THIRD_LINE {

        private static final String REGEX = "zxcvbnm";

        @Override
        public boolean run(String target) {
            return StringUtils.contains(REGEX, target);
        }

    },

    CONSECUTIVE_CHARACTERS {

        private static final int ZERO = 0;

        @Override
        public boolean run(String target) {
            final char firstChar = target.charAt(ZERO);

            return target.chars()
                    .mapToObj(value -> (char) value)
                    .allMatch(character -> character.equals(firstChar));
        }

    };

    public static void validator(String target) {
        if (StringUtils.isEmpty(target)) {
            return;
        }

        final PasswordValidator passwordValidator = Arrays.stream(PasswordValidator.values())
                .filter(validator -> validator.run(target))
                .findFirst()
                .orElse(null);

        Assert.isNull(passwordValidator, ErrorMessage.INVALID_PASSWORD.name());
    }

}

interface Validator {

    boolean run(String target);

}

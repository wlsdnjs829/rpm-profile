package com.jinwon.rpm.profile.infra.validator;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.domain.profile.inner_dto.PasswordDto;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 패스워드 유효성 체크
 */
public final class PasswordValidator {

    @Getter
    public enum FullPasswordValidator implements Validator {

        ALLOW_CASE_REGEX {

            @Override
            public boolean run() {
                final String replacePassword = password.replaceAll(RegexPattern.ALLOW_CASE_REGEX, StringUtils.EMPTY);
                return !StringUtils.equals(password, replacePassword);
            }

        },

        LENGTH_REGEX {

            @Override
            public boolean run() {
                final int length = password.length();
                return length < 10 || length > 16;
            }

        },

        MULTI_CASE_REGEX {

            private static final int CONDITION_COUNT = 2;

            @Override
            public boolean run() {
                final List<String> patterns = RegexPattern.multiPatterns();

                final AtomicInteger match = new AtomicInteger();

                patterns.stream()
                        .filter(password::matches)
                        .forEach(pattern -> match.incrementAndGet());

                return match.get() < CONDITION_COUNT;
            }

        },

        EMAIL_REGEX {

            private static final int ZERO = 0;
            private static final String AT = "@";

            @Override
            public boolean run() {
                final String front = email.substring(ZERO, email.indexOf(AT));
                return StringUtils.contains(password, front);
            }

        };

        private static String password;
        private static String email;


        public static void validator(PasswordDto passwordDto) {
            updateValidatorInfo(passwordDto);
            Assert.notNull(password, ErrorMessage.INVALID_PASSWORD.name());
            Assert.notNull(email, ErrorMessage.INVALID_EMAIL.name());

            final FullPasswordValidator fullPasswordValidator =
                    Arrays.stream(FullPasswordValidator.values())
                            .filter(Validator::run)
                            .findFirst()
                            .orElse(null);

            Assert.isNull(fullPasswordValidator, ErrorMessage.INVALID_PASSWORD.name());
        }

        protected static void updateValidatorInfo(PasswordDto passwordDto) {
            Assert.notNull(passwordDto, ErrorMessage.INVALID_PASSWORD.name());
            password = passwordDto.getPassword();
            email = passwordDto.getEmail();
        }

    }

    @Getter
    public enum PartPasswordValidator implements Validator {

        CONSECUTIVE_NUMBERS {

            private static final String LOWER_REGEX = "01234567890";
            private static final String UPPER_REGEX = "~!@#$%^&*()_+";

            @Override
            public boolean run() {
                return matchRegex(LOWER_REGEX, UPPER_REGEX);
            }

        },

        CONSECUTIVE_STRING {

            private static final String LOWER_REGEX = "abcdefghijklmnopqrstuvwxyz";
            private static final String UPPER_REGEX = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

            @Override
            public boolean run() {
                return matchRegex(LOWER_REGEX, UPPER_REGEX);
            }

        },

        KEYBOARD_FIRST_LINE {

            private static final String LOWER_REGEX = "qwertyuiop[]";
            private static final String UPPER_REGEX = "QWERTYUIOP{}";

            @Override
            public boolean run() {
                return matchRegex(LOWER_REGEX, UPPER_REGEX);
            }

        },

        KEYBOARD_SECOND_LINE {

            private static final String LOWER_REGEX = "asdfghjkl;";
            private static final String UPPER_REGEX = "ASDFGHJKL:";

            @Override
            public boolean run() {
                return matchRegex(LOWER_REGEX, UPPER_REGEX);
            }

        },

        KEYBOARD_THIRD_LINE {

            private static final String LOWER_REGEX = "zxcvbnm,./";
            private static final String UPPER_REGEX = "ZXCVBNM<>?";

            @Override
            public boolean run() {
                return matchRegex(LOWER_REGEX, UPPER_REGEX);
            }

        },

        CONSECUTIVE_CHARACTERS {

            private static final int ZERO = 0;

            @Override
            public boolean run() {
                final char firstChar = password.charAt(ZERO);

                return password.chars()
                        .mapToObj(value -> (char) value)
                        .allMatch(character -> character.equals(firstChar));
            }

        };

        private static String password;

        public static void validator(PasswordDto passwordDto) {
            updateValidatorInfo(passwordDto);
            Assert.notNull(password, ErrorMessage.INVALID_PASSWORD.name());

            final PartPasswordValidator passwordValidator =
                    Arrays.stream(PartPasswordValidator.values())
                            .filter(Validator::run)
                            .findFirst()
                            .orElse(null);

            Assert.isNull(passwordValidator, ErrorMessage.INVALID_PASSWORD.name());
        }

        protected static void updateValidatorInfo(PasswordDto passwordDto) {
            Assert.notNull(passwordDto, ErrorMessage.INVALID_PASSWORD.name());
            password = passwordDto.getPassword();
        }

        protected boolean matchRegex(String lowerCondition, String upperCondition) {
            return StringUtils.contains(lowerCondition, password) || StringUtils.contains(upperCondition, password);
        }

    }

    private interface Validator {

        boolean run();

    }

}
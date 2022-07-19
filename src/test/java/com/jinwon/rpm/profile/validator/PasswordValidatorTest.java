package com.jinwon.rpm.profile.validator;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.member.inner_dto.PasswordDto;
import com.jinwon.rpm.profile.infra.validator.PasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 패스워드 유효성 검사 테스트
 */
@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    private final Logger log = LoggerFactory.getLogger(PasswordValidatorTest.class);

    private static final String EMAIL = "ljw0829@midasin.com";
    private static final String PASSWORD = "1q2w3e4r5t";

    @Test
    @DisplayName("빈 생성자 테스트")
    void noArgsConstructTest() throws NoSuchMethodException {
        final Constructor<PasswordValidator> constructor = PasswordValidator.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class);
    }

    @Nested
    @DisplayName("패스워드 전체 유효성 검사")
    class FullPasswordValidatorTest {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("정상 동작한 경우")
            void case1() {
                final PasswordDto passwordDto = new PasswordDto(PASSWORD, EMAIL);

                PasswordValidator.FullPasswordValidator.validator(passwordDto);

                assertThat(passwordDto).isNotNull();

                assertThat(passwordDto.getPassword())
                        .isNotNull()
                        .isEqualTo(PASSWORD);

                assertThat(passwordDto.getEmail())
                        .isNotNull()
                        .isEqualTo(EMAIL);
            }

        }

        ;

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> PasswordValidator.FullPasswordValidator.validator(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.INVALID_PARAM.name());

            }

            @Test
            @DisplayName("이메일이 없는 경우")
            void case2() {
                final PasswordDto passwordDto = new PasswordDto(PASSWORD, null);

                assertThatThrownBy(() -> PasswordValidator.FullPasswordValidator.validator(passwordDto))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.INVALID_EMAIL.name());

            }

            @DisplayName("비밀번호 조건에 맞지 않는 경우")
            @ParameterizedTest(name = "{2}")
            @CsvSource({
                    ", ljw0829@midasin.com, 비밀번호가 없는 경우",
                    "1q2w3e4r5t!, ljw0829@midasin.com, 허용되지 않은 문구가 들어간 경우",
                    "1q2w3e4r5, ljw0829@midasin.com, 허용된 길이 수 보다 짧은 경우",
                    "1q2w3e4r5t1q2w3e4r, ljw0829@midasin.com, 허용된 길이 수 보다 긴 경우",
                    "1111111111, ljw0829@midasin.com, 다중 조건이 만족되지 않은 경우",
                    "ljw0829, ljw0829@midasin.com, 비밀번호에 이메일이 포함된 경우",

            })
            void case3(String password, String email, String displayName) {
                log.info(() -> displayName);

                final PasswordDto passwordDto = new PasswordDto(password, email);

                assertThatThrownBy(() -> PasswordValidator.FullPasswordValidator.validator(passwordDto))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.INVALID_PASSWORD.name());

            }

        }

    }

    @Nested
    @DisplayName("패스워드 부분 유효성 검사")
    class PartPasswordValidatorTest {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("정상 동작한 경우")
            void case1() {
                final PasswordDto passwordDto = new PasswordDto(PASSWORD, EMAIL);

                PasswordValidator.PartPasswordValidator.validator(passwordDto);

                assertThat(passwordDto).isNotNull();

                assertThat(passwordDto.getPassword())
                        .isNotNull()
                        .isEqualTo(PASSWORD);

                assertThat(passwordDto.getEmail())
                        .isNotNull()
                        .isEqualTo(EMAIL);
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> PasswordValidator.PartPasswordValidator.validator(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.INVALID_PARAM.name());

            }

            @DisplayName("비밀번호 조건에 맞지 않는 경우")
            @ParameterizedTest(name = "{1}")
            @CsvSource({
                    ", 비밀번호가 없는 경우",
                    "123, 첫번째 열 연속된 숫자가 들어간 경우",
                    "!@#, 첫번째 열 연속된 문자가 들어간 경우",
                    "qwe, 두번째 열 연속된 소문자 들어간 경우",
                    "QWE, 두번째 열 연속된 대문자 들어간 경우",
                    "asd, 세번째 열 연속된 소문자 들어간 경우",
                    "ASD, 세번째 열 연속된 대문자 들어간 경우",
                    "zxc, 네번째 열 연속된 소문자 들어간 경우",
                    "ZXC, 네번째 열 연속된 대문자 들어간 경우",
                    "abc, 알파벳 순서대로 소문자 들어간 경우",
                    "ABC, 알파벳 순서대로 대문자 들어간 경우",
                    "aaa, 동일한 문자 3개가 포함된 경우",
            })
            void case2(String password, String displayName) {
                log.info(() -> displayName);

                final PasswordDto passwordDto = new PasswordDto(password, null);

                assertThatThrownBy(() -> PasswordValidator.PartPasswordValidator.validator(passwordDto))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.INVALID_PASSWORD.name());

            }

        }

    }

}
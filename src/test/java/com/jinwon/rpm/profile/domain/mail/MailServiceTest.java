package com.jinwon.rpm.profile.domain.mail;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwon.rpm.profile.domain.mail.dto.CertMailDto;
import com.jinwon.rpm.profile.domain.mail.dto.CertSenderDto;
import com.jinwon.rpm.profile.domain.mail.dto.ChangeSenderDto;
import com.jinwon.rpm.profile.domain.member.MemberService;
import com.jinwon.rpm.profile.infra.component.EventPublisherComponent;
import com.jinwon.rpm.profile.infra.component.RedisComponent;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.mock_model.MockModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * 메일 서비스 테스트
 */
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private AmazonSimpleEmailService amazonSimpleEmailService;

    @Mock
    private RedisComponent redisComponent;

    @Mock
    private EventPublisherComponent eventPublisherComponent;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String SENDER = "ljw0829@midasin.com";
    private static final String CODE = "b9650478ac3934130bf73b76e7bc52d5f28728714469a62a9cd71d2af1d6f38e";
    private static final String CERTIFICATION_SUBJECT = "인증 메일";
    private static final String ENCRYPT_COMPONENT_ROLE = "encrypt_component_role";
    private static final String CODE_KEY = "mid@sTestCodeKey";

    private MailService mailService;

    @BeforeEach
    void setUp() {
        mailService = new MailService(memberService, amazonSimpleEmailService, redisComponent, eventPublisherComponent);

        lenient().when(redisComponent.getCertMailThrowIfInvalid(anyString()))
                .thenReturn(SENDER);

        System.setProperty(ENCRYPT_COMPONENT_ROLE, CODE_KEY);
    }

    @Nested
    @DisplayName("인증 메일 발송")
    class SendCertMail {

        private CertSenderDto certSenderDto;

        @BeforeEach
        void setUp() throws JsonProcessingException {
            certSenderDto = objectMapper.readValue(MockModel.CERT_SEND_DTO, CertSenderDto.class);
            certSenderDto.essentialInfo(SENDER, CERTIFICATION_SUBJECT);
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("인증 메일 발송 성공")
            void case1() {
                assertThat(certSenderDto.getContents()).isNull();

                mailService.sendCertMail(certSenderDto);

                assertThat(certSenderDto.getContents()).isNotNull();
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> mailService.sendCertMail(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("try-catch 내부 로직 실패 시")
            void case2() {
                lenient().doThrow(AmazonServiceException.class)
                        .when(amazonSimpleEmailService)
                        .sendEmail(any(SendEmailRequest.class));

                assertThatThrownBy(() -> mailService.sendCertMail(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

        }

    }

    @Nested
    @DisplayName("인증 정보 조회")
    class GetCertMail {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @BeforeEach
            void setUp() {
                when(redisComponent.getCertMailThrowIfInvalid(CODE))
                        .thenReturn(SENDER);
            }

            @Test
            @DisplayName("인증 메일 발송 성공")
            void case1() {
                final CertMailDto certMail = mailService.getCertMail(CODE);

                assertThat(certMail).isNotNull();
                assertThat(certMail.mail()).isEqualTo(SENDER);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> mailService.getCertMail(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

        }

    }

    @Nested
    @DisplayName("메일 변경 메일 발송")
    class SendChangeMail {

        private ChangeSenderDto changeSenderDto;

        @BeforeEach
        void setUp() throws JsonProcessingException {
            changeSenderDto = objectMapper.readValue(MockModel.CERT_SEND_DTO, ChangeSenderDto.class);
            changeSenderDto.essentialInfo(SENDER, CERTIFICATION_SUBJECT);
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("메일 변경 메일 발송 성공")
            void case1() {
                assertThat(changeSenderDto.getContents()).isNull();

                mailService.sendChangeMail(CODE, changeSenderDto);

                assertThat(changeSenderDto.getContents()).isNotNull();
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("토큰이 없는 경우")
            void case1() {
                assertThatThrownBy(() -> mailService.sendChangeMail(null, changeSenderDto))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("변경 발신 객체가 없는 경우")
            void case2() {
                assertThatThrownBy(() -> mailService.sendChangeMail(CODE, null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("try-catch 내부 로직 실패 시")
            void case3() {
                lenient().doThrow(AmazonServiceException.class)
                        .when(amazonSimpleEmailService)
                        .sendEmail(any(SendEmailRequest.class));

                assertThatThrownBy(() -> mailService.sendChangeMail(CODE, changeSenderDto))
                        .isInstanceOf(CustomException.class);
            }

        }

    }

}
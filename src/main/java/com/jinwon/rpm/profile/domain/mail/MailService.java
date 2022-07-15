package com.jinwon.rpm.profile.domain.mail;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.mail.dto.CertMailDto;
import com.jinwon.rpm.profile.domain.mail.dto.CertSenderDto;
import com.jinwon.rpm.profile.domain.mail.dto.ChangeSenderDto;
import com.jinwon.rpm.profile.domain.mail.inner_dto.CertInfoDto;
import com.jinwon.rpm.profile.domain.mail.inner_dto.SenderDto;
import com.jinwon.rpm.profile.domain.member.MemberService;
import com.jinwon.rpm.profile.infra.component.EventPublisherComponent;
import com.jinwon.rpm.profile.infra.component.RedisComponent;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.utils.EncryptionUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * 메일 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final MemberService memberService;
    private final AmazonSimpleEmailService amazonSimpleEmailService;

    private final RedisComponent redisComponent;
    private final EventPublisherComponent eventPublisherComponent;

    private static final String SENDER = "ljw0829@midasin.com";
    private static final String CERTIFICATION_SUBJECT = "인증 메일";

    private static final int RANDOM_CERT_CODE_COUNT = 10;

    /**
     * 인증 메일 발송
     *
     * @param senderDto 발송 객체
     */
    public void sendCertMail(CertSenderDto senderDto) {
        Assert.notNull(senderDto, ErrorMessage.INVALID_PARAM.name());

        final String receiver = senderDto.getReceiver();
        memberService.validEmailThrowIfExist(receiver);

        senderDto.essentialInfo(SENDER, CERTIFICATION_SUBJECT);

        try {
            final String code = EncryptionUtil.encode(receiver);
            final String certUrl = senderDto.getReturnUrl() + code;

            senderDto.updateContents(certUrl);

            amazonSimpleEmailService.sendEmail(senderDto.toSendEmailRequest());
            redisComponent.saveCertMailCode(receiver, code);

            publishMailInfo(senderDto, eventPublisherComponent::publishMailSuccessSendEvent);
        } catch (Exception ex) {
            processingExceptionMail(senderDto, ExceptionUtils.getStackTrace(ex));
        }
    }

    /**
     * 인증 메일 조회
     *
     * @param code 인증 코드
     */
    public CertMailDto getCertMail(String code) {
        Assert.notNull(code, ErrorMessage.INVALID_PARAM.name());

        final String mail = redisComponent.getCertMailThrowIfInvalid(code);
        return new CertMailDto(mail);
    }

    /**
     * 메일 변경 메일 발송
     *
     * @param senderDto 발송 객체
     */
    public void sendChangeMail(String accessToken, ChangeSenderDto senderDto) {
        Assert.notNull(accessToken, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(senderDto, ErrorMessage.INVALID_PARAM.name());

        senderDto.essentialInfo(SENDER, CERTIFICATION_SUBJECT);

        try {
            final String receiver = senderDto.getReceiver();
            final String code = RandomStringUtils.randomAlphanumeric(RANDOM_CERT_CODE_COUNT);

            senderDto.updateContents(code);
            amazonSimpleEmailService.sendEmail(senderDto.toSendEmailRequest());

            final CertInfoDto certInfoDto = new CertInfoDto(receiver, code);
            redisComponent.saveChangeMailInfo(accessToken, certInfoDto);

            publishMailInfo(senderDto, eventPublisherComponent::publishMailSuccessSendEvent);
        } catch (Exception ex) {
            processingExceptionMail(senderDto, ExceptionUtils.getStackTrace(ex));
        }
    }

    /* 메일 정보 발행 */
    private void publishMailInfo(SenderDto senderDto, Consumer<Mail> mailConsumer) {
        final Mail mail = senderDto.toEntity();
        mailConsumer.accept(mail);
    }

    /* 메일 에러 후 처리 */
    private void processingExceptionMail(SenderDto senderDto, String stackTrace) {
        publishMailInfo(senderDto, eventPublisherComponent::publishMailFailSendEvent);

        log.error(stackTrace);
        throw new CustomException(ErrorMessage.FAIL_SEND_MAIL);
    }

}
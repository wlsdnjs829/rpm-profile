package com.jinwon.rpm.profile.domain.mail.inner_dto;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.mail.Mail;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.jsonwebtoken.lang.Assert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.nio.charset.StandardCharsets;

@Getter
@Schema(description = "메일 발송 객체")
public class SenderDto {

    @NotNull
    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Schema(description = "수신자", example = "ljw0829@midasin.com")
    private String receiver;

    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Schema(description = "발신자", example = "ljw0829@midasin.com")
    private String sender;

    @Schema(description = "제목", example = "메일 제목")
    private String subject;

    @Schema(description = "내용", example = "메일 내용")
    private String contents;

    /**
     * AWS SendEmailRequest 변환
     */
    public SendEmailRequest toSendEmailRequest() {
        Assert.notNull(this.sender, ErrorMessage.FAIL_SEND_MAIL.name());
        Assert.notNull(this.subject, ErrorMessage.FAIL_SEND_MAIL.name());

        final Destination destination = new Destination()
                .withToAddresses(this.receiver);

        final Message message = new Message()
                .withSubject(createContent(this.subject))
                .withBody(new Body().withHtml(createContent(this.contents)));

        return new SendEmailRequest()
                .withSource(this.sender)
                .withDestination(destination)
                .withMessage(message);
    }

    /**
     * 필수 정보 저장
     *
     * @param sender 발신자
     * @param subject 제목
     */
    public void essentialInfo(String sender, String subject) {
        this.sender = sender;
        this.subject = subject;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    private Content createContent(String text) {
        return new Content()
                .withCharset(StandardCharsets.UTF_8.name())
                .withData(text);
    }

    public Mail toEntity() {
        return ModelMapperUtil.map(this, Mail.class);
    }

}

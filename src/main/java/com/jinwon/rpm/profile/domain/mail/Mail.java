package com.jinwon.rpm.profile.domain.mail;

import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.constants.enums.SendStatus;
import com.jinwon.rpm.profile.infra.converter.EncryptConverter;
import com.jinwon.rpm.profile.model.BaseEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 메일 Entity
 */
@Getter
@Entity
@Table(indexes = {
        @Index(name = "mail_index_001", columnList = "sender"),
        @Index(name = "mail_index_002", columnList = "receiver")
})
public class Mail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mailId;

    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Convert(converter = EncryptConverter.class)
    @Column(nullable = false, length = 350)
    private String sender;

    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Convert(converter = EncryptConverter.class)
    @Column(nullable = false, length = 350)
    private String receiver;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SendStatus sendStatus;

    private LocalDateTime readDateTime;

    public void successSend() {
        this.sendStatus = SendStatus.SUCCESS;
    }

    public void failSend() {
        this.sendStatus = SendStatus.FAIL;
    }

    public void essentialInfo(String sender, String subject, String contents) {
        this.sender = sender;
        this.subject = subject;
        this.contents = contents;
    }

}

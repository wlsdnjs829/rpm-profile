package com.jinwon.rpm.profile.domain.mail;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 메일 리스너
 */
@Component
@RequiredArgsConstructor
public class MailListener {

    private final MailRepository mailRepository;

    @Async
    @EventListener
    public void sendMailEvent(Mail mail) {
        Assert.notNull(mail, ErrorMessage.FAIL_SEND_MAIL.name());
        mailRepository.save(mail);
    }

}

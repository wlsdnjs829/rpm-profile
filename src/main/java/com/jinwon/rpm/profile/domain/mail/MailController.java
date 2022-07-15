package com.jinwon.rpm.profile.domain.mail;

import com.jinwon.rpm.profile.domain.mail.dto.CertMailDto;
import com.jinwon.rpm.profile.domain.mail.dto.CertSenderDto;
import com.jinwon.rpm.profile.domain.mail.dto.ChangeSenderDto;
import com.jinwon.rpm.profile.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "메일")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.PATH_V1 + "/mail")
public class MailController implements BaseController {

    private final MailService mailService;

    @PostMapping(value = "/cert")
    @Operation(summary = "인증 메일 발송")
    public void sendCertMail(@Valid @RequestBody CertSenderDto certSenderDto) {
        mailService.sendCertMail(certSenderDto);
    }

    @GetMapping(value = "/cert/{code}")
    @Operation(summary = "인증 메일 주소 조회")
    public CertMailDto getCertMail(@PathVariable String code) {
        return mailService.getCertMail(code);
    }

    @PostMapping(value = "/change-sub-mail")
    @Operation(summary = "이메일 변경 메일 발송")
    public void sendChangeMail(@NotNull Authentication authentication,
                               @Valid @RequestBody ChangeSenderDto changeSenderDto) {
        final String accessToken = getAccessTokenThrowIfNotExist(authentication);
        mailService.sendChangeMail(accessToken, changeSenderDto);
    }

}

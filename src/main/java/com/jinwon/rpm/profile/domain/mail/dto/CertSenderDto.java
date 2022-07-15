package com.jinwon.rpm.profile.domain.mail.dto;

import com.jinwon.rpm.profile.domain.mail.inner_dto.SenderDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "인증 메일 발송 객체")
public class CertSenderDto extends SenderDto {

    @NotNull
    @Schema(description = "응답 주소",
            example = "https://front-members.api-rpm-dv-midasitwebsolution.com/ko/certification?code=")
    private String returnUrl;

}

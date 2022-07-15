package com.jinwon.rpm.profile.domain.mail.dto;

import com.jinwon.rpm.profile.constants.RegexPattern;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "인증 메일 조회 객체")
public record CertMailDto(
        @Schema(description = "메일 주소", example = "ljw0829@midasin.com")
        @NotNull @Pattern(regexp = RegexPattern.EMAIL_REGEX) String mail
) {

}

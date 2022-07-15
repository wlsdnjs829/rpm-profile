package com.jinwon.rpm.profile.domain.member.dto;

import com.jinwon.rpm.profile.constants.RegexPattern;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "보조 이메일 변경 객체")
public record UpdateSubEmailDto(
        @NotNull @Pattern(regexp = RegexPattern.EMAIL_REGEX)
        @Schema(description = "이메일", required = true, example = "ljw0829@midasin.com") String subEmail,

        @NotNull @Schema(description = "인증 코드", required = true) String code
) {
}

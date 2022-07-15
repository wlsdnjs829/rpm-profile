package com.jinwon.rpm.profile.domain.mail.inner_dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "인증 정보 객체")
public record CertInfoDto(
        @Schema(description = "이메일", example = "ljw0829@midasin.com") @NotNull String email,
        @Schema(description = "인증 코드", example = "b3dfe5effs8efs3m4vds9emw") @NotNull String code

) {
}

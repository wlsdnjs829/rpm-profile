package com.jinwon.rpm.profile.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "JWT 토큰 송수신 객체")
public record JwtTokenDto(
        @NotNull @Schema(description = "인증 토큰", required = true) String token,
        @NotNull @Schema(description = "재사용 토큰", required = true) String refreshToken
) {
}

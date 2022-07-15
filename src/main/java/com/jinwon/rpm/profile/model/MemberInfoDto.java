package com.jinwon.rpm.profile.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "사용자 정보 객체")
public record MemberInfoDto(
        @NotNull @Schema(description = "사용자 ID") Long memberId,
        @NotNull @Schema(description = "JWT 토큰") String accessToken
) {
}

package com.jinwon.rpm.profile.domain.withdraw.inner_dto;

import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.domain.withdraw.Withdraw;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "사용자 탈퇴 사유 생성 객체")
public record PostWithdrawReasonDto(
        @Pattern(regexp = RegexPattern.EMAIL_REGEX) @Schema(description = "이메일", required = true) String email,
        @NotNull @Pattern(regexp = RegexPattern.NAME_REGEX) @Schema(description = "이름", required = true) String name,
        @Schema(description = "탈퇴 사유") String reason) {

    /**
     * 사용자 탈퇴 엔티티 변환
     *
     * @return 사용자 탈퇴
     */
    public Withdraw toEntity() {
        return ModelMapperUtil.map(this, Withdraw.class);
    }

}

package com.jinwon.rpm.profile.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.enums.WithdrawType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "프로필 삭제 Dto")
public class DeleteProfileDto extends CommonProfileDto {

    @NotNull
    @Schema(description = "탈퇴 타입")
    private WithdrawType type;

    @Schema(description = "탈퇴 사유")
    private String reason;

    @NotNull
    @Schema(description = "비밀 번호", required = true, example = "1q2w3e4r5t")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}

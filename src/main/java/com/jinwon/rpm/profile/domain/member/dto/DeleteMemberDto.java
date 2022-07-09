package com.jinwon.rpm.profile.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.enums.WithdrawType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Schema(description = "사용자 삭제 Dto")
public class DeleteMemberDto extends CommonMemberDto {

    @NotNull
    @Schema(description = "탈퇴 타입 리스트")
    private List<WithdrawType> withdrawTypes;

    @Schema(description = "탈퇴 사유")
    private String reason;

    @NotNull
    @Schema(description = "비밀 번호", required = true, example = "1q2w3e4r5t")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}

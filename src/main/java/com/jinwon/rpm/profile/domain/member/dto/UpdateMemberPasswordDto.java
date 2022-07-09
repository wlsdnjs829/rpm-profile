package com.jinwon.rpm.profile.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.infra.utils.AssertUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "사용자 비밀번호 전체 수정 Dto")
public class UpdateMemberPasswordDto extends CommonPasswordDto {

    @NotNull
    @Schema(description = "기존 비밀 번호", required = true, example = "1q2w3e4r5t")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldPassword;

    @Override
    public void validation() {
        AssertUtil.isFalse(StringUtils.equals(oldPassword, getPassword()), ErrorMessage.NOT_USE_OLD_PASSWORD.name());
        super.validation();
    }

}

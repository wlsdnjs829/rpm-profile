package com.jinwon.rpm.profile.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "프로필 비밀번호 전체 수정 Dto")
public class UpdateProfilePasswordDto extends CommonPasswordDto {

    @NotNull
    @Schema(description = "기존 비밀 번호", required = true, example = "1q2w3e4r5t")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldPassword;

    @Override
    public void validation() {
        Assert.isTrue(!StringUtils.equals(oldPassword, getPassword()), ErrorMessage.NOT_USE_OLD_PASSWORD.name());
        super.validation();
    }

}

package com.jinwon.rpm.profile.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jinwon.rpm.profile.constants.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.modelmapper.internal.util.Assert;

@Getter
@Schema(description = "공통 프로필 DTO")
public class CommonProfileDto {

    @JsonIgnore
    @Schema(description = "프로필 번호")
    private Long profileId;

    /**
     * 유저 필수 정보 저장
     *
     * @param profileId 프로필 아이디
     */
    public void userEssentialInfo(Long profileId) {
        Assert.notNull(profileId, ErrorMessage.NOT_EXIST_PROFILE.name());
        this.profileId = profileId;
    }

}

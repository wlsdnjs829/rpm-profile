package com.jinwon.rpm.profile.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.modelmapper.internal.util.Assert;

@Getter
@Schema(description = "공통 사용자 DTO")
public class CommonMemberDto {

    @JsonIgnore
    @Schema(description = "사용자 번호")
    private Long memberId;

    /**
     * 유저 필수 정보 저장
     *
     * @param memberId 사용자 아이디
     */
    public void userEssentialInfo(Long memberId) {
        Assert.notNull(memberId, ErrorMessage.NOT_EXIST_MEMBER.name());
        this.memberId = memberId;
    }

}

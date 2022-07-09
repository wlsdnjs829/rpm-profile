package com.jinwon.rpm.profile.domain.memeber_log.dto;

import com.jinwon.rpm.profile.constants.enums.MemberLogType;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.memeber_log.MemberLog;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 로그 객체")
public record MemberLogDto(
        @Schema(description = "이벤트 소스") Object source,
        @Schema(description = "사용자") Member member,
        @Schema(description = "사용자 로그 타입") MemberLogType memberLogType
) {

    /**
     * 사용자 로그 Entity 변환
     */
    public MemberLog toEntity() {
        return ModelMapperUtil.map(this, MemberLog.class);
    }

}

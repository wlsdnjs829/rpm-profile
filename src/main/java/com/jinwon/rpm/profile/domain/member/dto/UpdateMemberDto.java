package com.jinwon.rpm.profile.domain.member.dto;

import com.jinwon.rpm.profile.constants.enums.CountryCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "사용자 부분 수정 Dto")
public class UpdateMemberDto extends CommonMemberDto {

    @Schema(description = "사용자 이름")
    private String memberName;

    @Schema(description = "소속 회사")
    private String companyName;

    @Schema(description = "국가", required = true)
    private CountryCode country;

}

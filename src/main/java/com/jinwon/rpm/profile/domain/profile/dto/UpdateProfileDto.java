package com.jinwon.rpm.profile.domain.profile.dto;

import com.jinwon.rpm.profile.constants.enums.CountryCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "프로필 부분 수정 Dto")
public class UpdateProfileDto extends CommonProfileDto {

    @Schema(description = "프로필 이름")
    private String profileName;

    @Schema(description = "소속 회사")
    private String affiliatedCompany;

    @Schema(description = "국가", required = true)
    private CountryCode country;

}

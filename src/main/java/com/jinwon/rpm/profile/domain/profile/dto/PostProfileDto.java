package com.jinwon.rpm.profile.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.CountryCode;
import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.domain.profile.Profile;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "프로필 정보 생성 객체")
public class PostProfileDto extends CommonPasswordDto {

    @NotNull
    @Pattern(regexp = RegexPattern.NAME_REGEX)
    @Schema(description = "이름", required = true, example = "이진원")
    private String name;

    @Schema(description = "프로필 이름")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String profileName;

    @NotNull
    @Schema(description = "국가", required = true)
    private CountryCode country;

    @Schema(description = "회원 권한")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final List<String> roles = new ArrayList<>();

    /**
     * 프로필 엔티티 변환
     *
     * @return 프로필
     */
    public Profile toEntity() {
        this.profileName = name;
        return ModelMapperUtil.map(this, Profile.class);
    }

}

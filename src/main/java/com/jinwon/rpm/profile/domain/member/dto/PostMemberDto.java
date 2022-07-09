package com.jinwon.rpm.profile.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.constants.enums.CountryCode;
import com.jinwon.rpm.profile.constants.enums.MemberStatus;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "사용자 정보 생성 객체")
public class PostMemberDto extends CommonPasswordDto {

    @NotNull
    @Pattern(regexp = RegexPattern.NAME_REGEX)
    @Schema(description = "이름", required = true, example = "이진원")
    private String name;

    @Schema(description = "사용자 이름")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String memberName;

    @NotNull
    @Schema(description = "국가", required = true)
    private CountryCode country;

    @Schema(description = "사용자 권한")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final List<String> roles = new ArrayList<>();

    @NotNull
    @Schema(description = "동의 약관 타입 리스트")
    private List<TermsType> agreeTermsTypes;

    @Schema(description = "사용자 상태")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MemberStatus memberStatus;

    /**
     * 사용자 엔티티 변환
     *
     * @return 사용자
     */
    public Member toEntity() {
        this.memberName = name;
        this.memberStatus = MemberStatus.ACTIVATE;
        return ModelMapperUtil.map(this, Member.class);
    }

}

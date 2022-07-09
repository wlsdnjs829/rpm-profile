package com.jinwon.rpm.profile.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.enums.CountryCode;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "사용자 정보")
public class MemberDto {

    @JsonIgnore
    @Schema(description = "사용자 번호")
    private Long memberId;

    @Email
    @Schema(description = "이메일", required = true)
    private String email;

    @Schema(description = "보조 이메일")
    private String subEmail;

    @Schema(description = "휴대 번호")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
    private String phone;

    @Schema(description = "이름", required = true)
    private String name;

    @Schema(description = "사용자 이름")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String memberName;

    @Schema(description = "비밀 번호", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "재입력 비밀 번호", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String reTypePassword;

    @Schema(description = "소속 회사")
    private String companyName;

    @Schema(description = "국가", required = true)
    private CountryCode country;

    @Schema(description = "사용자 권한")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final List<GrantedAuthority> authorities = new ArrayList<>();

    public static MemberDto of(Member member) {
        return ModelMapperUtil.map(member, MemberDto.class);
    }

}

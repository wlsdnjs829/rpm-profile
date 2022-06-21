package com.jinwon.rpm.profile.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.enums.CountryCode;
import com.jinwon.rpm.profile.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "회원 정보 생성 객체")
public class PostUserDto {

    @Email
    @NotNull
    @Schema(description = "이메일", required = true)
    private String email;

    @NotNull
    @Max(value = 30)
    @Pattern(regexp = "/[^a-z|A-Z|ㄱ-ㅎ|가-힣]/g")
    @Schema(description = "이름", required = true)
    private String name;

    @Schema(description = "프로필 이름")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String profileName;

    @NotNull
    @Schema(description = "비밀 번호", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @Schema(description = "재입력 비밀 번호", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String reTypePassword;

    @NotNull
    @Schema(description = "국가", required = true)
    private CountryCode country;

    @Schema(description = "회원 권한")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final List<String> roles = new ArrayList<>();

    public void postUser() {
        this.profileName = name;
    }

    public static PostUserDto of(Profile profile) {
        return ModelMapperUtil.map(profile, PostUserDto.class);
    }

}

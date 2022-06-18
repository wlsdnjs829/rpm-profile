package com.jinwon.rpm.profile.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.enums.CountryCode;
import com.jinwon.rpm.profile.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "회원 정보")
public class UserDto {

    @Email
    @Schema(description = "이메일", required = true)
    private String email;

    @Schema(description = "보조 이메일")
    private String subEmail;

    @Schema(description = "휴대 번호")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
    private String phone;

    @NotNull
    @Schema(description = "이름", required = true)
    private String name;

    @Schema(description = "프로필 이름")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String profileName;

    @Schema(description = "비밀 번호", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "재입력 비밀 번호", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String reTypePassword;

    @Schema(description = "소속 회사")
    private String affiliatedCompany;

    @NotNull
    @Schema(description = "국가", required = true)
    private CountryCode country;

    @Schema(description = "회원 권한")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final List<String> roles = new ArrayList<>();

    public static UserDto of(User user) {
        return ModelMapperUtil.map(user, UserDto.class);
    }

}

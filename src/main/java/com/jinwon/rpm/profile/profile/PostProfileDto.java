package com.jinwon.rpm.profile.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.enums.CountryCode;
import com.jinwon.rpm.profile.enums.ErrorMessage;
import com.jinwon.rpm.profile.exception.CustomException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "프로필 정보 생성 객체")
public class PostProfileDto {

    @NotNull
    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Schema(description = "이메일", required = true)
    private String email;

    @NotNull
    @Pattern(regexp = RegexPattern.NAME_REGEX)
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

    /**
     * 프로필 생성, 조건에 맞지 않을 시 예외 처리
     */
    public void postProfileThrowIfInvalid() {
        if (!StringUtils.equals(password, reTypePassword)) {
            throw new CustomException(ErrorMessage.MISMATCH_PASSWORD);
        }

        this.profileName = name;
    }

}

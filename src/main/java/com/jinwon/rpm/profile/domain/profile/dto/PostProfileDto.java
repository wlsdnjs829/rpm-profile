package com.jinwon.rpm.profile.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.CountryCode;
import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.domain.profile.Profile;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.jinwon.rpm.profile.infra.validator.PasswordValidator.FullPasswordValidator;
import static com.jinwon.rpm.profile.infra.validator.PasswordValidator.PartPasswordValidator;

@Getter
@Schema(description = "프로필 정보 생성 객체")
public class PostProfileDto {

    private static final int ZERO = 0;
    private static final int TWO = 2;
    private static final int THREE = 3;

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

    /**
     * 유효성 검사
     */
    public void validation() {
        final PasswordDto passwordDto = new PasswordDto(password, email);
        FullPasswordValidator.validator(passwordDto);

        IntStream.range(ZERO, password.length() - TWO)
                .mapToObj(index -> {
                    final String threeLetters = password.substring(index, index + THREE);
                    return new PasswordDto(threeLetters);
                }).forEach(PartPasswordValidator::validator);

    }

    /**
     * 프로필 엔티티 변환
     *
     * @return 프로필
     */
    public Profile toEntity() {
        return ModelMapperUtil.map(this, Profile.class);
    }

}

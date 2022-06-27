package com.jinwon.rpm.profile.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.domain.profile.inner_dto.PasswordDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.stream.IntStream;

import static com.jinwon.rpm.profile.infra.validator.PasswordValidator.FullPasswordValidator;
import static com.jinwon.rpm.profile.infra.validator.PasswordValidator.PartPasswordValidator;

@Getter
@Schema(description = "패스워드 공통 Dto")
public class CommonPasswordDto extends CommonProfileDto {

    private static final int ZERO = 0;
    private static final int TWO = 2;
    private static final int THREE = 3;

    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Schema(description = "이메일", required = true, example = "ljw0829@midasin.com")
    private String email;

    @NotNull
    @Schema(description = "비밀 번호", required = true, example = "1q2w3e4r5t")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @Schema(description = "재입력 비밀 번호", required = true, example = "1q2w3e4r5t")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String reTypePassword;

    /**
     * 유효성 검사
     */
    public void validation() {
        Assert.isTrue(StringUtils.equals(password, reTypePassword), ErrorMessage.MISMATCH_PASSWORD.name());

        final PasswordDto passwordDto = new PasswordDto(password, email);
        FullPasswordValidator.validator(passwordDto);

        IntStream.range(ZERO, password.length() - TWO)
                .mapToObj(index -> {
                    final String threeLetters = password.substring(index, index + THREE);
                    return new PasswordDto(threeLetters);
                }).forEach(PartPasswordValidator::validator);
    }

    /**
     * 유저 필수 정보 저장
     *
     * @param profileId 프로필 ID
     * @param email     이메일
     */
    public void userEssentialInfo(Long profileId, String email) {
        Assert.notNull(email, ErrorMessage.EXIST_EMAIL.name());

        this.email = email;
        super.userEssentialInfo(profileId);
    }

}

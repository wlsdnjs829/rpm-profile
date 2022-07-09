package com.jinwon.rpm.profile.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "로그인 객체")
public class LoginDto {

    @NotNull
    @Schema(description = "아이디", required = true, example = "ljw0829@midasin.com")
    private String email;

    @NotNull
    @Schema(description = "고유 코드", required = true, example = "1q2w3e4r5t")
    private String password;

}

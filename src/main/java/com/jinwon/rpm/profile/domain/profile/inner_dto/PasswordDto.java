package com.jinwon.rpm.profile.domain.profile.inner_dto;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 패스워드 정보 Dto
 */
@Getter
public class PasswordDto {

    private final String password;
    private final String email;

    public PasswordDto(String password) {
        this.password = password;
        this.email = StringUtils.EMPTY;
    }

    public PasswordDto(String password, String email) {
        this.password = password;
        this.email = email;
    }

}

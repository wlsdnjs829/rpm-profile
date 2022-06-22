package com.jinwon.rpm.profile.domain.profile.dto;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

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

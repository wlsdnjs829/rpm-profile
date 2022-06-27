package com.jinwon.rpm.profile.infra.utils;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import lombok.experimental.UtilityClass;
import org.modelmapper.internal.util.Assert;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 비밀번호 암호화 유틸
 */
@UtilityClass
public class PasswordEncryptUtil {

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    /**
     * 평문 비밀번호 암호화
     *
     * @param password 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    public String encrypt(String password) {
        Assert.notNull(password, ErrorMessage.INVALID_PASSWORD.name());
        return passwordEncoder.encode(password);
    }

    /**
     * 일치 여부 조회
     *
     * @param inputPassword  입력받은 패스워드
     * @param encodePassword 인코딩 패스워드
     * @return 일치 여부
     */
    public boolean match(String inputPassword, String encodePassword) {
        Assert.notNull(inputPassword, ErrorMessage.INVALID_PASSWORD.name());
        Assert.notNull(encodePassword, ErrorMessage.INVALID_PASSWORD.name());
        return passwordEncoder.matches(inputPassword, encodePassword);
    }

}

package com.jinwon.rpm.profile.infra.converter;

import com.jinwon.rpm.profile.infra.utils.PasswordEncryptUtil;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;

/**
 * 패스워드 전용 컨버터
 */
@Component
public class PasswordConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return PasswordEncryptUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }

}

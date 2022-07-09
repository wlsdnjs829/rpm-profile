package com.jinwon.rpm.profile.infra.utils;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

import java.util.function.Supplier;

/**
 * Assert Util
 */
@UtilityClass
public class AssertUtil {

    /**
     * 참이 맞는 조건 여부 확인
     *
     * @param expression 조건
     * @param supplier 에러 공급
     */
    public void isTrue(boolean expression, Supplier<String> supplier) {
        Assert.notNull(supplier, ErrorMessage.INVALID_SUPPLIER.name());

        if (!expression) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 거짓이 맞는 조건 여부 확인
     *
     * @param expression 조건
     * @param supplier 에러 공급
     */
    public void isFalse(boolean expression, Supplier<String> supplier) {
        Assert.notNull(supplier, ErrorMessage.INVALID_SUPPLIER.name());

        if (expression) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 거짓이 맞는 조건 여부 확인
     *
     * @param expression 조건
     * @param message 메시지
     */
    public void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

}

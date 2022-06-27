package com.jinwon.rpm.profile.infra.exception;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀 Exception
 *
 * Enum::name / Enum::code / Enum::status
 *
 */
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorMessage errorMessage;

}

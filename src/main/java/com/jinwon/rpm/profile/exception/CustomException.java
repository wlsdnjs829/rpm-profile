package com.jinwon.rpm.profile.exception;

import com.jinwon.rpm.profile.enums.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorMessage errorMessage;

}

package com.jinwon.rpm.profile.infra.adviser;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.exception.EncryptionException;
import com.jinwon.rpm.profile.model.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 컨트롤러 예외 핸들러
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 잡지 않은 모든 Exception 예외 처리
     *
     * @param e 모든 예외
     * @return 응답 상태 값에 따른 예외 반환
     */
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorDto> exceptionHandler(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));

        return new ResponseEntity<>(
                new ErrorDto(INTERNAL_SERVER_ERROR, e.getMessage()), INTERNAL_SERVER_ERROR);
    }

    /**
     * 메시지 Readable Exception 예외 처리
     *
     * @param e 메시지 Readable 에러
     * @return 응답 상태 값에 따른 예외 반환
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorDto> messageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.error(ExceptionUtils.getStackTrace(e));

        return new ResponseEntity<>(
                new ErrorDto(BAD_REQUEST, e.getMessage()), BAD_REQUEST);
    }

    /**
     * 암복호화 Exception 예외 처리
     *
     * @param e 암복호화 예외
     * @return 암복호화 값에 다른 공통 예외 반환
     */
    @ExceptionHandler(value = EncryptionException.class)
    protected ResponseEntity<ErrorDto> encryptionExceptionHandler(EncryptionException e) {
        log.error(ExceptionUtils.getStackTrace(e));

        return new ResponseEntity<>(
                new ErrorDto(INTERNAL_SERVER_ERROR, e.getDefaultMessage()), INTERNAL_SERVER_ERROR);
    }

    /**
     * IllegalArgumentException 예외 처리
     *
     * @param e IllegalArgument 예외
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<ErrorDto> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error(ExceptionUtils.getStackTrace(e));

        final String message = e.getMessage();
        final ErrorMessage errorMessage = ErrorMessage.getErrorMessageByName(message);

        if (Objects.isNull(errorMessage)) {
            return new ResponseEntity<>(
                    new ErrorDto(BAD_REQUEST, e.getMessage()), BAD_REQUEST);
        }

        final HttpStatus httpStatus = errorMessage.getHttpStatus();

        return new ResponseEntity<>(
                new ErrorDto(httpStatus, errorMessage.name()), httpStatus);
    }

    /**
     * MethodArgumentNotValidException 예외 처리
     *
     * @param e MethodArgumentNotValid 예외
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorDto> notValidArgExceptionHandler(MethodArgumentNotValidException e) {
        log.error(ExceptionUtils.getStackTrace(e));

        final FieldError fieldError = Optional.of(e)
                .map(BindException::getBindingResult)
                .map(Errors::getFieldError)
                .stream()
                .findFirst()
                .orElse(null);

        final Optional<FieldError> fieldErrorOp = Optional.ofNullable(fieldError);

        final String field = fieldErrorOp.map(FieldError::getField)
                .orElse(EMPTY);

        final String defaultMessage = fieldErrorOp.map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse(EMPTY);

        return new ResponseEntity<>(
                new ErrorDto(BAD_REQUEST, field + SPACE + defaultMessage), BAD_REQUEST);
    }

    /**
     * 커스텀 예외 처리
     *
     * @param ce 커스텀 예외
     * @return 커스텀 상태 값에 따른 예외 반환
     */
    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<ErrorDto> customExceptionHandler(CustomException ce) {
        log.error(ExceptionUtils.getStackTrace(ce));

        final Optional<ErrorMessage> tokenMessageOp = Optional.of(ce)
                .map(CustomException::getErrorMessage);

        final HttpStatus httpStatus = tokenMessageOp.map(ErrorMessage::getHttpStatus)
                .orElse(INTERNAL_SERVER_ERROR);

        final String message = tokenMessageOp.map(Enum::name)
                .orElse(EMPTY);

        return new ResponseEntity<>(
                new ErrorDto(httpStatus,  message), httpStatus);
    }

}

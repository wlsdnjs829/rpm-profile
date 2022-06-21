package com.jinwon.rpm.profile.base;

import com.jinwon.rpm.profile.enums.ErrorMessage;
import com.jinwon.rpm.profile.exception.CustomException;
import com.jinwon.rpm.profile.user.User;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface BaseController {

    /**
     * 회원 정보 반환
     *
     * @param authentication 인증 정보
     */
    @SuppressWarnings("unchecked")
    public default Optional<User> getUserOp(@NotNull Authentication authentication) {
        return (Optional<User>) authentication.getPrincipal();
    }

    public default User getUserThrowIfNotExist(@NotNull Authentication authentication) {
        return getUserOp(authentication).orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_USER));
    }

}

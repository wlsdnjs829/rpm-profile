package com.jinwon.rpm.profile.base;

import com.jinwon.rpm.profile.user.User;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public abstract class BaseController {

    /**
     * 회원 정보 반환
     *
     * @param authentication 인증 정보
     */
    @SuppressWarnings("unchecked")
    public Optional<User> getUser(@NotNull Authentication authentication) {
        return (Optional<User>) authentication.getPrincipal();
    }

}

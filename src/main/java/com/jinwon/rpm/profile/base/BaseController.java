package com.jinwon.rpm.profile.base;

import com.jinwon.rpm.profile.enums.ErrorMessage;
import com.jinwon.rpm.profile.exception.CustomException;
import com.jinwon.rpm.profile.profile.Profile;
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
    default Optional<Profile> getProfileOp(@NotNull Authentication authentication) {
        return (Optional<Profile>) authentication.getPrincipal();
    }

    /**
     * 유효한 회원 정보 반환
     *
     * @param authentication 인증 정보
     */
    default Profile getUserThrowIfNotExist(@NotNull Authentication authentication) {
        return getProfileOp(authentication).orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE));
    }

    /**
     * 유효한 회원 번호 반환
     *
     * @param authentication 인증 정보
     */
    default Long getUserIdThrowIfNotExist(@NotNull Authentication authentication) {
        return getProfileOp(authentication).map(Profile::getProfileId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE));
    }

}

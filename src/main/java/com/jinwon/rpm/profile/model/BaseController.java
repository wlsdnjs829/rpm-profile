package com.jinwon.rpm.profile.model;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.domain.profile.Profile;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface BaseController {

    /**
     * 프로필 정보 반환
     *
     * @param authentication 인증 정보
     */
    @SuppressWarnings("unchecked")
    default Optional<Profile> getProfileOp(@NotNull Authentication authentication) {
        return (Optional<Profile>) authentication.getPrincipal();
    }

    /**
     * 유효한 프로필 번호 반환
     *
     * @param authentication 인증 정보
     */
    default Long getProfileIdThrowIfNotExist(@NotNull Authentication authentication) {
        return getProfileOp(authentication).map(Profile::getProfileId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE));
    }

}

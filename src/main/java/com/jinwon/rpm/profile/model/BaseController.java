package com.jinwon.rpm.profile.model;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * 기본 컨트롤러, 유저 & 세션 등 공통 관리 로직 추가
 */
public interface BaseController {

    String PATH_V1 = "/v1";

    /**
     * 사용자 정보 반환
     *
     * @param authentication 인증 정보
     */
    @SuppressWarnings("unchecked")
    default Optional<Member> getMemberOp(@NotNull Authentication authentication) {
        return (Optional<Member>) authentication.getPrincipal();
    }

    /**
     * 유효한 사용자 번호 반환
     *
     * @param authentication 인증 정보
     */
    default Long getMemberIdThrowIfNotExist(@NotNull Authentication authentication) {
        return getMemberOp(authentication).map(Member::getMemberId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_MEMBER));
    }

}

package com.jinwon.rpm.profile.model;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Supplier;

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
     * JWT 토큰 반환
     *
     * @param authentication 인증 정보
     */
    default String getAccessTokenThrowIfNotExist(@NotNull Authentication authentication) {
        return getMemberOp(authentication).map(Member::getAccessToken)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_MEMBER));
    }

    /**
     * 사용자 정보 객체 반환
     *
     * @param authentication 인증 정보
     */
    default MemberInfoDto getMemberInfoThrowIfNotExist(@NotNull Authentication authentication) {
        final Optional<Member> memberOp = getMemberOp(authentication);

        final Supplier<CustomException> exceptionSupplier = () -> new CustomException(ErrorMessage.NOT_EXIST_MEMBER);

        final Long memberId = memberOp.map(Member::getMemberId)
                .orElseThrow(exceptionSupplier);

        final String accessToken = memberOp.map(Member::getAccessToken)
                .orElseThrow(exceptionSupplier);

        return new MemberInfoDto(memberId, accessToken);
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

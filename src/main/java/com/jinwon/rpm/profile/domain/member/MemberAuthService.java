package com.jinwon.rpm.profile.domain.member;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.member.dto.JwtTokenDto;
import com.jinwon.rpm.profile.domain.member.dto.LoginDto;
import com.jinwon.rpm.profile.infra.component.EventPublisherComponent;
import com.jinwon.rpm.profile.infra.component.RedisComponent;
import com.jinwon.rpm.profile.infra.config.jwt.JwtTokenProvider;
import com.jinwon.rpm.profile.infra.config.security.CustomUserDetailService;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.utils.AssertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 권한 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberAuthService {

    private final CustomUserDetailService userDetailService;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisComponent redisComponent;
    private final EventPublisherComponent eventPublisherComponent;

    /**
     * 사용자 로그인 토큰 발급
     *
     * @param loginDto 로그인 객체
     * @param clientIp 사용자 IP
     * @return JWT Token 객체
     */
    public JwtTokenDto login(LoginDto loginDto, String clientIp) {
        final Member member = userDetailService.validMemberThrowIfInvalid(loginDto.getEmail(), loginDto.getPassword());
        final Member loginMember = addMemberInfo(member, clientIp);
        
        eventPublisherComponent.publishLoginEvent(member);
        
        return new JwtTokenDto(loginMember.getAccessToken(), loginMember.getRefreshToken());
    }

    /**
     * 사용자 토큰 재사용 요청
     *
     * @param jwtTokenDto JWT Token 객체
     * @param clientIp 사용자 IP
     * @return JWT Token 객체
     */
    public JwtTokenDto refreshToken(JwtTokenDto jwtTokenDto, String clientIp) {
        final String expiredAccessToken = jwtTokenDto.token();
        final String expiredRefreshToken = jwtTokenDto.refreshToken();

        deleteAccessTokenThrowIfInvalid(expiredAccessToken);

        final Member refreshMember = redisComponent.getTokenMember(expiredRefreshToken)
                .orElseThrow(() -> new CustomException(ErrorMessage.JWT_EXPIRED_REFRESH_TOKEN));

        redisComponent.deleteValues(expiredRefreshToken);

        final Member member = addMemberInfo(refreshMember, clientIp);

        eventPublisherComponent.publishRefreshEvent(member);

        return new JwtTokenDto(member.getAccessToken(), member.getRefreshToken());
    }

    /* 사용자 토큰 삭제, 유효 하지 않을 시 예외 처리 */
    private void deleteAccessTokenThrowIfInvalid(String expiredAccessToken) {
        final boolean validateToken = jwtTokenProvider.validateToken(expiredAccessToken);
        AssertUtil.isFalse(validateToken, ErrorMessage.JWT_NON_EXPIRED.name());

        redisComponent.deleteValues(expiredAccessToken);
    }

    /* 사용자 추가 정보 저장 */
    private Member addMemberInfo(Member member, String clientIp) {
        final String token = jwtTokenProvider.generateToken(member);
        final String refreshToken = jwtTokenProvider.generateRefreshToken();

        final Member loginMember = member.accessToken(token)
                .refreshToken(refreshToken)
                .clientIp(clientIp);

        redisComponent.addAccessToken(token, loginMember);
        redisComponent.addRefreshToken(refreshToken, loginMember);
        return member;
    }

    /**
     * 사용자 로그아웃
     *
     * @param accessToken 액세스 토큰
     * @param refreshToken 리프레쉬 토큰
     * @return 성공 여부
     */
    public Boolean logout(Member member) {
        eventPublisherComponent.publishLogoutEvent(member);

        final String accessToken = member.getAccessToken();
        final String refreshToken = member.getRefreshToken();
        return redisComponent.deleteValues(accessToken) && redisComponent.deleteValues(refreshToken);
    }

}


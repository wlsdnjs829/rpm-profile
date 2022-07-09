package com.jinwon.rpm.profile.infra.config.security;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.MemberLogType;
import com.jinwon.rpm.profile.constants.enums.MemberStatus;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.member.MemberRepository;
import com.jinwon.rpm.profile.domain.memeber_log.MemberLog;
import com.jinwon.rpm.profile.domain.memeber_log.MemberLogRepository;
import com.jinwon.rpm.profile.infra.component.EventPublisherComponent;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.utils.AssertUtil;
import com.jinwon.rpm.profile.infra.utils.PasswordEncryptUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 사용자 인증 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberLogRepository memberLogRepository;

    private final EventPublisherComponent eventPublisherComponent;

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    private static final int PAGE = 0;
    private static final int SIZE = 5;

    @Override
    public Member loadUserByUsername(String name) {
        final Member member = memberRepository.findByEmail(name)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_MEMBER));

        detailsChecker.check(member);
        return member;
    }

    /**
     * 유효한 사용자 반환, 유효하지 않을 시 예외 처리
     *
     * @param email    이메일
     * @param password 개인 코드
     */
    public Member validMemberThrowIfInvalid(String email, String password) {
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_MEMBER));

        final MemberStatus memberStatus = member.getMemberStatus();
        Assert.isTrue(MemberStatus.ACTIVATE.equals(memberStatus), ErrorMessage.DISABLE_MEMBER.name());

        final boolean memberLock = isMemberLock(member);
        AssertUtil.isFalse(memberLock, () -> getPublishMemberLockEvent(member));

        final String savedPassword = member.getPassword();
        AssertUtil.isTrue(PasswordEncryptUtil.match(password, savedPassword), () -> publishFailEvent(member));

        return member;
    }

    /* 사용자 잠긴 상태 여부 조회 */
    private boolean isMemberLock(Member member) {
        final PageRequest pageable = PageRequest.of(PAGE, SIZE);
        final List<MemberLog> memberLogs = memberLogRepository.findByMemberOrderByMemberLogIdDesc(member, pageable);

        final boolean isLocked = memberLogs.stream()
                .map(MemberLog::getMemberLogType)
                .allMatch(MemberLogType.FAIL::equals);

        return memberLogs.size() == SIZE && isLocked;
    }

    /* 사용자 잠금 이벤트 공급 */
    private String getPublishMemberLockEvent(Member member) {
        eventPublisherComponent.publishMemberLockEvent(member);
        return ErrorMessage.LOCKED_MEMBER.name();
    }

    /* 사용자 로그인 실패 이벤트 공급 */
    private String publishFailEvent(Member member) {
        eventPublisherComponent.publishFailEvent(member);
        return ErrorMessage.INVALID_PASSWORD.name();
    }

}

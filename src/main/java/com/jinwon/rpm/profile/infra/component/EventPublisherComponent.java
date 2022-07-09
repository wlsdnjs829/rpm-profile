package com.jinwon.rpm.profile.infra.component;

import com.jinwon.rpm.profile.constants.enums.MemberLogType;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.memeber_log.dto.MemberLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 이벤트 발행 Component
 */
@Component
@RequiredArgsConstructor
public class EventPublisherComponent {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 사용자 로그인 이벤트 발행
     *
     * @param member 사용자
     */
    public void publishLoginEvent(Member member) {
        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.LOGIN);
        applicationEventPublisher.publishEvent(memberLogDto);
    }

    /**
     * 사용자 로그아웃 이벤트 발행
     *
     * @param member 사용자
     */
    public void publishLogoutEvent(Member member) {
        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.LOGOUT);
        applicationEventPublisher.publishEvent(memberLogDto);
    }

    /**
     * 사용자 토큰 재발급 이벤트 발행
     *
     * @param member 사용자
     */
    public void publishRefreshEvent(Member member) {
        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.REFRESH_TOKEN);
        applicationEventPublisher.publishEvent(memberLogDto);
    }

    /**
     * 사용자 로그인 실패 이벤트 발행
     *
     * @param member 사용자
     */
    public void publishFailEvent(Member member) {
        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.FAIL);
        applicationEventPublisher.publishEvent(memberLogDto);
    }

    /**
     * 사용자 잠금 이벤트 발행
     *
     * @param member 사용자
     */
    public void publishMemberLockEvent(Member member) {
        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.LOCK);
        applicationEventPublisher.publishEvent(memberLogDto);

        member.lock();
        applicationEventPublisher.publishEvent(member);
    }

}
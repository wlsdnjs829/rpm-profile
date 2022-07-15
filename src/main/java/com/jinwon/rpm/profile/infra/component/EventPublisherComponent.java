package com.jinwon.rpm.profile.infra.component;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.MemberLogType;
import com.jinwon.rpm.profile.domain.mail.Mail;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.memeber_log.dto.MemberLogDto;
import io.jsonwebtoken.lang.Assert;
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
        Assert.notNull(member, ErrorMessage.INVALID_PARAM.name());

        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.LOGIN);
        applicationEventPublisher.publishEvent(memberLogDto);
    }

    /**
     * 사용자 로그아웃 이벤트 발행
     *
     * @param member 사용자
     */
    public void publishLogoutEvent(Member member) {
        Assert.notNull(member, ErrorMessage.INVALID_PARAM.name());

        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.LOGOUT);
        applicationEventPublisher.publishEvent(memberLogDto);
    }

    /**
     * 사용자 토큰 재발급 이벤트 발행
     *
     * @param member 사용자
     */
    public void publishRefreshEvent(Member member) {
        Assert.notNull(member, ErrorMessage.INVALID_PARAM.name());

        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.REFRESH_TOKEN);
        applicationEventPublisher.publishEvent(memberLogDto);
    }

    /**
     * 사용자 로그인 실패 이벤트 발행
     *
     * @param member 사용자
     */
    public void publishFailEvent(Member member) {
        Assert.notNull(member, ErrorMessage.INVALID_PARAM.name());

        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.FAIL);
        applicationEventPublisher.publishEvent(memberLogDto);
    }

    /**
     * 사용자 잠금 이벤트 발행
     *
     * @param member 사용자
     */
    public void publishMemberLockEvent(Member member) {
        Assert.notNull(member, ErrorMessage.INVALID_PARAM.name());

        final MemberLogDto memberLogDto = new MemberLogDto(this, member, MemberLogType.LOCK);
        applicationEventPublisher.publishEvent(memberLogDto);

        member.lock();
        applicationEventPublisher.publishEvent(member);
    }

    /**
     * 메일 성공 이벤트 발행
     *
     * @param mail 메일
     */
    public void publishMailSuccessSendEvent(Mail mail) {
        Assert.notNull(mail, ErrorMessage.INVALID_PARAM.name());

        mail.successSend();
        applicationEventPublisher.publishEvent(mail);
    }

    /**
     * 메일 실패 이벤트 발행
     *
     * @param mail 메일
     */
    public void publishMailFailSendEvent(Mail mail) {
        Assert.notNull(mail, ErrorMessage.INVALID_PARAM.name());

        mail.failSend();
        applicationEventPublisher.publishEvent(mail);
    }

}
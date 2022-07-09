package com.jinwon.rpm.profile.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 사용자 리스너
 */
@Component
@RequiredArgsConstructor
public class MemberListener {

    private final MemberRepository memberRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void updateMemberEvent(Member member) {
        memberRepository.save(member);
    }

}

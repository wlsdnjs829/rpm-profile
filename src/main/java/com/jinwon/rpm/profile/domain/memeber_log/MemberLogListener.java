package com.jinwon.rpm.profile.domain.memeber_log;

import com.jinwon.rpm.profile.domain.memeber_log.dto.MemberLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 사용자 로그 리스너
 */
@Component
@RequiredArgsConstructor
public class MemberLogListener {

    private final MemberLogRepository memberLogRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void logEvent(MemberLogDto memberLogDto) {
        final MemberLog memberLog = memberLogDto.toEntity();
        memberLogRepository.save(memberLog);
    }

}

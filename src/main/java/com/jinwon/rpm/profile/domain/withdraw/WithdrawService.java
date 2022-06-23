package com.jinwon.rpm.profile.domain.withdraw;

import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;

    /**
     * 회원 탈퇴 사유 등록
     *
     * @param postWithdrawReasonDto 회원 탈퇴 사유 등록 DTO
     */
    public Withdraw postWithdrawReason(@NotNull PostWithdrawReasonDto postWithdrawReasonDto) {
        final Withdraw withdraw = postWithdrawReasonDto.toEntity();
        return withdrawRepository.save(withdraw);
    }

}


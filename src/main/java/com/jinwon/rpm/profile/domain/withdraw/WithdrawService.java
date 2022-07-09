package com.jinwon.rpm.profile.domain.withdraw;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.WithdrawType;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.member.dto.DeleteMemberDto;
import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 탈퇴 관련 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;

    /**
     * 사용자 탈퇴 사유 등록
     *
     * @param deleteMemberDto 삭제 대상 DTO
     * @param member          사용자 정보
     * @return 사용자 탈퇴 사유
     */
    public PostWithdrawReasonDto postWithdrawReason(DeleteMemberDto deleteMemberDto, Member member) {
        Assert.notNull(member, ErrorMessage.NOT_EXIST_MEMBER.name());
        Assert.notNull(deleteMemberDto, ErrorMessage.INVALID_PARAM.name());

        final String email = member.getEmail();
        final String name = member.getName();
        final String reason = deleteMemberDto.getReason();

        final PostWithdrawReasonDto postWithdrawReasonDto = new PostWithdrawReasonDto(email, name, reason);
        final List<WithdrawType> types = deleteMemberDto.getWithdrawTypes();
        Assert.notNull(postWithdrawReason(postWithdrawReasonDto, types), ErrorMessage.FAIL_POST_WITHDRAW.name());
        return postWithdrawReasonDto;
    }

    /* 사용자 탈퇴 사유 등록 */
    private Withdraw postWithdrawReason(PostWithdrawReasonDto postWithdrawReasonDto, List<WithdrawType> withdrawTypes) {
        final Withdraw withdraw = postWithdrawReasonDto.toEntity();
        final Withdraw savedWithdraw = withdrawRepository.save(withdraw);

        withdrawTypes.stream()
                .map(WithdrawReason::new)
                .forEach(savedWithdraw::registration);

        return savedWithdraw;
    }

}


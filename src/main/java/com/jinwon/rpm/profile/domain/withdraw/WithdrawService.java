package com.jinwon.rpm.profile.domain.withdraw;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.WithdrawType;
import com.jinwon.rpm.profile.domain.profile.Profile;
import com.jinwon.rpm.profile.domain.profile.dto.DeleteProfileDto;
import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * 회원 탈퇴 관련 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;

    /**
     * 프로필 탈퇴 사유 등록
     *
     * @param deleteProfileDto 삭제 대상 DTO
     * @param profile          프로필 정보
     * @return 프로필 탈퇴 사유
     */
    public PostWithdrawReasonDto postWithdrawReason(DeleteProfileDto deleteProfileDto, Profile profile) {
        Assert.notNull(profile, ErrorMessage.NOT_EXIST_PROFILE.name());
        Assert.notNull(deleteProfileDto, ErrorMessage.INVALID_PARAM.name());

        final String email = profile.getEmail();
        final String name = profile.getName();
        final WithdrawType type = deleteProfileDto.getType();
        final String reason = deleteProfileDto.getReason();

        final PostWithdrawReasonDto postWithdrawReasonDto = new PostWithdrawReasonDto(email, name, type, reason);
        Assert.notNull(postWithdrawReason(postWithdrawReasonDto), ErrorMessage.FAIL_POST_WITHDRAW.name());
        return postWithdrawReasonDto;
    }

    /* 프로필 탈퇴 사유 등록 */
    private Withdraw postWithdrawReason(@NotNull PostWithdrawReasonDto postWithdrawReasonDto) {
        final Withdraw withdraw = postWithdrawReasonDto.toEntity();
        return withdrawRepository.save(withdraw);
    }

}


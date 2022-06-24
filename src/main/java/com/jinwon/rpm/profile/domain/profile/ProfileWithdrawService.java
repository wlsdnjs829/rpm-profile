package com.jinwon.rpm.profile.domain.profile;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.WithdrawType;
import com.jinwon.rpm.profile.domain.profile.dto.DeleteProfileDto;
import com.jinwon.rpm.profile.domain.terms.TermsAgreementRepository;
import com.jinwon.rpm.profile.domain.withdraw.Withdraw;
import com.jinwon.rpm.profile.domain.withdraw.WithdrawRepository;
import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.utils.PasswordEncryptUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileWithdrawService {

    private final ProfileRepository profileRepository;
    private final WithdrawRepository withdrawRepository;
    private final TermsAgreementRepository termsAgreementRepository;

    /**
     * 프로필 삭제
     *
     * @param deleteProfileDto 프로필 삭제 DTO
     * @return 회원 탈퇴 사유
     */
    public PostWithdrawReasonDto deleteProfile(@NotNull DeleteProfileDto deleteProfileDto) {
        final Long profileId = deleteProfileDto.getProfileId();

        final Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE));

        final String inputPassword = deleteProfileDto.getPassword();
        final String encodePassword = profile.getPassword();
        Assert.isTrue(PasswordEncryptUtil.match(inputPassword, encodePassword), ErrorMessage.MISMATCH_PASSWORD.name());

        termsAgreementRepository.deleteByProfile(profile);
        profileRepository.delete(profile);
        return postWithdrawReason(deleteProfileDto, profile);
    }

    /* 회원 탈퇴 사유 등록 */
    private PostWithdrawReasonDto postWithdrawReason(DeleteProfileDto deleteProfileDto, Profile profile) {
        final String email = profile.getEmail();
        final String name = profile.getName();
        final WithdrawType type = deleteProfileDto.getType();
        final String reason = deleteProfileDto.getReason();

        final PostWithdrawReasonDto postWithdrawReasonDto = new PostWithdrawReasonDto(email, name, type, reason);
        Assert.notNull(postWithdrawReason(postWithdrawReasonDto), ErrorMessage.FAIL_POST_WITHDRAW.name());
        return postWithdrawReasonDto;
    }

    /* 회원 탈퇴 사유 등록 */
    private Withdraw postWithdrawReason(@NotNull PostWithdrawReasonDto postWithdrawReasonDto) {
        final Withdraw withdraw = postWithdrawReasonDto.toEntity();
        return withdrawRepository.save(withdraw);
    }

}


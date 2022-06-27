package com.jinwon.rpm.profile.domain.profile;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.RoleType;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.profile.dto.CommonProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.DeleteProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.PostProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.ProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.TermsAgreementDto;
import com.jinwon.rpm.profile.domain.profile.dto.UpdateProfilePasswordDto;
import com.jinwon.rpm.profile.domain.role.Role;
import com.jinwon.rpm.profile.domain.terms.Terms;
import com.jinwon.rpm.profile.domain.terms.TermsService;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import com.jinwon.rpm.profile.domain.terms_agreement.TermsAgreementService;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.PutTermsAgreementDto;
import com.jinwon.rpm.profile.domain.withdraw.WithdrawService;
import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.utils.PasswordEncryptUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final TermsService termsService;
    private final WithdrawService withdrawService;
    private final TermsAgreementService termsAgreementService;

    private final ProfileRepository profileRepository;

    /**
     * 프로필 생성
     *
     * @param postProfileDto 프로필 생성 객체
     * @return 생성된 프로필 정보
     */
    public ProfileDto postProfile(@NotNull PostProfileDto postProfileDto) {
        postProfileDto.validation();

        final String email = postProfileDto.getEmail();
        final Optional<Profile> existProfile = profileRepository.findByEmail(email);
        Assert.isTrue(existProfile.isEmpty(), ErrorMessage.EXIST_EMAIL.name());

        final Profile profile = postProfileDto.toEntity();
        final Profile savedProfile = profileRepository.save(profile);

        final Role role = new Role(RoleType.USER);
        savedProfile.grantRoles(role);

        return ProfileDto.of(savedProfile);
    }

    /**
     * 프로필 부분 수정
     *
     * @param commonProfileDto 공통 프로필 부분 수정 DTO
     * @return 수정된 프로필 정보
     */
    public ProfileDto patchProfile(@NotNull CommonProfileDto commonProfileDto) {
        final Profile profile = getProfileThrowIfNull(commonProfileDto.getProfileId());

        final Profile patchProfile = profile.patch(commonProfileDto);
        return ProfileDto.of(patchProfile);
    }

    /**
     * 프로필 전체 수정
     *
     * @param commonProfileDto 공통 프로필 전체 수정 DTO
     * @return 수정된 프로필 정보
     */
    public ProfileDto putProfile(@NotNull CommonProfileDto commonProfileDto) {
        final Profile profile = getProfileThrowIfNull(commonProfileDto.getProfileId());

        final Profile patchProfile = profile.put(commonProfileDto);
        return ProfileDto.of(patchProfile);
    }

    /* 프로필 조회, 없을 시 예외 처리 */
    private Profile getProfileThrowIfNull(@NotNull Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE));
    }

    /**
     * 프로필 패스워드 수정
     *
     * @param updateProfilePasswordDto 프로필 패스워드 수정 DTO
     * @return 수정된 프로필 정보
     */
    public ProfileDto patchProfilePassword(@NotNull UpdateProfilePasswordDto updateProfilePasswordDto) {
        final Long profileId = updateProfilePasswordDto.getProfileId();

        final Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE));

        final String encodePassword = profile.getPassword();
        final String oldPassword = updateProfilePasswordDto.getOldPassword();
        Assert.isTrue(PasswordEncryptUtil.match(oldPassword, encodePassword), ErrorMessage.MISMATCH_PASSWORD.name());

        updateProfilePasswordDto.validation();

        final Profile patchProfile = profile.patch(updateProfilePasswordDto);
        return ProfileDto.of(patchProfile);
    }

    /**
     * 프로필 삭제
     *
     * @param deleteProfileDto 프로필 삭제 DTO
     * @return 회원 탈퇴 사유
     */
    public PostWithdrawReasonDto deleteProfile(@NotNull DeleteProfileDto deleteProfileDto) {
        final Long profileId = deleteProfileDto.getProfileId();
        final Profile profile = getProfileThrowIfNull(profileId);

        final String inputPassword = deleteProfileDto.getPassword();
        final String encodePassword = profile.getPassword();
        Assert.isTrue(PasswordEncryptUtil.match(inputPassword, encodePassword), ErrorMessage.MISMATCH_PASSWORD.name());

        termsAgreementService.deleteByProfile(profile);
        profileRepository.delete(profile);
        return withdrawService.postWithdrawReason(deleteProfileDto, profile);
    }

    /**
     * 동의 여부 조회
     *
     * @param profileId 프로필 아이디
     * @param type      동의서 타입
     */
    public TermsAgreementDto getTermsAgreement(@NotNull Long profileId, @NotNull TermsType type) {
        Assert.notNull(profileId, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(type, ErrorMessage.INVALID_PARAM.name());

        final Profile profile = getProfileThrowIfNull(profileId);

        final Terms terms = termsService.getTermsByType(type);
        final UseType agreeType = termsAgreementService.getTermsAgreementAgreeType(profile, terms);

        final TermsDetailDto termsDetailDto = TermsDetailDto.of(terms);
        return new TermsAgreementDto(termsDetailDto, agreeType);
    }

    /**
     * 프로필 마케팅 동의 생성 / 수정
     *
     * @param profileId 프로필 아이디
     * @param agreeType 동의 타입
     * @return 동의한 프로필 DTO
     */
    public TermsAgreementDto putMarketingAgreement(@NotNull Long profileId, @NotNull UseType agreeType) {
        Assert.notNull(profileId, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(agreeType, ErrorMessage.INVALID_PARAM.name());

        final Profile profile = getProfileThrowIfNull(profileId);
        final Terms terms = termsService.getTermsByType(TermsType.RECEIVE_MARKETING_INFO);

        final PutTermsAgreementDto putTermsAgreementDto = new PutTermsAgreementDto(profile, terms);
        termsAgreementService.putTermsAgreement(putTermsAgreementDto, agreeType);

        final TermsDetailDto termsDetailDto = TermsDetailDto.of(terms);
        return new TermsAgreementDto(termsDetailDto, agreeType);
    }

}


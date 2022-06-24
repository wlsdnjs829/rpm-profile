package com.jinwon.rpm.profile.domain.profile;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.RoleType;
import com.jinwon.rpm.profile.domain.profile.dto.CommonProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.PostProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.ProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.UpdateProfilePasswordDto;
import com.jinwon.rpm.profile.domain.role.Role;
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

}


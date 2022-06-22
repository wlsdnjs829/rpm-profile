package com.jinwon.rpm.profile.domain.profile;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.RoleType;
import com.jinwon.rpm.profile.domain.profile.dto.PostProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.ProfileDto;
import com.jinwon.rpm.profile.domain.role.Role;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

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
    public Profile postUser(@NotNull PostProfileDto postProfileDto) {
        postProfileDto.validation();
        final Profile profile = postProfileDto.toEntity();

        final Profile savedProfile = profileRepository.save(profile);

        final Role role = new Role(RoleType.USER);
        savedProfile.grantRoles(role);

        return savedProfile;
    }

    /**
     * 프로필 부분 수정
     *
     * @param profileDto 프로필 DTO
     * @return 수정된 프로필 정보
     */
    public Profile patchUser(@NotNull ProfileDto profileDto) {
        final Profile profile = profileRepository.findById(profileDto.getProfileId())
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE));

        return profile.patch(profileDto, profile);
    }

}


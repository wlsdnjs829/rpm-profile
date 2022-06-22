package com.jinwon.rpm.profile.domain.profile;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.domain.profile.dto.ProfileDto;
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


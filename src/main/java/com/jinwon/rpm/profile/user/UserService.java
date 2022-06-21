package com.jinwon.rpm.profile.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 프로필 전체 수정
     *
     * @param userDto 유저 DTO
     * @return 수정된 유저 정보
     */
    public User putUser(@NotNull UserDto userDto) {
        userRepository.findById(userDto)
    }

}

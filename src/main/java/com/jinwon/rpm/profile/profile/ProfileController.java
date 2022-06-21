package com.jinwon.rpm.profile.profile;

import com.jinwon.rpm.profile.base.BaseController;
import com.jinwon.rpm.profile.enums.ErrorMessage;
import com.jinwon.rpm.profile.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로필 컨트롤러")
public class ProfileController implements BaseController {

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;

    @GetMapping
    @Operation(description = "프로필 조회")
    public Mono<ProfileDto> getProfile(@NotNull Authentication authentication) {
        final Optional<Profile> profileOp = getProfileOp(authentication);
        final Supplier<CustomException> exceptionSupplier = () -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE);

        return profileOp.map(Profile::getProfileId)
                .map(profileRepository::findById)
                .orElseThrow(exceptionSupplier)
                .map(ProfileDto::of)
                .map(Mono::just)
                .orElseThrow(exceptionSupplier);
    }

    @PostMapping
    @Operation(description = "프로필 생성")
    public Mono<Profile> createProfile(@Valid @RequestBody PostProfileDto postProfileDto) {
        postProfileDto.postProfileThrowIfInvalid();
        return Mono.just(profileRepository.save(Profile.of(postProfileDto)));
    }

//    @PutMapping
//    @Operation(description = "프로필 전체 수정")
//    public Mono<Profile> putProfile(@NotNull Authentication authentication, @Valid @RequestBody ProfileDto profileDto) {
//        final long userId = getProfileIdThrowIfNotExist(authentication);
//        profileDto.userEssentialInfo(userId);
//
//        return Mono.just(profileRepository.save(Profile.of(profileDto)));
//    }

    @PatchMapping
    @Operation(description = "프로필 부분 수정")
    public Mono<Profile> patchProfile(@NotNull Authentication authentication, @Valid @RequestBody ProfileDto profileDto) {
        final long userId = getProfileIdThrowIfNotExist(authentication);
        profileDto.userEssentialInfo(userId);

        return Mono.just(profileService.patchUser(profileDto));
    }

}

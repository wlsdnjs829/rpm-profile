package com.jinwon.rpm.profile.domain.profile;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.profile.dto.DeleteProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.PostProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.ProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.TermsAgreementDto;
import com.jinwon.rpm.profile.domain.profile.dto.UpdateProfileDto;
import com.jinwon.rpm.profile.domain.profile.dto.UpdateProfilePasswordDto;
import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로필 Controller")
public class ProfileController implements BaseController {

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;

    //TODO 임시
    @GetMapping
    @Operation(summary = "프로필 조회")
    public Mono<ProfileDto> getProfile() {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        final Supplier<CustomException> exceptionSupplier = () -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE);

        return Optional.of(1L)
                .map(profileRepository::findById)
                .orElseThrow(exceptionSupplier)
                .map(ProfileDto::of)
                .map(Mono::just)
                .orElseThrow(exceptionSupplier);
    }

//    @GetMapping
//    @Operation(summary = "프로필 조회")
//    public Mono<ProfileDto> getProfile(@NotNull Authentication authentication) {
//        final Optional<Profile> profileOp = getProfileOp(authentication);
//        final Supplier<CustomException> exceptionSupplier = () -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE);
//
//        return profileOp.map(Profile::getProfileId)
//                .map(profileRepository::findById)
//                .orElseThrow(exceptionSupplier)
//                .map(ProfileDto::of)
//                .map(Mono::just)
//                .orElseThrow(exceptionSupplier);
//    }

    @PostMapping
    @Operation(summary = "프로필 생성")
    public Mono<ProfileDto> createProfile(@Valid @RequestBody PostProfileDto postProfileDto) {
        return Mono.just(profileService.postProfile(postProfileDto));
    }

    @PutMapping
    @Operation(summary = "프로필 전체 수정")
    public Mono<ProfileDto> putProfile(@NotNull Authentication authentication,
                                       @Valid @RequestBody UpdateProfileDto updateProfileDto) {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        updateProfileDto.userEssentialInfo(profileId);

        return Mono.just(profileService.putProfile(updateProfileDto));
    }

    @PatchMapping
    @Operation(summary = "프로필 부분 수정")
    public Mono<ProfileDto> patchProfile(@NotNull Authentication authentication,
                                         @Valid @RequestBody UpdateProfileDto updateProfileDto) {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        updateProfileDto.userEssentialInfo(profileId);

        return Mono.just(profileService.patchProfile(updateProfileDto));
    }

    @PatchMapping(value = "/password")
    @Operation(summary = "패스워드 수정")
    public Mono<ProfileDto> putProfilePassword(@NotNull Authentication authentication,
                                               @Valid @RequestBody UpdateProfilePasswordDto updateProfilePasswordDto) {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        updateProfilePasswordDto.userEssentialInfo(profileId, "ljw0829@midasin.com");

        return Mono.just(profileService.patchProfilePassword(updateProfilePasswordDto));
    }

    @DeleteMapping(value = "/withdraw")
    @Operation(summary = "프로필 삭제")
    public Mono<PostWithdrawReasonDto> deleteProfile(@NotNull Authentication authentication,
                                                     @Valid @RequestBody DeleteProfileDto deleteProfileDto) {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        deleteProfileDto.userEssentialInfo(profileId);

        return Mono.just(profileService.deleteProfile(deleteProfileDto));
    }

    @GetMapping(value = "/agreement/{type}")
    @Operation(summary = "프로필 동의서 조회")
    public Mono<TermsAgreementDto> getProfileAgreement(@NotNull Authentication authentication,
                                                       @PathVariable TermsType type) {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        final TermsAgreementDto termsAgreementDto = profileService.getTermsAgreement(profileId, type);
        return Mono.just(termsAgreementDto);
    }

    @Operation(summary = "프로필 마케팅 생성/수정")
    @PutMapping(value = "/agreement/marketing/{agreeType}")
    public Mono<TermsAgreementDto> putProfileMarketingAgree(@NotNull Authentication authentication,
                                                            @PathVariable UseType agreeType) {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        final TermsAgreementDto termsAgreementDto =
                profileService.putMarketingAgreement(profileId, agreeType);

        return Mono.just(termsAgreementDto);
    }

}

package com.jinwon.rpm.profile.domain.profile;

import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.profile.dto.ProfileAgreementDto;
import com.jinwon.rpm.profile.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로필 동의서 Controller")
@RequestMapping(value = "/agreement")
public class ProfileAgreementController implements BaseController {

    private final ProfileAgreementService profileAgreementService;

    @GetMapping(value = "/{type}")
    @Operation(summary = "프로필 동의서 조회")
    public Mono<ProfileAgreementDto> getProfileAgreement(@NotNull Authentication authentication,
                                                         @PathVariable TermsType type) {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        final ProfileAgreementDto profileAgreementDto = profileAgreementService.getProfileAgreement(profileId, type);
        return Mono.just(profileAgreementDto);
    }

    @Operation(summary = "프로필 마케팅 생성/수정")
    @PutMapping(value = "/marketing/{agreeType}")
    public Mono<ProfileAgreementDto> putProfileMarketingAgree(@NotNull Authentication authentication,
                                                              @PathVariable UseType agreeType) {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        final ProfileAgreementDto profileAgreementDto =
                profileAgreementService.putProfileMarketingAgree(profileId, agreeType);

        return Mono.just(profileAgreementDto);
    }

}

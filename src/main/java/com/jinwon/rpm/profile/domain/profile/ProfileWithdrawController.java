package com.jinwon.rpm.profile.domain.profile;

import com.jinwon.rpm.profile.domain.profile.dto.DeleteProfileDto;
import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
import com.jinwon.rpm.profile.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로필 삭제 Controller")
@RequestMapping(value = "/withdraw")
public class ProfileWithdrawController implements BaseController {

    private final ProfileWithdrawService profileWithdrawService;

    @DeleteMapping
    @Operation(summary = "프로필 삭제")
    public Mono<PostWithdrawReasonDto> deleteProfile(@NotNull Authentication authentication,
                                                     @Valid @RequestBody DeleteProfileDto deleteProfileDto) {
//        final Long profileId = getProfileIdThrowIfNotExist(authentication);
        final Long profileId = 1L;
        deleteProfileDto.userEssentialInfo(profileId);

        return Mono.just(profileWithdrawService.deleteProfile(deleteProfileDto));
    }

}

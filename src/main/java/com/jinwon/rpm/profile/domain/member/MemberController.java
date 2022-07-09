package com.jinwon.rpm.profile.domain.member;

import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.attach_file.inner_dto.MemberAttachFileDto;
import com.jinwon.rpm.profile.domain.member.dto.DeleteMemberDto;
import com.jinwon.rpm.profile.domain.member.dto.MemberDto;
import com.jinwon.rpm.profile.domain.member.dto.PostMemberDto;
import com.jinwon.rpm.profile.domain.member.dto.UpdateMemberDto;
import com.jinwon.rpm.profile.domain.member.dto.UpdateMemberPasswordDto;
import com.jinwon.rpm.profile.domain.terms_agreement.dto.TermsAgreementDetailDto;
import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
import com.jinwon.rpm.profile.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "사용자")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.PATH_V1)
public class MemberController implements BaseController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "사용자 조회")
    public MemberDto getMember(@NotNull Authentication authentication) {
        final Long memberId = getMemberIdThrowIfNotExist(authentication);

        return memberService.getMember(memberId);
    }

    @PostMapping
    @Operation(summary = "사용자 생성")
    public MemberDto createMember(@Valid @RequestBody PostMemberDto postMemberDto) {
        return memberService.postMember(postMemberDto);
    }

    @PutMapping
    @Operation(summary = "사용자 전체 수정")
    public MemberDto putMember(@NotNull Authentication authentication,
                               @Valid @RequestBody UpdateMemberDto updateMemberDto) {
        final Long memberId = getMemberIdThrowIfNotExist(authentication);
        updateMemberDto.userEssentialInfo(memberId);

        return memberService.putMember(updateMemberDto);
    }

    @PatchMapping
    @Operation(summary = "사용자 부분 수정")
    public MemberDto patchMember(@NotNull Authentication authentication,
                                 @Valid @RequestBody UpdateMemberDto updateMemberDto) {
        final Long memberId = getMemberIdThrowIfNotExist(authentication);
        updateMemberDto.userEssentialInfo(memberId);

        return memberService.patchMember(updateMemberDto);
    }

    @PatchMapping(value = "/password")
    @Operation(summary = "패스워드 수정")
    public MemberDto putMemberPassword(@NotNull Authentication authentication,
                                       @Valid @RequestBody UpdateMemberPasswordDto updateMemberPasswordDto) {
        final Long memberId = getMemberIdThrowIfNotExist(authentication);
        updateMemberPasswordDto.userEssentialInfo(memberId);
        return memberService.patchMemberPassword(updateMemberPasswordDto);
    }

    @DeleteMapping(value = "/withdraw")
    @Operation(summary = "사용자 삭제")
    public PostWithdrawReasonDto deleteMember(@NotNull Authentication authentication,
                                              @Valid @RequestBody DeleteMemberDto deleteMemberDto) {
        final Long memberId = getMemberIdThrowIfNotExist(authentication);
        deleteMemberDto.userEssentialInfo(memberId);

        return memberService.deleteMember(deleteMemberDto);
    }

    @GetMapping(value = "/agreement/{type}")
    @Operation(summary = "사용자 동의서 조회")
    public TermsAgreementDetailDto getMemberAgreement(@NotNull Authentication authentication,
                                                      @PathVariable TermsType type) {
        final Long memberId = getMemberIdThrowIfNotExist(authentication);
        return memberService.getTermsAgreement(memberId, type);
    }

    @Operation(summary = "사용자 마케팅 생성/수정")
    @PutMapping(value = "/agreement/marketing/{agreeType}")
    public TermsAgreementDetailDto putMemberMarketingAgree(@NotNull Authentication authentication,
                                                           @PathVariable UseType agreeType) {
        final Long memberId = getMemberIdThrowIfNotExist(authentication);
        return memberService.putMarketingAgreement(memberId, agreeType);
    }

    @PutMapping(value = "/attach-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "사용자 첨부 파일 업로드")
    public MemberAttachFileDto putMemberFile(@NotNull Authentication authentication,
                                             @RequestPart MultipartFile multipartFile) {
        final Long memberId = getMemberIdThrowIfNotExist(authentication);
        return memberService.uploadMemberFile(memberId, multipartFile);
    }

    @GetMapping(value = "/attach-file/pre-signed-url")
    @Operation(summary = "사용자 첨부 파일 서명 주소 조회")
    public String getMemberFile(@NotNull Authentication authentication) {
        final Long memberId = getMemberIdThrowIfNotExist(authentication);
        return memberService.getMemberFilePreSignedUrl(memberId);
    }

}

package com.jinwon.rpm.profile.domain.member;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.RoleType;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.attach_file.MemberAttachFile;
import com.jinwon.rpm.profile.domain.attach_file.MemberAttachFileService;
import com.jinwon.rpm.profile.domain.attach_file.inner_dto.MemberAttachFileDto;
import com.jinwon.rpm.profile.domain.member.dto.CommonMemberDto;
import com.jinwon.rpm.profile.domain.member.dto.DeleteMemberDto;
import com.jinwon.rpm.profile.domain.member.dto.MemberDto;
import com.jinwon.rpm.profile.domain.member.dto.PostMemberDto;
import com.jinwon.rpm.profile.domain.member.dto.UpdateMemberPasswordDto;
import com.jinwon.rpm.profile.domain.role.Role;
import com.jinwon.rpm.profile.domain.terms_agreement.TermsAgreementService;
import com.jinwon.rpm.profile.domain.terms_agreement.dto.TermsAgreementDetailDto;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.TermsAgreementDto;
import com.jinwon.rpm.profile.domain.withdraw.WithdrawService;
import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.utils.PasswordEncryptUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 사용자 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final WithdrawService withdrawService;
    private final MemberAttachFileService memberAttachFileService;
    private final TermsAgreementService termsAgreementService;

    private final MemberRepository memberRepository;

    /**
     * 사용자 조회
     *
     * @param memberId 사용자 ID
     */
    public MemberDto getMember(Long memberId) {
        final Supplier<CustomException> exceptionSupplier = () -> new CustomException(ErrorMessage.NOT_EXIST_MEMBER);

        return Optional.of(memberId)
                .map(memberRepository::findById)
                .orElseThrow(exceptionSupplier)
                .map(MemberDto::of)
                .orElseThrow(exceptionSupplier);
    }

    /**
     * 사용자 생성
     *
     * @param postMemberDto 사용자 생성 객체
     * @return 생성된 사용자 정보
     */
    public MemberDto postMember(PostMemberDto postMemberDto) {
        Assert.notNull(postMemberDto, ErrorMessage.INVALID_PARAM.name());

        final Member member = createValidMember(postMemberDto);
        final Member savedMember = memberRepository.save(member);

        final Role role = new Role(RoleType.USER);
        savedMember.grantRoles(role);

        final List<TermsType> agreeTermsTypes = postMemberDto.getAgreeTermsTypes();
        termsAgreementService.putDefaultTermsAgreements(member, agreeTermsTypes);
        return MemberDto.of(savedMember);
    }

    /* 유효한 사용자 생성 */
    private Member createValidMember(PostMemberDto postMemberDto) {
        postMemberDto.validation();

        final String email = postMemberDto.getEmail();
        final Optional<Member> existMember = memberRepository.findByEmail(email);
        Assert.isTrue(existMember.isEmpty(), ErrorMessage.EXIST_EMAIL.name());

        return postMemberDto.toEntity();
    }

    /**
     * 사용자 부분 수정
     *
     * @param commonMemberDto 공통 사용자 부분 수정 DTO
     * @return 수정된 사용자 정보
     */
    public MemberDto patchMember(CommonMemberDto commonMemberDto) {
        Assert.notNull(commonMemberDto, ErrorMessage.INVALID_PARAM.name());

        final Long memberId = commonMemberDto.getMemberId();
        final Member member = getMemberThrowIfNull(memberId);

        final Member patchMember = member.patch(commonMemberDto);
        return MemberDto.of(patchMember);
    }

    /**
     * 사용자 전체 수정
     *
     * @param commonMemberDto 공통 사용자 전체 수정 DTO
     * @return 수정된 사용자 정보
     */
    public MemberDto putMember(CommonMemberDto commonMemberDto) {
        Assert.notNull(commonMemberDto, ErrorMessage.INVALID_PARAM.name());

        final Long memberId = commonMemberDto.getMemberId();
        final Member member = getMemberThrowIfNull(memberId);

        final Member patchMember = member.put(commonMemberDto);
        return MemberDto.of(patchMember);
    }

    /* 사용자 조회, 없을 시 예외 처리 */
    private Member getMemberThrowIfNull(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_MEMBER));
    }

    /**
     * 사용자 패스워드 수정
     *
     * @param updateMemberPasswordDto 사용자 패스워드 수정 DTO
     * @return 수정된 사용자 정보
     */
    public MemberDto patchMemberPassword(UpdateMemberPasswordDto updateMemberPasswordDto) {
        Assert.notNull(updateMemberPasswordDto, ErrorMessage.INVALID_PARAM.name());

        final Long memberId = updateMemberPasswordDto.getMemberId();

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_MEMBER));

        final String encodePassword = member.getPassword();
        final String oldPassword = updateMemberPasswordDto.getOldPassword();
        Assert.isTrue(PasswordEncryptUtil.match(oldPassword, encodePassword), ErrorMessage.MISMATCH_PASSWORD.name());

        updateMemberPasswordDto.userEssentialInfo(member.getEmail());
        updateMemberPasswordDto.validation();

        final Member patchMember = member.patch(updateMemberPasswordDto);
        return MemberDto.of(patchMember);
    }

    /**
     * 사용자 삭제
     *
     * @param deleteMemberDto 사용자 삭제 DTO
     * @return 사용자 탈퇴 사유
     */
    public PostWithdrawReasonDto deleteMember(DeleteMemberDto deleteMemberDto) {
        Assert.notNull(deleteMemberDto, ErrorMessage.INVALID_PARAM.name());

        final Long memberId = deleteMemberDto.getMemberId();
        final Member member = getMemberThrowIfNull(memberId);

        final String inputPassword = deleteMemberDto.getPassword();
        final String encodePassword = member.getPassword();
        Assert.isTrue(PasswordEncryptUtil.match(inputPassword, encodePassword), ErrorMessage.MISMATCH_PASSWORD.name());

        deleteMemberFile(member);
        termsAgreementService.deleteByMember(member);
        memberRepository.delete(member);
        return withdrawService.postWithdrawReason(deleteMemberDto, member);
    }

    /**
     * 동의 여부 조회
     *
     * @param memberId 사용자 아이디
     * @param type     동의서 타입
     */
    public TermsAgreementDetailDto getTermsAgreement(Long memberId, TermsType type) {
        Assert.notNull(memberId, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(type, ErrorMessage.INVALID_PARAM.name());

        final Member member = getMemberThrowIfNull(memberId);
        return termsAgreementService.getTermsAgreementDetail(member, type);
    }

    /**
     * 사용자 마케팅 동의 생성 / 수정
     *
     * @param memberId  사용자 아이디
     * @param agreeType 동의 타입
     * @return 동의한 사용자 DTO
     */
    public TermsAgreementDetailDto putMarketingAgreement(Long memberId, UseType agreeType) {
        Assert.notNull(memberId, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(agreeType, ErrorMessage.INVALID_PARAM.name());

        final Member member = getMemberThrowIfNull(memberId);
        final TermsAgreementDto termsAgreementDto =
                new TermsAgreementDto(member, TermsType.RECEIVE_MARKETING_INFO, agreeType);

        return termsAgreementService.putTermsAgreement(termsAgreementDto);
    }

    /**
     * 사용자 파일 첨부
     *
     * @param memberId      사용자 ID
     * @param multipartFile 첨부 파일
     * @return 첨부한 파일 정보
     */
    public MemberAttachFileDto uploadMemberFile(Long memberId, MultipartFile multipartFile) {
        Assert.notNull(memberId, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(multipartFile, ErrorMessage.INVALID_PARAM.name());

        final Member member = getMemberThrowIfNull(memberId);

        deleteMemberFile(member);

        final MemberAttachFile attachFile = memberAttachFileService.uploadMemberFile(multipartFile);
        attachFile.linkMember(member);
        return MemberAttachFileDto.of(attachFile);
    }

    /* 사용자 파일 삭제 */
    private void deleteMemberFile(Member member) {
        memberAttachFileService.deleteMemberFile(member);
    }

    /**
     * 사용자 파일 조회
     *
     * @param memberId 사용자 ID
     */
    public String getMemberFilePreSignedUrl(Long memberId) {
        Assert.notNull(memberId, ErrorMessage.INVALID_PARAM.name());

        final Member member = getMemberThrowIfNull(memberId);

        return Optional.of(member)
                .map(memberAttachFileService::getPreSignedUrl)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_ATTACH_FILE));
    }

}


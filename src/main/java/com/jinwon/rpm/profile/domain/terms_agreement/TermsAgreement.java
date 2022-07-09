package com.jinwon.rpm.profile.domain.terms_agreement;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsCondition;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.terms.Terms;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.PutTermsAgreementDto;
import com.jinwon.rpm.profile.infra.utils.AssertUtil;
import com.jinwon.rpm.profile.model.BaseEntity;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * 이용 약관 동의
 */
@Entity
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "terms_agreement_unique_001", columnNames = {"member_id", "terms_id"}),
})
public class TermsAgreement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsAgreementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "terms_agreement_foreign_key_001"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", nullable = false, foreignKey = @ForeignKey(name = "terms_agreement_foreign_key_002"))
    private Terms terms;

    @Column(nullable = false)
    private UseType agreeType;

    /**
     * 미동의 약관 생성
     *
     * @param putTermsAgreementDto 약관 동의 생성 / 수정 DTO
     */
    public static TermsAgreement disagreeTermsAgreement(@NotNull PutTermsAgreementDto putTermsAgreementDto) {
        Assert.notNull(putTermsAgreementDto, ErrorMessage.INVALID_PARAM.name());

        final Terms terms = putTermsAgreementDto.terms();
        final Member member = putTermsAgreementDto.member();

        final TermsCondition condition = terms.getTermsCondition();
        validTermsCondition(condition, UseType.UNUSED);

        final TermsAgreement termsAgreement = new TermsAgreement();
        termsAgreement.member = member;
        termsAgreement.terms = terms;
        termsAgreement.agreeType = UseType.UNUSED;

        return termsAgreement;
    }

    /**
     * 동의 약관 생성
     *
     * @param putTermsAgreementDto 약관 동의 생성 / 수정 DTO
     */
    public static TermsAgreement agreeTermsAgreement(@NotNull PutTermsAgreementDto putTermsAgreementDto) {
        Assert.notNull(putTermsAgreementDto, ErrorMessage.INVALID_PARAM.name());

        final Terms terms = putTermsAgreementDto.terms();
        final Member member = putTermsAgreementDto.member();

        final TermsCondition condition = terms.getTermsCondition();
        validTermsCondition(condition, UseType.USE);

        final TermsAgreement termsAgreement = new TermsAgreement();
        termsAgreement.member = member;
        termsAgreement.terms = terms;
        termsAgreement.agreeType = UseType.USE;

        return termsAgreement;
    }

    /**
     * 동의 여부 저장
     *
     * @param agreeType 동의 타입
     */
    public void agreeOrNot(UseType agreeType) {
        Assert.notNull(agreeType, ErrorMessage.INVALID_PARAM.name());
        AssertUtil.isFalse(agreeType.equals(this.agreeType), ErrorMessage.SAME_AGREE_TYPE.name());

        final TermsCondition condition = terms.getTermsCondition();
        validTermsCondition(condition, agreeType);

        this.agreeType = agreeType;
    }

    /* 동의 조건 충족 -> 필수 동의서 X OR 동의 */
    private static void validTermsCondition(TermsCondition condition, UseType agreeType) {
        Assert.isTrue(
                !TermsCondition.ESSENTIAL.equals(condition) || UseType.USE.equals(agreeType),
                ErrorMessage.DONT_MEET_TERMS_OF_CONSENT.name());
    }

}

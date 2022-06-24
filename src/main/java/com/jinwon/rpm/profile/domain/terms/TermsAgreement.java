package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsCondition;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.profile.Profile;
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
@Table(uniqueConstraints = @UniqueConstraint(
        name = "terms_agreement_unique_constraint_001", columnNames = {"profile_id", "terms_id"})
)
public class TermsAgreement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsAgreementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "profile_id", nullable = false, foreignKey = @ForeignKey(name = "terms_agreement_foreign_key_001"))
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", nullable = false)
    private Terms terms;

    @Column(nullable = false)
    private UseType agreeType;

    /**
     * 동의 약관 생성
     *
     * @param profile   프로필
     * @param terms     약관
     * @param agreeType 동의 타입
     */
    public static TermsAgreement create(@NotNull Profile profile, @NotNull Terms terms, @NotNull UseType agreeType) {
        Assert.notNull(profile, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(terms, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(agreeType, ErrorMessage.INVALID_PARAM.name());

        final TermsCondition condition = terms.getCondition();
        Assert.isTrue(meetTermsOfConsent(condition, agreeType), ErrorMessage.DONT_MEET_TERMS_OF_CONSENT.name());

        final TermsAgreement termsAgreement = new TermsAgreement();
        termsAgreement.profile = profile;
        termsAgreement.terms = terms;
        termsAgreement.agreeType = agreeType;

        return termsAgreement;
    }

    /**
     * 동의 여부 저장
     *
     * @param agreeType 동의 타입
     */
    public void agreeOrNot(@NotNull UseType agreeType) {
        Assert.notNull(agreeType, ErrorMessage.INVALID_PARAM.name());
        Assert.isTrue(!agreeType.equals(this.agreeType), ErrorMessage.SAME_AGREE_TYPE.name());

        final TermsCondition condition = terms.getCondition();
        Assert.isTrue(meetTermsOfConsent(condition, agreeType), ErrorMessage.DONT_MEET_TERMS_OF_CONSENT.name());

        this.agreeType = agreeType;
    }

    /* 동의 조건 충족 -> 필수 동의서 X OR 동의 */
    private static boolean meetTermsOfConsent(TermsCondition termsCondition, UseType agreeType) {
        return !TermsCondition.ESSENTIAL.equals(termsCondition) || UseType.USE.equals(agreeType);
    }

}

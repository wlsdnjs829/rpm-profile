package com.jinwon.rpm.profile.domain.terms_agreement;

import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.terms.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * 약관 동의 Repository
 */
public interface TermsAgreementRepository extends JpaRepository<TermsAgreement, Long> {

    Optional<TermsAgreement> findByMemberAndTerms(Member member, Terms terms);

    @Modifying
    @Transactional
    @Query(value = "delete from TermsAgreement TA where TA.member = :member")
    void deleteByMember(Member member);

}
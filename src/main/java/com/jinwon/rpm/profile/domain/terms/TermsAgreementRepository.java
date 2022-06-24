package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * 약관 동의 Repository
 */
public interface TermsAgreementRepository extends JpaRepository<TermsAgreement, Long> {

    Optional<TermsAgreement> findByProfileAndTerms(Profile profile, Terms terms);

    @Modifying
    @Transactional
    @Query(value = "delete from TermsAgreement TA where TA.profile = :profile")
    void deleteByProfile(Profile profile);

}
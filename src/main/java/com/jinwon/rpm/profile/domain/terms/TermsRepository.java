package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.constants.enums.TermsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 약관 Repository
 */
public interface TermsRepository extends JpaRepository<Terms, Long> {

    Optional<Terms> findByTypeAndVersion(TermsType type, String version);

}
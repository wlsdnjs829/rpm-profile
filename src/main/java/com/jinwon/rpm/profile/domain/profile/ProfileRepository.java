package com.jinwon.rpm.profile.domain.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 프로필 Repository
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByEmail(String email);

}
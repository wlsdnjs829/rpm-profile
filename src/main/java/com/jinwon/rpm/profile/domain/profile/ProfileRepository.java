package com.jinwon.rpm.profile.domain.profile;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필 Repository
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
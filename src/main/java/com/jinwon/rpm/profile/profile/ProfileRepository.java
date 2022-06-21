package com.jinwon.rpm.profile.profile;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필 Repository
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
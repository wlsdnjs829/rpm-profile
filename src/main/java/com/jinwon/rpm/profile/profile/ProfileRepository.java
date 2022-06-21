package com.jinwon.rpm.profile.profile;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 Repository
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
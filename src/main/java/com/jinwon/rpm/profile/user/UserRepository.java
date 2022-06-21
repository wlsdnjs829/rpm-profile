package com.jinwon.rpm.profile.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
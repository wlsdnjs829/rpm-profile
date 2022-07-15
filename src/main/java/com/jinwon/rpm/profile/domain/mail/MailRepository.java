package com.jinwon.rpm.profile.domain.mail;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 메일 Repository
 */
public interface MailRepository extends JpaRepository<Mail, Long> {

}
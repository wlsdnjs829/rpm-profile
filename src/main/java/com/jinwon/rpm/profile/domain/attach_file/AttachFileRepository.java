package com.jinwon.rpm.profile.domain.attach_file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 첨부 파일 Repository
 */
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    Optional<AttachFile> findByFileUid(String fileUid);

}
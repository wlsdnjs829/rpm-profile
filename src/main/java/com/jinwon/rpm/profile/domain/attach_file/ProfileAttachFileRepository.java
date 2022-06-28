package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.constants.enums.AttachFileType;
import com.jinwon.rpm.profile.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 프로필 첨부 파일 Repository
 */
public interface ProfileAttachFileRepository extends JpaRepository<ProfileAttachFile, Long> {

    Optional<ProfileAttachFile> findByFileUid(String fileUid);

    List<ProfileAttachFile> findByProfileAndType(Profile profile, AttachFileType type);


}
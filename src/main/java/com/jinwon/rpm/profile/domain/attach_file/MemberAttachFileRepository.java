package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.constants.enums.AttachFileType;
import com.jinwon.rpm.profile.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 사용자 첨부 파일 Repository
 */
public interface MemberAttachFileRepository extends JpaRepository<MemberAttachFile, Long> {

    List<MemberAttachFile> findByMemberAndType(Member member, AttachFileType type);

}
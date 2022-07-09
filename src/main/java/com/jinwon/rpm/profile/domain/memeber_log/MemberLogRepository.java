package com.jinwon.rpm.profile.domain.memeber_log;

import com.jinwon.rpm.profile.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 사용자 로그 Repository
 */
public interface MemberLogRepository extends JpaRepository<MemberLog, Long> {

    List<MemberLog> findByMemberOrderByMemberLogIdDesc(Member member, Pageable pageable);

}
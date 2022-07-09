package com.jinwon.rpm.profile.domain.memeber_log;

import com.jinwon.rpm.profile.constants.enums.MemberLogType;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 사용자 로그 Entity
 */
@Table(indexes = {
        @Index(name = "member_log_index_001", columnList = "memberLogType")
})
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "member_log_foreign_key_001"))
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberLogType memberLogType;

}



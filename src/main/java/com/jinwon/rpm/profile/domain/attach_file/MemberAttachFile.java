package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 첨부 파일 조회
 */
@Table(indexes = {
        @Index(name = "member_attach_file_index_001", columnList = "type"),
}, uniqueConstraints = {
        @UniqueConstraint(name = "member_attach_file_unique_001", columnNames = "fileUid"),
})
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAttachFile extends AttachFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberAttachFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "member_attach_file_member_foreign_key_001"))
    private Member member;

    public void linkMember(Member member) {
        this.member = member;
    }

}

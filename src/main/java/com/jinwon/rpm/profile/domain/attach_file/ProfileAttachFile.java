package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.domain.profile.Profile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 첨부 파일 조회
 */
@Table(uniqueConstraints = @UniqueConstraint(
        name = "profile_attach_file_unique_constraint_001", columnNames = {"type"}
))
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileAttachFile extends AttachFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileAttachFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false,
            foreignKey = @ForeignKey(name = "profile_attach_file_profile_foreign_key_001"))
    private Profile profile;

    public void linkProfile(Profile profile) {
        this.profile = profile;
    }

}

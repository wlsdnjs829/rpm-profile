package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 첨부 파일 조회
 */
@Table
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachFileId;

    @Column(nullable = false, unique = true)
    private String fileUid;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long fileSize;

}

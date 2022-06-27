package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.constants.enums.TermsCondition;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.model.BaseEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

/**
 * 약관 Entity
 */
@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        name = "terms_unique_constraint_001", columnNames = {"type", "version"}
))
public class Terms extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsId;

    @Column(nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private TermsType type;

    @Column(nullable = false, length = 15)
    @Enumerated(value = EnumType.STRING)
    private TermsCondition condition;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false, length = 15)
    @Enumerated(value = EnumType.STRING)
    private UseType useType;

    @Column(nullable = false, length = 30)
    private String version;

    private LocalDateTime noticeDateTime; // 공고일

    private LocalDateTime effectiveDateTime; // 시행일

}

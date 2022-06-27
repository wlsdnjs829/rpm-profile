package com.jinwon.rpm.profile.domain.withdraw;

import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.constants.enums.WithdrawType;
import com.jinwon.rpm.profile.infra.converter.EncryptConverter;
import com.jinwon.rpm.profile.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

/**
 * 회원 탈퇴 Entity
 */
@Table(indexes = {
        @Index(name = "withdraw_index_001", columnList = "email"),
        @Index(name = "withdraw_index_002", columnList = "type")
})
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Withdraw extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long withdrawId;

    @Column(nullable = false, length = 350)
    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Convert(converter = EncryptConverter.class)
    private String email;

    @Column(nullable = false, length = 40)
    @Pattern(regexp = RegexPattern.NAME_REGEX)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WithdrawType type;

    @Column
    private String reason;

}



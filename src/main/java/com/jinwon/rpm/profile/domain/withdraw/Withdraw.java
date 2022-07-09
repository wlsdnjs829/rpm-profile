package com.jinwon.rpm.profile.domain.withdraw;

import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.infra.converter.EncryptConverter;
import com.jinwon.rpm.profile.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 탈퇴 Entity
 */
@Table(indexes = {@Index(name = "withdraw_index_001", columnList = "email")})
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Withdraw extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawId;

    @Column(nullable = false, length = 350)
    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Convert(converter = EncryptConverter.class)
    private String email;

    @Column(nullable = false, length = 40)
    @Pattern(regexp = RegexPattern.NAME_REGEX)
    private String name;

    @OneToMany(mappedBy = "withdraw", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<WithdrawReason> withdrawReasons = new ArrayList<>();

    @Column
    private String reason;

    /**
     * 탈퇴 사유 등록
     *
     * @param withdrawReason 탈퇴 사유
     */
    public void registration(WithdrawReason withdrawReason) {
        this.withdrawReasons.add(withdrawReason);
        withdrawReason.registration(this);
    }

}



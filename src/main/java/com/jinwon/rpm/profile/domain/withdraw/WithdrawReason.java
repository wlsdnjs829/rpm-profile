package com.jinwon.rpm.profile.domain.withdraw;

import com.jinwon.rpm.profile.constants.enums.WithdrawType;
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
import javax.persistence.UniqueConstraint;

/**
 * 사용자 탈퇴 사유 Entity
 */
@Table(indexes = {
        @Index(name = "withdraw_reason_index_001", columnList = "withdrawType")
}, uniqueConstraints = {
        @UniqueConstraint(name = "withdraw_reason_id_type_unique_001", columnNames = {"withdraw_id", "withdrawType"})
})
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawReasonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdraw_id", nullable = false,
            foreignKey = @ForeignKey(name = "withdraw_reason_foreign_key_001"))
    private Withdraw withdraw;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WithdrawType withdrawType;

    public WithdrawReason(WithdrawType withdrawType) {
        this.withdrawType = withdrawType;
    }

    /**
     * 사유 등록
     *
     * @param withdraw 회원 탈퇴
     */
    public void registration(Withdraw withdraw) {
        this.withdraw = withdraw;
    }

}



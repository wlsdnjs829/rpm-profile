package com.jinwon.rpm.profile.domain.role;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwon.rpm.profile.constants.enums.RoleType;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 권한 Entity
 */
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "member_role_type_unique_001",
                columnNames = {"member_id", "roleType"}
        )
})
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "role_member_foreign_key_001"))
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public Role(RoleType roleType) {
        this.roleType = roleType;
    }

    public void grant(Member member) {
        this.member = member;
    }

}



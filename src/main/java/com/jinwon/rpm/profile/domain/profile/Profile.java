package com.jinwon.rpm.profile.domain.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.constants.enums.CountryCode;
import com.jinwon.rpm.profile.domain.role.Role;
import com.jinwon.rpm.profile.infra.converter.EncryptConverter;
import com.jinwon.rpm.profile.infra.converter.PasswordConverter;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import com.jinwon.rpm.profile.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 프로필 Entity
 */
@Table
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long profileId;

    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Convert(converter = EncryptConverter.class)
    @Column(nullable = false, length = 350, unique = true)
    private String email;

    @Email
    @Column(length = 350)
    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Convert(converter = EncryptConverter.class)
    private String subEmail;

    @Column(length = 100, unique = true)
    @Pattern(regexp = RegexPattern.PHONE_REGEX)
    @Convert(converter = EncryptConverter.class)
    private String phone;

    @Column(nullable = false, length = 40)
    @Pattern(regexp = RegexPattern.NAME_REGEX)
    private String name;

    @Column(nullable = false, length = 40)
    @Pattern(regexp = RegexPattern.NAME_REGEX)
    private String profileName;

    @Column(length = 100)
    private String affiliatedCompany;

    @Column(nullable = false, length = 100)
    @Convert(converter = PasswordConverter.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode country;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Role> roles = new ArrayList<>();

    @JsonDeserialize(using = AuthorityDeserializer.class)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(Role::getRoleType)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 권한 부여
     *
     * @param role 권한
     */
    public void grantRoles(Role role) {
        this.roles.add(role);
        role.grant(this);
    }

    /**
     * 부분 수정
     *
     * @param object 수정할 데이터
     * @return 부분 수정된 프로필
     */
    public Profile patch(Object object) {
        ModelMapperUtil.patchMap(object, this);
        return this;
    }

    /**
     * 전체 수정
     *
     * @param object 수정할 데이터
     * @return 전체 수정된 프로필
     */
    public Profile put(Object object) {
        ModelMapperUtil.putMap(object, this);
        return this;
    }

}



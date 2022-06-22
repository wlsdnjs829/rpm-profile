package com.jinwon.rpm.profile.domain.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jinwon.rpm.profile.constants.CountryCode;
import com.jinwon.rpm.profile.constants.RegexPattern;
import com.jinwon.rpm.profile.domain.profile.dto.ProfileDto;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import com.jinwon.rpm.profile.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Table
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long profileId;

    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    @Column(nullable = false, length = 350, unique = true)
    private String email;

    @Email
    @Column(length = 350)
    @Pattern(regexp = RegexPattern.EMAIL_REGEX)
    private String subEmail;

    @Column(length = 100, unique = true)
    @Pattern(regexp = RegexPattern.PHONE_REGEX)
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode country;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "profileId"))
    private final List<String> roles = new ArrayList<>();

    @JsonDeserialize(using = AuthorityDeserializer.class)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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

    public Profile patch(ProfileDto profileDto, Profile profile) {
        ModelMapperUtil.patchMap(profileDto, profile);
        return this;
    }

}



package com.app.invoice.security.services;

import com.app.invoice.master.entity.Business;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.app.invoice.master.entity.MasterUser;
import com.app.invoice.tenant.entity.auth.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String phoneNo;
    private String accountNo;
    private Business pharmacy;
    @JsonIgnore
    private String password;
    private Long nurseId;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(long id, String username, String phoneNo, String accountNo, String password,
                           Business pharmacy,
                           Long nurseId,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.phoneNo = phoneNo;
        this.accountNo = accountNo;
        this.password = password;
        this.pharmacy = pharmacy;
        this.nurseId = nurseId;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user, Business pharmacy) {
        Set<String> roles = new HashSet<>(user.getUserTypes());

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUserID(),
                user.getFullName(),
                user.getPhoneNo(),
                user.getPharmacyId(),
                user.getPassword(),
                pharmacy,
                user.getNurseId(),
                authorities
        );
    }

    public static UserDetailsImpl buildForMasterUser(MasterUser user) {
        Set<String> roles = new HashSet<>();
        roles.add(user.getUserType());
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUserID(),
                user.getFullName(),
                user.getPhoneNo(),
                user.getTenantID(),
                user.getPassword(),
                null,
                null,
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("Granted Authorities: " + authorities);
        return authorities;
    }

    public long getId() {
        return id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public Business getPharmacy() {
        return pharmacy;
    }

    public Long getNurseId() {
        return nurseId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setPharmacy(Business pharmacy) {
        this.pharmacy = pharmacy;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNurseId(Long nurseId) {
        this.nurseId = nurseId;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}

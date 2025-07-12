package com.app.invoice.tenant.dto.auth;

import com.app.invoice.master.entity.Business;

import java.util.List;

public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type;
    private long id;
    private String username;
    private String phoneNo;
    private String accountNo;
    private Business pharmacy;
    private List<String> roles;
    private Long nurseId;

    public JwtResponse(String token, String refreshToken, String type, long id, String username,
                       String phoneNo, String accountNo, Business pharmacy, List<String> roles, Long nurseId) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.type = type;
        this.id = id;
        this.username = username;
        this.phoneNo = phoneNo;
        this.accountNo = accountNo;
        this.pharmacy = pharmacy;
        this.roles = roles;
        this.nurseId = nurseId;
    }

    public JwtResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Business getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Business pharmacy) {
        this.pharmacy = pharmacy;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Long getNurseId() {
        return nurseId;
    }

    public void setNurseId(Long nurseId) {
        this.nurseId = nurseId;
    }
}

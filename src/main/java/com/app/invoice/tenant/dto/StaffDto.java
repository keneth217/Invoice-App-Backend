package com.app.invoice.tenant.dto;

import lombok.Data;

import java.util.Base64;
@Data
public class StaffDto {

    private Long id;
    private String nationalIdNumber;
    private String nurseNumber;
    private String fullName;
    private String initials;
    private String phoneNumber;
    private String gender;
    private String address;
    private byte[] signature;
    private byte[] profilePhoto;
    private String email;


    public String getProfilePhotoBase64() {
        return profilePhoto != null ? Base64.getEncoder().encodeToString(profilePhoto) : null;
    }

    public String getSignatureBase64() {
        return signature != null ? Base64.getEncoder().encodeToString(signature) : null;
    }
}

package com.app.invoice.master.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BusinessDTO {

    private String pharmacyName;
    private String pharmacyCode;
    private String address;
    private String email;
    
    private String phoneNo;

    private String region;
    private String pharmacyType;
    private String ownership;
    private String enrollment;

    private String county;
    private String subCounty;
    private String ward;

    private String motto;
    private String vision;
    private String mission;

    private String ownerName;
    private String nationalId;
    private String registrationNo;
    private String contactPhone;
    private String contactEmail;

    private String description;
    private String slogan;

    private Boolean isOnline = true;
    private LocalDateTime expiryDate;
}

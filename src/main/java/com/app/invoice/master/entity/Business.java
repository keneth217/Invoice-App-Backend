package com.app.invoice.master.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public  class Business {
    // Unique identifier for the company
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String email;
    private String businessCode;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private  String imageUrl;
    private String website;
    private String businessOwner;
    private String taxId = "N/A";
    private String currency = "KSH";
    private String  invoicePrefix = "INV-";
    private String invoiceFooter = "Thank you for your business!";
    private String invoiceTerms = "Payment is due within 30 days.";
    private String invoiceNote = "If you have any questions, please contact us.";
    private String receiptPrefix = "RCT-";
    private String receiptFooter = "Thank you for your payment!";
    private String receiptTerms = "Payment is due within 30 days.";
    private String receiptNote = "If you have any questions, please contact us.";
    private String voucherPrefix = "VCH-";
    private String currencySymbol = "KSH";
    private boolean deleted = false;
    private  boolean active = false;
    private String activationToken;
    private boolean activated = false;

    private String dbUrl;


    public Boolean getActive() {
        return null;
    }

    public void setActivated(boolean b) {
        
    }

    public Instant getExpiryDate() {
        return null;
    }

    public boolean getActivated() {
        return false;
    }
}

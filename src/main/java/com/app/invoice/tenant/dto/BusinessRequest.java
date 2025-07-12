package com.app.invoice.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessRequest {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String businessCode;
    private String country;
    private String zipCode;
    private  String imageUrl;
    private  String website;
    private String  invoicePrefix;
    private String invoiceFooter;
    private String invoiceTerms;
    private String invoiceNote;
    private String receiptPrefix;
    private String receiptFooter;
    private String receiptTerms;
    private String receiptNote;
    private String voucherPrefix;
    private String currencySymbol;
    private boolean deleted = false;
}

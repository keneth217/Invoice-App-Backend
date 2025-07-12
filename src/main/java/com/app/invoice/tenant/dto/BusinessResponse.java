package com.app.invoice.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BusinessResponse {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String businessCode;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private  String imageUrl;
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
}

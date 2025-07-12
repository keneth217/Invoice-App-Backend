package com.app.invoice.tenant.mapper;


import com.app.invoice.master.entity.Business;
import com.app.invoice.tenant.dto.BusinessRequest;
import com.app.invoice.tenant.dto.BusinessResponse;

public class BusinessMapper {
    public static Business toCompanyEntity(BusinessRequest company) {
        return Business.builder()
                .name(company.getName())
                .email(company.getEmail())
                .phone(company.getPhone())
                .businessCode(company.getBusinessCode())
                .address(company.getAddress())
                .city(company.getCity())
                .state(company.getState())
                .zipCode(company.getZipCode())
                .country(company.getCountry() != null ? company.getCountry() : "Kenya")
                .imageUrl(company.getImageUrl())
                .invoicePrefix(company.getInvoicePrefix())
                .invoiceFooter(company.getInvoiceFooter())
                .invoiceTerms(company.getInvoiceTerms())
                .invoiceNote(company.getInvoiceNote())
                .receiptPrefix(company.getReceiptPrefix())
                .receiptFooter(company.getReceiptFooter())
                .receiptTerms(company.getReceiptTerms())
                .receiptNote(company.getReceiptNote())
                .voucherPrefix(company.getVoucherPrefix())
                .currencySymbol(company.getCurrencySymbol())
                .website(company.getWebsite())
                .deleted(false)
                .build();
    }

    public static BusinessResponse toCompanyResponse(Business company) {
        return new BusinessResponse(
                company.getId(),
                company.getName(),
                company.getEmail(),
                company.getPhone(),
                company.getBusinessCode(),
                company.getAddress(),
                company.getCity(),
                company.getState(),
                company.getZipCode(),
                company.getCountry(),
                company.getImageUrl(),
                company.getInvoicePrefix(),
                company.getInvoiceFooter(),
                company.getInvoiceTerms(),
                company.getInvoiceNote(),
                company.getReceiptPrefix(),
                company.getReceiptFooter(),
                company.getReceiptTerms(),
                company.getReceiptNote(),
                company.getVoucherPrefix(),
                company.getCurrencySymbol()


        );
    }

}

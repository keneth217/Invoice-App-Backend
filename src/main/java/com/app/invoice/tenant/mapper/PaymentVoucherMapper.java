package com.app.invoice.tenant.mapper;



import com.app.invoice.tenant.dto.PaymentVoucherRequest;
import com.app.invoice.tenant.dto.PaymentVoucherResponse;
import com.app.invoice.tenant.entity.PaymentVoucher;

import java.time.LocalDate;
import java.util.List;

public class PaymentVoucherMapper {

    public static PaymentVoucher toEntity(PaymentVoucherRequest dto) {
        return PaymentVoucher.builder()
                .amountPaid(dto.getAmountPaid())
                .paymentDate(dto.getPaymentDate())
                .voucherNumber(dto.getVoucherNumber())
                .paymentMethod(dto.getPaymentMethod())

                .build();
    }

    public  static PaymentVoucherResponse toResponse(PaymentVoucher paymentVoucher) {
        return PaymentVoucherResponse.builder()
                .id(paymentVoucher.getId())
                .paymentMethod(paymentVoucher.getPaymentMethod())
                .amountPaid(paymentVoucher.getAmountPaid())
                .paymentDate(paymentVoucher.getPaymentDate())
                .voucherNumber(paymentVoucher.getVoucherNumber())
                .referenceNumber(paymentVoucher.getReferenceNumber())
                .paymentDate(LocalDate.now())
                .invoice(paymentVoucher.getInvoice())

                .build();
    }


    public static List<PaymentVoucherResponse> toResponseList(List<PaymentVoucher> paymentVouchers) {
        return paymentVouchers.stream()
                .map(PaymentVoucherMapper::toResponse)
                .toList();
    }

}

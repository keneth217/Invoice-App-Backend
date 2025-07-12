package com.app.invoice.tenant.service;



import com.app.invoice.tenant.dto.PaymentVoucherRequest;
import com.app.invoice.tenant.dto.PaymentVoucherResponse;

import java.util.List;

public interface PaymentVoucherService {
    PaymentVoucherResponse payInvoice(Long invoiceId, PaymentVoucherRequest paymentVoucherRequest);

    PaymentVoucherResponse getPaymentVoucherById(Long id);
    PaymentVoucherResponse updatePaymentVoucher(Long id, PaymentVoucherRequest paymentVoucherRequest);
    PaymentVoucherResponse deletePaymentVoucher(Long id);
    List<PaymentVoucherResponse> getAllPaymentVouchers();
    List<PaymentVoucherResponse> getPaymentVouchersByInvoiceId(Long invoiceId);

}

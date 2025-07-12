package com.app.invoice.utils;


import com.app.invoice.tenant.repos.PaymentVoucherRepository;
import org.springframework.stereotype.Component;

@Component
public class VoucherNumberGenerator {


    private final PaymentVoucherRepository paymentVoucherRepository;

    public VoucherNumberGenerator(PaymentVoucherRepository paymentVoucherRepository) {
        this.paymentVoucherRepository = paymentVoucherRepository;
    }


    public String generate() {
        String lastInvoiceNumber = paymentVoucherRepository.findLastVoucherNumber();
        int nextNumber = 1;

        if (lastInvoiceNumber != null && lastInvoiceNumber.startsWith("VCH-")) {
            try {
                String numericPart = lastInvoiceNumber.substring(4); // e.g., "0005"
                nextNumber = Integer.parseInt(numericPart) + 1;
            } catch (NumberFormatException ignored) {
                // fallback to 1
            }
        }

        return "VCH-" + String.format("%04d", nextNumber);
    }
}

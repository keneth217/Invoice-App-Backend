package com.app.invoice.utils;


import com.app.invoice.tenant.repos.ReceiptRepository;
import org.springframework.stereotype.Component;

@Component
public class ReceiptNumberGenerator {

    private final ReceiptRepository receiptRepository;

    public ReceiptNumberGenerator(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }


    public String generate() {
        String lastInvoiceNumber = receiptRepository.findLastVoucherNumber();
        int nextNumber = 1;

        if (lastInvoiceNumber != null && lastInvoiceNumber.startsWith("RCT-")) {
            try {
                String numericPart = lastInvoiceNumber.substring(4); // e.g., "0005"
                nextNumber = Integer.parseInt(numericPart) + 1;
            } catch (NumberFormatException ignored) {
                // fallback to 1
            }
        }

        return "RCT-" + String.format("%04d", nextNumber);
    }
}

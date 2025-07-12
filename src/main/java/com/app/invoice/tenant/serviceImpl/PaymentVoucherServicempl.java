package com.app.invoice.tenant.serviceImpl;


import com.app.invoice.exception.InvalidBusinessException;
import com.app.invoice.exception.NotFoundException;

import com.app.invoice.tenant.dto.PaymentVoucherRequest;
import com.app.invoice.tenant.dto.PaymentVoucherResponse;
import com.app.invoice.tenant.entity.Invoice;
import com.app.invoice.tenant.entity.PaymentVoucher;
import com.app.invoice.tenant.entity.Receipt;
import com.app.invoice.tenant.mapper.PaymentVoucherMapper;
import com.app.invoice.tenant.repos.InvoiceRepository;
import com.app.invoice.tenant.repos.PaymentVoucherRepository;
import com.app.invoice.tenant.repos.ReceiptRepository;


import com.app.invoice.tenant.services.LedgerService;
import com.app.invoice.tenant.services.PaymentVoucherService;
import com.app.invoice.utils.ReceiptNumberGenerator;
import com.app.invoice.utils.VoucherNumberGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class PaymentVoucherServicempl implements PaymentVoucherService {

    private final InvoiceRepository invoiceRepository;
    private  final PaymentVoucherRepository paymentVoucherRepository;

    private final LedgerService ledgerService;

    private final VoucherNumberGenerator voucherNumberGenerator;
    private final ReceiptNumberGenerator receiptNumberGenerator;
    private final ReceiptRepository receiptRepository;

    public PaymentVoucherServicempl(InvoiceRepository invoiceRepository,
                                    PaymentVoucherRepository paymentVoucherRepository,
                                    LedgerService ledgerService,
                                    VoucherNumberGenerator voucherNumberGenerator, ReceiptNumberGenerator receiptNumberGenerator, ReceiptRepository receiptRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentVoucherRepository = paymentVoucherRepository;
        this.ledgerService = ledgerService;
        this.voucherNumberGenerator = voucherNumberGenerator;

        this.receiptNumberGenerator = receiptNumberGenerator;
        this.receiptRepository = receiptRepository;
    }

    @Override
    @Transactional
    public PaymentVoucherResponse payInvoice(Long invoiceId, PaymentVoucherRequest paymentVoucherRequest) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("Invoice not found with ID: " + invoiceId));

        BigDecimal totalInvoiceAmount = invoice.getTotalAmount();
        BigDecimal currentPayment = paymentVoucherRequest.getAmountPaid();
        System.out.println(totalInvoiceAmount);

// Calculate total paid so far
        BigDecimal totalPaidSoFar = invoice.getPaymentVouchers() != null
                ? invoice.getPaymentVouchers().stream()
                .map(PaymentVoucher::getAmountPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                : BigDecimal.ZERO;

        BigDecimal newTotalPaid = totalPaidSoFar.add(currentPayment);

// ✅ Prevent payment that exceeds invoice total
        if (newTotalPaid.compareTo(totalInvoiceAmount) > 0) {
            throw new InvalidBusinessException("Total payments exceed invoice total amount. Overpayment not allowed.");
        }


        BigDecimal paymentAmount = paymentVoucherRequest.getAmountPaid();
        BigDecimal invoiceAmount = invoice.getTotalAmount();

        BigDecimal previouslyPaid = invoice.getPaymentVouchers() != null
                ? invoice.getPaymentVouchers().stream()
                .map(PaymentVoucher::getAmountPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                : BigDecimal.ZERO;

        BigDecimal totalPaid = previouslyPaid.add(paymentAmount);

        // Update invoice status
        if (totalPaid.compareTo(invoiceAmount) >= 0) {
            invoice.setStatus("PAID");
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus("PARTIALLY_PAID");
        } else {
            invoice.setStatus("UNPAID");
        }

        // Create and save PaymentVoucher
        PaymentVoucher paymentVoucher = PaymentVoucher.builder()
                .voucherNumber(voucherNumberGenerator.generate())
                .amountPaid(paymentAmount)
                .paymentMethod(paymentVoucherRequest.getPaymentMethod())
                .invoice(invoice)
                .build();

        PaymentVoucher savedVoucher = paymentVoucherRepository.save(paymentVoucher);

        // Save invoice status
        invoiceRepository.save(invoice);

        // Update ledger
        ledgerService.updateLedgerPayment(savedVoucher);

        // Generate and save receipt silently (no return)
        Receipt receipt = Receipt.builder()
                .receiptNumber(receiptNumberGenerator.generate())
                .issueDate(LocalDate.now())
                .amountReceived(paymentAmount)
                .paymentMethod(String.valueOf(paymentVoucher.getPaymentMethod()))
                .description("Receipt for Invoice " + invoice.getInvoiceNumber())
                .balanceRemaining(invoiceAmount.subtract(totalPaid))
                .customer(invoice.getCustomer())
                .paymentVoucher(savedVoucher)
                .invoice(invoice)
                .build();

        receiptRepository.save(receipt);

        // ✅ Only return the voucher response
        return PaymentVoucherMapper.toResponse(savedVoucher);
    }





    @Override
    public PaymentVoucherResponse getPaymentVoucherById(Long id) {
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment Voucher not found with ID: " + id));
        return PaymentVoucherMapper.toResponse(paymentVoucher);

    }

    @Override
    public PaymentVoucherResponse updatePaymentVoucher(Long id, PaymentVoucherRequest paymentVoucher) {
        PaymentVoucher existingVoucher = paymentVoucherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment Voucher not found with ID: " + id));

        // Update fields
        existingVoucher.setPaymentDate(paymentVoucher.getPaymentDate());
        existingVoucher.setAmountPaid(paymentVoucher.getAmountPaid());
        existingVoucher.setPaymentMethod(paymentVoucher.getPaymentMethod());
        existingVoucher.setReferenceNumber(paymentVoucher.getReferenceNumber());


        // Save updated voucher
        PaymentVoucher updatedVoucher = paymentVoucherRepository.save(existingVoucher);

        return PaymentVoucherMapper.toResponse(updatedVoucher);

    }

    @Override
    public PaymentVoucherResponse deletePaymentVoucher(Long id) {

        //safe delete
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment Voucher not found with ID: " + id));
        paymentVoucher.setDeleted(true);
        PaymentVoucher deletedVoucher = paymentVoucherRepository.save(paymentVoucher);
        return PaymentVoucherMapper.toResponse(deletedVoucher);
    }

    @Override
    public List<PaymentVoucherResponse> getPaymentVouchersByInvoiceId(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("Invoice not found with ID: " + invoiceId));

        List<PaymentVoucher> vouchers = invoice.getPaymentVouchers();

        if (vouchers == null || vouchers.isEmpty()) {
            return Collections.emptyList();
        }

        return PaymentVoucherMapper.toResponseList(vouchers);
    }


    @Override
    public List<PaymentVoucherResponse> getAllPaymentVouchers() {
        List<PaymentVoucher> paymentVouchers = paymentVoucherRepository.findAllByDeletedFalse();

        if (paymentVouchers.isEmpty()) {
            return Collections.emptyList(); // or throw new NotFoundException("No vouchers found")
        }
        return PaymentVoucherMapper.toResponseList(paymentVouchers);
    }



}

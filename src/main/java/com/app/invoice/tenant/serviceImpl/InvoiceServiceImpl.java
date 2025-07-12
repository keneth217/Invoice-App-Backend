package com.app.invoice.tenant.serviceImpl;


import com.app.invoice.exception.CustomerNotFoundException;
import com.app.invoice.exception.NotFoundException;
import com.app.invoice.tenant.dto.InvoiceRequest;
import com.app.invoice.tenant.dto.InvoiceResponse;
import com.app.invoice.tenant.entity.Customer;
import com.app.invoice.tenant.entity.Invoice;
import com.app.invoice.tenant.entity.InvoiceItem;
import com.app.invoice.tenant.mapper.InvoiceMapper;
import com.app.invoice.tenant.repos.CustomerRepository;
import com.app.invoice.tenant.repos.InvoiceRepository;
import com.app.invoice.tenant.service.InvoiceService;
import com.app.invoice.tenant.service.LedgerService;
import com.app.invoice.utils.InvoiceNumberGenerator;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final LedgerService ledgerService;

    private final CustomerRepository customerRepository;
    private final InvoiceNumberGenerator invoiceNumberGenerator;
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, LedgerService ledgerService, CustomerRepository customerRepository, InvoiceNumberGenerator invoiceNumberGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.ledgerService = ledgerService;
        this.customerRepository = customerRepository;
        this.invoiceNumberGenerator = invoiceNumberGenerator;
    }

    @Override
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        // Validate input first
        if (request == null) {
            throw new NotFoundException("Invoice cannot be null");
        }

        if (request.getCustomer() == null || request.getCustomer().getId() == null) {
            throw new CustomerNotFoundException("Invoice must have a valid customer");
        }

        if (request.getInvoiceItems() == null || request.getInvoiceItems().isEmpty()) {
            throw new NotFoundException("Invoice must contain at least one item");
        }

        // Fetch the actual Customer entity from DB
        Customer customer = customerRepository.findById(request.getCustomer().getId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + request.getCustomer().getId()));

        // Generate Invoice Entity
        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumberGenerator.generate())
                .customer(customer)
                .issueDate(request.getIssueDate())
                .dueDate(request.getDueDate())
                .status("UNPAID") // Default to UNPAID or whatever logic you prefer
                .build();

        List<InvoiceItem> items = request.getInvoiceItems().stream()
                .map(itemDto -> InvoiceItem.builder()
                        .description(itemDto.getDescription())
                        .quantity(itemDto.getQuantity())
                        .unitPrice(itemDto.getUnitPrice())
                        .itemsTotal(itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())))
                        .invoice(invoice) // Set back-reference to invoice
                        .build())
                .toList();

        invoice.setInvoiceItems(items);


        // Calculate total amount if not already done
        BigDecimal total = items.stream()
                .map(InvoiceItem::getItemsTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        invoice.setTotalAmount(total);

        // Save invoice with items
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Update ledger
        ledgerService.updateLedgerWithInvoice(savedInvoice);

        // Return response DTO
        return InvoiceMapper.toResponse(savedInvoice);
    }

    @Override
    public List<InvoiceResponse> getAllInvoices() {
        //get all whose deletd is false
        List<Invoice> invoices = invoiceRepository.findAllByDeletedFalse();
        return invoices.stream()
                .map(InvoiceMapper::toResponse)
                .toList();

    }

    @Override
    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found with ID: " + id));
        return InvoiceMapper.toResponse(invoice);
    }

    @Override
    public InvoiceResponse updateInvoice(Long id, InvoiceRequest invoiceRequest) {
        Invoice existingInvoice = invoiceRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found with ID: " + id));
        // Update fields
        existingInvoice.setInvoiceNumber(invoiceRequest.getInvoiceNumber());
        existingInvoice.setIssueDate(invoiceRequest.getIssueDate());
        existingInvoice.setDueDate(invoiceRequest.getDueDate());
        existingInvoice.setStatus(invoiceRequest.getStatus());

        // Update customer if provided
        if (invoiceRequest.getCustomer() != null && invoiceRequest.getCustomer().getId() != null) {
            Customer customer = customerRepository.findById(invoiceRequest.getCustomer().getId())
                    .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + invoiceRequest.getCustomer().getId()));
            existingInvoice.setCustomer(customer);
        }
        Invoice updatedInvoice = invoiceRepository.save(existingInvoice);
        return InvoiceMapper.toResponse(updatedInvoice);
    }

    @Override
    public InvoiceResponse deleteInvoice(Long id) {
        //safe deleted
        Invoice existingInvoice = invoiceRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found with ID: " + id));
        existingInvoice.setDeleted(true);
        Invoice deletedInvoice = invoiceRepository.save(existingInvoice);
        return InvoiceMapper.toResponse(deletedInvoice);
    }

    @Override
    public List<InvoiceResponse> getInvoicesByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));

        List<Invoice> invoices = invoiceRepository.findAllByDeletedFalse()
                .stream()
                .filter(invoice -> invoice.getCustomer().getId().equals(customer.getId()))
                .toList();

        return invoices.stream()
                .map(InvoiceMapper::toResponse)
                .toList();

    }

    @Override
    public List<InvoiceResponse> getInvoicesByStatus(String status) {
    if (status == null || status.isEmpty()) {
            throw new NotFoundException("Status cannot be null or empty");
        }
    List<Invoice> invoices = invoiceRepository.findAllByDeletedFalse()
            .stream()
            .filter(invoice -> status.equalsIgnoreCase(invoice.getStatus()))
            .toList();
        return invoices.stream()
                .map(InvoiceMapper::toResponse)
                .toList();
    }


}

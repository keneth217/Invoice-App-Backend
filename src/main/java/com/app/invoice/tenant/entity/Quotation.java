package com.app.invoice.tenant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Quotation {
    @Id
    @GeneratedValue
    private Long id;

    private String quotationNumber;
    private LocalDate dateIssued;
    private LocalDate validUntil;
    private BigDecimal totalAmount;
    private String status; // DRAFT, SENT, ACCEPTED, EXPIRED

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL)
    private List<QuotationItem> items;
}

package com.app.invoice.tenant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QuotationItem {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;

    @ManyToOne
    @JsonIgnore
    private Quotation quotation;
}

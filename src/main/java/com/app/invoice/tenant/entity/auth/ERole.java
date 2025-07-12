package com.app.invoice.tenant.entity.auth;

import org.springframework.security.access.prepost.PreAuthorize;

public enum ERole {
    OWNER,  // Creates the pharmacy (chemist), full system access.
    PHARMACY_ADMIN,
    MANAGER,                // Manages staff, inventory, suppliers; second to Owner.
    PHARMACIST,             // Licensed to dispense medications, offer consultations.
    PHARMACY_ASSISTANT,     // Helps with non-dispensing tasks: inventory, sales, customer support.
    CASHIER,                // Handles payments, issues receipts.
    INVENTORY_CLERK
}

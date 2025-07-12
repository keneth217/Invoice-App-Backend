package com.app.invoice.tenant.entity.school_settings;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "pharmacy_settings")
@Getter
@Setter
@ToString(exclude = {"regulatedDrugs"})
public class BusinessSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pharmacy_id", unique = true)
    private String pharmacyId;

    @Column(name = "pharmacy_name", unique = true)
    private String pharmacyName;

    @Column(name = "pharmacy_code", unique = true)
    private String pharmacyCode;

    @Column(name = "address")
    private String address;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "region")
    private String region;

    @Column(name = "county")
    private String county;

    @Column(name = "sub_county")
    private String subCounty;

    @Column(name = "ward")
    private String ward;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @Column(name = "pharmacist_in_charge")
    private String pharmacistInCharge;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "license_expiry_date")
    private LocalDate licenseExpiryDate;
    @Column(name = "operating_hours")
    private String operatingHours;
    @Column(name = "license_valid", nullable = false)
    private boolean licenseValid = true;

    @Column(name = "sms_credits", nullable = false)
    private int smsCredits;

    public void addSmsCredits(int amount) {
        this.smsCredits += amount;
    }

    public void deductSmsCredits(int amount) {
        this.smsCredits -= amount;
    }

    @Column(name = "stock_tracking_enabled", nullable = false)
    private boolean stockTrackingEnabled = true;

    @Column(name = "sales_tracking_enabled", nullable = false)
    private boolean salesTrackingEnabled = true;

    @Column(name = "allow_prescription_uploads", nullable = false)
    private boolean allowPrescriptionUploads = true;

    @Lob
    @Column(columnDefinition = "longblob", name = "pharmacy_logo")
    private byte[] pharmacyLogo;

//    @OneToMany(mappedBy = "pharmacySettings", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<DrugCategory> drugCategories;
//
//    @OneToMany(mappedBy = "pharmacySettings", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Branch> branches;

    @ElementCollection
    @CollectionTable(name = "regulated_drugs", joinColumns = @JoinColumn(name = "pharmacy_settings_id"))
    @Column(name = "drug_name")
    private Set<String> regulatedDrugs = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BusinessSettings that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

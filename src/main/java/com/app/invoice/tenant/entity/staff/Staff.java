package com.app.invoice.tenant.entity.staff;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nurses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_id_number", nullable = false, unique = true)
    private String nationalIdNumber;

    @Column(name = "nurse_number", unique = true)
    private String nurseNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "initials")
    private String initials;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "signature")
    private byte[] signature;

    @Lob
    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @Column(name = "email", unique = true)
    private String email;
}

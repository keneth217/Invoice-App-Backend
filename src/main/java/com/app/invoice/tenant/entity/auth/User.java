package com.app.invoice.tenant.entity.auth;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Long userID;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "PhoneNo", unique = true, nullable = false)
    private String phoneNo;
    @Column(name = "Email")
    private String email;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_types", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "usertype")
    private Set<String> userTypes = new HashSet<>();
    @Column(name = "pharmacyId")
    private String pharmacyId;
    @Column(name = "pharmacyName")
    private String pharmacyName;
    @Column(name = "Password")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Column(name = "AddedBy")
    private long addedBy;
    @Column(name = "Deleted")
    private Boolean deleted = false;
    @Column(name = "passcode")
    private String passcode;
    @Column(name = "reset_token", unique = true)
    private String resetToken;
    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;
    @Column(name = "nurse_id", nullable = true)
    private Long nurseId;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userID != null && userID.equals(user.userID);
    }

    @Override
    public int hashCode() {
        return userID != null ? userID.hashCode() : 0;
    }

}

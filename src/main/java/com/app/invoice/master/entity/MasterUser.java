package com.app.invoice.master.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class MasterUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Long userID;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "PhoneNo", unique = true, nullable = false)
    private String phoneNo;
    @Column(name = "Email", unique = true, nullable = false)
    private String email;
    @Column(name = "User_type")
    private String userType;
    @Column(name = "Password")
    private String password;
    @Column(name = "TenantID", nullable = false)
    private String tenantID = "master";
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Role", nullable = false)
    private MasterRole role = new MasterRole(MasterERole.DEPUTY_ADMIN);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterUser masterUser = (MasterUser) o;
        return userID != null && userID.equals(masterUser.userID);
    }

    @Override
    public int hashCode() {
        return userID != null ? userID.hashCode() : 0;
    }

}

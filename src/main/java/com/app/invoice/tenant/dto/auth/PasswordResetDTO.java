package com.app.invoice.tenant.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {
    private Long userID;
    private String oldPassword;
    private String newPassword;
    private String resetToken; // For reset password
}

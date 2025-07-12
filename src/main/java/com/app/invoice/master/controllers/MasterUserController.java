package com.app.invoice.master.controllers;



import com.app.invoice.exception.DuplicateValueException;
import com.app.invoice.exception.WrongOldPasswordException;
import com.app.invoice.master.dto.MasterUserDTO;
import com.app.invoice.master.dto.PasswordResetDTO;
import com.app.invoice.master.entity.MasterERole;
import com.app.invoice.master.entity.MasterRole;
import com.app.invoice.master.entity.MasterUser;
import com.app.invoice.master.services.MasterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/users")
public class MasterUserController {

    @Autowired
    MasterUserService masterUserService;

    @Autowired
    PasswordEncoder encoder;




    @PostMapping("/create")
    public MasterUserDTO createUser(@RequestBody MasterUserDTO u) {
        if (u.getUserID() != null) {
            throw new IllegalArgumentException("userID should not be provided when creating a new user");
        }

        // Validate phone number uniqueness
        MasterUser existingUserByPhone = masterUserService.findByPhoneNo(u.getPhoneNo()).orElse(null);
        if (existingUserByPhone != null) {
            throw new DuplicateValueException("Phone number has already been used before");
        }

        // Validate email uniqueness
        MasterUser existingUserByEmail = masterUserService.findByEmail(u.getEmail()).orElse(null);
        if (existingUserByEmail != null) {
            throw new DuplicateValueException("Email has already been used before");
        }

        // Validate passcode uniqueness
        if (u.getPasscode() != null) {
            MasterUser existingUserByPasscode = masterUserService.findByPasscode(u.getPasscode()).orElse(null);
            if (existingUserByPasscode != null) {
                throw new DuplicateValueException("Passcode has already been used before");
            }
        }

        // Create a new user
        MasterUser masterUser = new MasterUser();
        masterUser.setPassword(encoder.encode(u.getPhoneNo())); // Set default password

        // Set user details
        masterUser.setEmail(u.getEmail());
        masterUser.setFullName(u.getFullName());
        masterUser.setAddedBy(u.getAddedBy());
        masterUser.setPhoneNo(u.getPhoneNo());
        masterUser.setUserType(u.getUserType());
        masterUser.setPasscode(u.getPasscode());

        // Set role based on usertype
        try {
            masterUser.setRole(new MasterRole(MasterERole.valueOf(u.getUserType().toUpperCase())));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid usertype provided");
        }

        masterUser.setTenantID("master");

        // Save user and return DTO
        MasterUser savedUser = masterUserService.saveUser(masterUser);
        sendEmailNotificationToUser(savedUser); // Send email notification
        return new MasterUserDTO(savedUser);
    }
    private void sendEmailNotificationToUser(MasterUser user) {

    }

    @PostMapping("/list")
    public List<MasterUserDTO> listUsers(){
        List<MasterUser> users = masterUserService.listUsers();
        List<MasterUserDTO> u = new ArrayList<>();
        for (MasterUser masterUser : users){
            u.add(new MasterUserDTO(masterUser));
        }
        return u;
    }

    @PutMapping("/update/{id}")
    public MasterUserDTO updateUser(@PathVariable Long id, @RequestBody MasterUserDTO u) {
        if (!id.equals(u.getUserID())) {
            throw new IllegalArgumentException("Path variable ID does not match userID in the body");
        }

        // Fetch existing user
        MasterUser masterUser = masterUserService.findByID(id);
        if (masterUser == null) {
            throw new IllegalArgumentException("User not found with the provided ID");
        }

        // Validate phone number uniqueness
        MasterUser existingUserByPhone = masterUserService.findByPhoneNo(u.getPhoneNo()).orElse(null);
        if (existingUserByPhone != null && !existingUserByPhone.getUserID().equals(id)) {
            throw new DuplicateValueException("Phone number has already been used before");
        }

        // Validate email uniqueness
        MasterUser existingUserByEmail = masterUserService.findByEmail(u.getEmail()).orElse(null);
        if (existingUserByEmail != null && !existingUserByEmail.getUserID().equals(id)) {
            throw new DuplicateValueException("Email has already been used before");
        }

        // Validate passcode uniqueness
        if (u.getPasscode() != null) {
            MasterUser existingUserByPasscode = masterUserService.findByPasscode(u.getPasscode()).orElse(null);
            if (existingUserByPasscode != null && !existingUserByPasscode.getUserID().equals(id)) {
                throw new DuplicateValueException("Passcode has already been used before");
            }
        }

        // Update user details
        masterUser.setEmail(u.getEmail());
        masterUser.setFullName(u.getFullName());
        masterUser.setAddedBy(u.getAddedBy());
        masterUser.setPhoneNo(u.getPhoneNo());
        masterUser.setUserType(u.getUserType());
        masterUser.setPasscode(u.getPasscode());

        // Update role based on usertype
        try {
            masterUser.setRole(new MasterRole(MasterERole.valueOf(u.getUserType().toUpperCase())));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid usertype provided");
        }

        masterUser.setTenantID("master");

        // Save user and return DTO
        MasterUser savedUser = masterUserService.saveUser(masterUser);
        return new MasterUserDTO(savedUser);
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        // Retrieve the user based on the provided user ID
        MasterUser user = masterUserService.findByID(passwordResetDTO.getUserID());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Validate old password
        if (!encoder.matches(passwordResetDTO.getOldPassword(), user.getPassword())) {
            throw new WrongOldPasswordException("The old password provided is incorrect");
        }

        // Validate new password complexity
        if (!isValidPassword(passwordResetDTO.getNewPassword())) {
            throw new IllegalArgumentException("New password does not meet complexity requirements");
        }

        // Update the password
        user.setPassword(encoder.encode(passwordResetDTO.getNewPassword()));
        masterUserService.saveUser(user);

        // Send confirmation notification
        sendPasswordChangeConfirmation(user);

        return ResponseEntity.ok("Your password has been successfully changed");
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        MasterUser user = masterUserService.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("User with the provided email does not exist"));

        // Generate a secure token
        String resetToken = UUID.randomUUID().toString();

        // Save the token and expiration time
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30)); // Token valid for 30 minutes
        masterUserService.saveUser(user);

        // Send reset email
        sendPasswordResetEmail(user, resetToken);

        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    @PostMapping("/reset_password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDTO resetDTO) {
        // Validate token
        MasterUser user = masterUserService.findByResetToken(resetDTO.getNewPassword()).orElseThrow(() ->
                new IllegalArgumentException("Invalid or expired reset token"));

        // Check token expiration
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        // Validate new password complexity
        if (!isValidPassword(resetDTO.getNewPassword())) {
            throw new IllegalArgumentException("New password does not meet complexity requirements");
        }

        // Update password
        user.setPassword(encoder.encode(resetDTO.getNewPassword()));
        user.setResetToken(null); // Clear the reset token
        user.setResetTokenExpiry(null); // Clear the token expiry
        masterUserService.saveUser(user);

        sendPasswordChangeConfirmation(user);

        return ResponseEntity.ok("Your password has been successfully reset.");
    }
    // Helper method: Validate password complexity
    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*\\d.*") && // At least one digit
                password.matches(".*[!@#$%^&*].*"); // At least one special character
    }

    // Helper method: Send password change confirmation
    private void sendPasswordChangeConfirmation(MasterUser user) {


    }


    private void sendPasswordResetEmail(MasterUser user, String resetToken) {

    }
}

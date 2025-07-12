package com.app.invoice.master.services;


import com.app.invoice.master.entity.MasterERole;
import com.app.invoice.master.entity.MasterRole;
import com.app.invoice.master.entity.MasterUser;
import com.app.invoice.master.repos.MasterRoleRepository;
import com.app.invoice.master.repos.MasterUserRepository;
import com.app.invoice.security.services.UserDetailsImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MasterUserService {

    @Autowired
    MasterUserRepository masterUserRepository;

    @Autowired
    MasterRoleRepository masterRoleRepository;

    @Autowired
    PasswordEncoder encoder;





    @PostConstruct
    public void createDefaultMasterUser() {
        Optional<MasterUser> existingUser = masterUserRepository.findByEmail("kenethkipyegon03@gmail.com");

        if (existingUser.isEmpty()) {
            // Create the default master user if not found
            MasterUser masterAdmin = new MasterUser();
            masterAdmin.setUserType(MasterERole.ADMIN.name());
            masterAdmin.setEmail("kenethkipyegon03@gmail.com");
            masterAdmin.setFullName("Keneth Kipyegon");
            masterAdmin.setPhoneNo("0711766223");
            masterAdmin.setRole(new MasterRole(MasterERole.ADMIN));
            masterAdmin.setPasscode(generateUniqueFourDigitNumber(new HashSet<>(masterUserRepository.findDistinctPasscodes())));
            masterAdmin.setPassword(encoder.encode("keneth"));
            masterAdmin.setTenantID("pharmacy-master");
            saveUser(masterAdmin);

            // Send email notification to the default admin
            sendEmailNotificationToDefaultAdmin(masterAdmin);

            // Optionally, log the creation of the default user
            System.out.println("Default master user created and email sent: " + masterAdmin.getEmail());
        } else {
            // Optionally, log if the user already exists
            System.out.println("Default master user already exists.");
        }
    }

    private void sendEmailNotificationToDefaultAdmin(MasterUser admin) {

    }

    public MasterUser getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNo = ((UserDetailsImpl) authentication.getPrincipal()).getPhoneNo();
        return findByPhoneNo(phoneNo).orElse(null);
    }

    public MasterUser findByID(Long id){
        return masterUserRepository.findById(id).orElse(null);
    }

    public MasterUser saveUser(MasterUser masterUser){
        MasterRole masterRole = masterRoleRepository.findByName(MasterERole.valueOf(masterUser.getUserType().toUpperCase()))
                .orElseGet(() -> {
                    // Save the masterRole if it does not exist
                    MasterRole newRole = new MasterRole(masterUser.getRole().getName());
                    return masterRoleRepository.save(newRole);
                });
        masterUser.setRole(masterRole);
        MasterUser u;
        // Convert the array to a HashSet for faster lookup
        HashSet<String> existingNumbersSet = new HashSet<>(masterUserRepository.findDistinctPasscodes());
        if (masterUser.getUserID() != null) {
            u = masterUserRepository.findById(masterUser.getUserID()).orElse(null);
            if (u != null) {
                if (masterUser.getPasscode() == null) {
                    masterUser.setPasscode(u.getPasscode());
                }
                if (masterUser.getPassword() == null){
                    masterUser.setPassword(u.getPassword());
                }
            }
        }
        if (masterUser.getPasscode() == null) {
            masterUser.setPasscode(generateUniqueFourDigitNumber(existingNumbersSet));
        }
        return masterUserRepository.save(masterUser);
    }

    public Optional<MasterUser> findByPhoneNo(String phoneNo) {
        MasterUser masterUser = masterUserRepository.findByPhoneNo(phoneNo).orElse(null);
        if (masterUser != null){
            if (masterUser.getPasscode() == null){
//                masterUser.setPasscode(generateUniqueFourDigitNumber(new HashSet<>(masterUserRepository.findDistinctPasscodes())));
                masterUser = saveUser(masterUser);
            }
        }
        return Optional.ofNullable(masterUser);
    }

    public Optional<MasterUser> findByPasscode(String passcode){
        return masterUserRepository.findByPasscode(passcode);
    }

    public Optional<MasterUser> findByEmail(String email) {
        MasterUser masterUser = masterUserRepository.findByEmail(email).orElse(null);
        if (masterUser != null){
            if (masterUser.getPasscode() == null){
//                user.setPasscode(generateUniqueFourDigitNumber(new HashSet<>(masterUserRepository.findDistinctPasscodes())));
                masterUser = saveUser(masterUser);
            }
        }
        return Optional.ofNullable(masterUser);
//        return masterUserRepository.findByEmail(email);
    }

    public List<MasterUser> listUsers() {
        return masterUserRepository.findAll();
    }

    private static String generateUniqueFourDigitNumber(HashSet<String> existingNumbersSet) {
        Random random = new Random();
        String number;

        // Keep generating numbers until we find one that isn't in the set
        do {
            number = String.format("%04d", random.nextInt(10000)); // Generate a number between 0000 and 9999
        } while (existingNumbersSet.contains(number));

        return number;
    }

    public Optional<MasterUser> findByResetToken(String resetToken) {
        return masterUserRepository.findByResetToken(resetToken);
    }
}

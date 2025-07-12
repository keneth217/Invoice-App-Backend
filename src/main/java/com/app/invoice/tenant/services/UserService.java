package com.app.invoice.tenant.services;

import com.app.invoice.security.services.UserDetailsImpl;
import com.app.invoice.tenant.entity.auth.ERole;
import com.app.invoice.tenant.entity.auth.Role;
import com.app.invoice.tenant.entity.auth.User;
import com.app.invoice.tenant.repos.auth.RoleRepository;
import com.app.invoice.tenant.repos.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNo = ((UserDetailsImpl) authentication.getPrincipal()).getPhoneNo();
        return findByPhoneNo(phoneNo).orElse(null);
    }

    public User findByID(Long id){
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public User saveUser(User user) {
        // Handle roles based on usertypes
        Set<Role> roles = new HashSet<>();
        for (String usertype : user.getUserTypes()) {
            Role role = roleRepository.findByName(ERole.valueOf(usertype.toUpperCase()))
                    .orElse(null);

            if (role == null) {
                role = new Role(ERole.valueOf(usertype.toUpperCase()));
                role = roleRepository.save(role);
            }

            roles.add(role);
        }
        user.setRoles(roles);

        HashSet<String> existingNumbersSet = new HashSet<>(userRepository.findDistinctPasscodes());

        User existingUser = null;
        if (user.getUserID() != null) {
            existingUser = userRepository.findById(user.getUserID()).orElse(null);
            if (existingUser != null) {
                // Update the existing user entity
                if (user.getPasscode() == null) {
                    user.setPasscode(existingUser.getPasscode());
                }
                if (user.getPassword() == null) {
                    user.setPassword(existingUser.getPassword());
                }
                if (user.getUserTypes().isEmpty()) {
                    user.setUserTypes(existingUser.getUserTypes());
                }

                Set<Role> existingRoles = existingUser.getRoles();
                existingRoles.addAll(roles);
                user.setRoles(existingRoles);
            }
        }

        if (user.getPasscode() == null) {
            user.setPasscode(generateUniqueFourDigitNumber(existingNumbersSet));
        }

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {

            throw new RuntimeException("Duplicate entry error while saving user", e);
        }
    }

    public Optional<User> findByPhoneNo(String phoneNo) {
        User user = userRepository.findByPhoneNo(phoneNo).orElse(null);
        if (user != null){
            if (user.getPasscode() == null){
//                user.setPasscode(generateUniqueFourDigitNumber(new HashSet<>(userRepository.findDistinctPasscodes())));
                user = saveUser(user);
            }
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> findByPasscode(String passcode){
        return userRepository.findByPasscode(passcode);
    }

    public Optional<User> findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null){
            if (user.getPasscode() == null){
//                user.setPasscode(generateUniqueFourDigitNumber(new HashSet<>(userRepository.findDistinctPasscodes())));
                user = saveUser(user);
            }
        }
        return Optional.ofNullable(user);
//        return userRepository.findByEmail(email);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public static String generateUniqueFourDigitNumber(HashSet<String> existingNumbersSet) {
        Random random = new Random();
        String number;

        // Keep generating numbers until we find one that isn't in the set
        do {
            number = String.format("%04d", random.nextInt(10000)); // Generate a number between 0000 and 9999
        } while (existingNumbersSet.contains(number));

        return number;
    }

    public Optional<User> findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    public Optional<User> findByTeacherId(Long teacherId) {
        return userRepository.findByNurseId(teacherId);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public User assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ERole roleEnum;
        try {
            roleEnum = ERole.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName);
        }

        Role role = roleRepository.findByName(roleEnum)
                .orElseGet(() -> roleRepository.save(new Role(roleEnum)));

        user.getRoles().add(role);

        // Ensure userTypes contains the role name
        user.getUserTypes().add(roleEnum.name());

        return userRepository.save(user);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public User removeRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ERole roleEnum;
        try {
            roleEnum = ERole.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName);
        }

        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().remove(role);

        user.getUserTypes().remove(roleEnum.name());

        return userRepository.save(user);
    }
        public List<String> getAllRoleNames() {
            return Arrays.stream(ERole.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
        }

}

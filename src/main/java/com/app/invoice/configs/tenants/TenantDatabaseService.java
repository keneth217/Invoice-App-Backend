package com.app.invoice.configs.tenants;


import com.app.invoice.master.entity.Business;
import com.app.invoice.master.services.BusinessRegistrationStatusService;
import com.app.invoice.tenant.entity.auth.ERole;
import com.app.invoice.tenant.entity.auth.Role;
import com.app.invoice.tenant.entity.auth.User;
import com.app.invoice.tenant.dto.StaffDto;
import com.app.invoice.tenant.services.staff.StaffService;
import com.app.invoice.tenant.repos.auth.RoleRepository;
import com.app.invoice.tenant.services.UserService;
import com.app.invoice.tenant.services.defaultSettings.DefaultSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class TenantDatabaseService {

    @Autowired
    private JdbcTemplate centralJdbcTemplate; // JdbcTemplate connected to the central DB

    @Autowired
    TenantDataSourceProvider provider;

    @Autowired
    UserService userService;

    @Autowired
    StaffService nurseService;


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @Autowired
    private DefaultSettingsService defaultSettingsService;


    private final BusinessRegistrationStatusService statusService;

    public TenantDatabaseService(BusinessRegistrationStatusService statusService) {
        this.statusService = statusService;
    }


    @Async("taskExecutor")
    public CompletableFuture<String> createDatabase(Business business) {
        String tenantId = business.getBusinessCode(); // Use business code as tenant ID
        String businessName = business.getName();
        String businessEmail = business.getEmail();
        String businessCode = business.getBusinessCode();
        String businessPhoneNo = business.getPhone();
        String businessAddress = business.getAddress();
        try {
            statusService.updateStatus(tenantId, "Creating database...");
            String sql = "CREATE DATABASE IF NOT EXISTS invoice_" + businessCode.toLowerCase();
            centralJdbcTemplate.execute(sql);

            statusService.updateStatus(tenantId, "Setting up tenant data source...");
            // Set the database URL for the business
            String dbUrl = "jdbc:mysql://localhost:3306/invoice_" + businessCode.toLowerCase() + "?createDatabaseIfNotExist=true";
            business.setDbUrl(dbUrl);
            
            provider.createDataSourceForTenant(business);
            TenantContext.setCurrentTenant(tenantId);

            statusService.updateStatus(tenantId, "Creating admin user...");
            User admin = new User();
            Set<String> usertypes = new HashSet<>();
            usertypes.add(ERole.PHARMACY_ADMIN.name());
            admin.setUserTypes(usertypes);
            admin.setFullName(business.getBusinessOwner());
            admin.setEmail(business.getEmail());
            admin.setPhoneNo(business.getPhone());

            Role principalRole = roleRepository.findByName(ERole.PHARMACY_ADMIN)
                    .orElseGet(() -> {
                        Role newRole = new Role(ERole.PHARMACY_ADMIN);
                        return roleRepository.save(newRole);
                    });

            Set<Role> roles = new HashSet<>();
            roles.add(principalRole);
            admin.setRoles(roles);
            admin.setPharmacyId(business.getBusinessCode());
            admin.setPharmacyName(business.getName());
            admin.setPassword(encoder.encode(business.getPhone()));

            admin = userService.saveUser(admin);

            statusService.updateStatus(tenantId, "Creating staff or doctor profile...");
            StaffDto nurseDTO = new StaffDto();
            nurseDTO.setFullName(admin.getFullName());
            nurseDTO.setEmail(admin.getEmail());
            nurseDTO.setPhoneNumber(admin.getPhoneNo());
            nurseDTO.setNationalIdNumber(business.getPhone());

            nurseDTO.setAddress(business.getAddress());

            StaffDto savedNurse = nurseService.saveNurse(nurseDTO);

            admin.setNurseId(savedNurse.getId());
            userService.saveUser(admin);

            statusService.updateStatus(tenantId, "Sending email notification...");
            sendEmailNotificationToPrincipal(admin, business);

            defaultSettingsService.applyDefaultSettings(businessCode, businessName, businessEmail, businessCode, businessPhoneNo, businessAddress);

            TenantContext.clear();
            statusService.updateStatus(tenantId, "Completed");

            return CompletableFuture.completedFuture("Database creation completed for " + tenantId);
        } catch (Exception e) {
            statusService.updateStatus(tenantId, "Error: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    private void sendEmailNotificationToPrincipal(User admin, Business school) {

    }
}
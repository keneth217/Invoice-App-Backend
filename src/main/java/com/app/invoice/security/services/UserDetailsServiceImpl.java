package com.app.invoice.security.services;

import com.app.invoice.configs.tenants.TenantContext;
import com.app.invoice.exception.BusinessDeactivatedException;
import com.app.invoice.master.entity.Business;
import com.app.invoice.master.entity.MasterUser;
import com.app.invoice.master.services.MasterUserService;
import com.app.invoice.master.services.BusinessService;
import com.app.invoice.tenant.entity.auth.User;
import com.app.invoice.tenant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private BusinessService pharmacyService;

    @Autowired
    private MasterUserService masterUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String tenantId = TenantContext.getCurrentTenant();

        if ("master".equalsIgnoreCase(tenantId)) {
            MasterUser masterUser = masterUserService.findByPhoneNo(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Master user not found with phone no: " + username));

            return UserDetailsImpl.buildForMasterUser(masterUser);
        } else {
            User tenantUser = userService.findByPhoneNo(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Tenant user not found with phone no: " + username));

            Business pharmacy = pharmacyService.findByBusinessId(UUID.fromString(tenantUser.getPharmacyId()));

            if (pharmacy == null || Boolean.FALSE.equals(pharmacy.getActivated())) {
                throw new BusinessDeactivatedException("Business is deactivated. Contact admin.");
            }

            return UserDetailsImpl.build(tenantUser, pharmacy);
        }
    }
}

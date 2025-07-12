package com.app.invoice.master.controllers;



import com.app.invoice.master.dto.JwtResponse;
import com.app.invoice.master.dto.LoginRequest;
import com.app.invoice.master.repos.MasterRoleRepository;
import com.app.invoice.master.services.MasterUserService;
import com.app.invoice.master.services.BusinessService;
import com.app.invoice.security.jwt.JwtUtils;
import com.app.invoice.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/auth")
public class MasterAuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    BusinessService schoolService;

    @Autowired
    MasterUserService masterUserService;

    @Autowired
    MasterRoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNo(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    "",
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getPhoneNo(),
                    roles
            ));
        } catch (BadCredentialsException e) {
            // You can return a custom error response here
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/signout")
    public void signout(){

    }

}
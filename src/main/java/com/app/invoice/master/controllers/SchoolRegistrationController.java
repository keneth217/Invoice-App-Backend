package com.app.invoice.master.controllers;

import com.app.invoice.master.services.BusinessRegistrationStatusService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/school_registration")
public class SchoolRegistrationController {

    private final BusinessRegistrationStatusService statusService;

    public SchoolRegistrationController(BusinessRegistrationStatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/status/{schoolId}")
    public String getRegistrationStatus(@PathVariable String schoolId) {
        return statusService.getStatus(schoolId);
    }
}

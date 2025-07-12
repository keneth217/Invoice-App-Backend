package com.app.invoice.master.services;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BusinessRegistrationStatusService {
    private final ConcurrentHashMap<String, String> statusMap = new ConcurrentHashMap<>();

    public void updateStatus(String schoolId, String status) {
        if (schoolId == null || schoolId.isEmpty()) {
            System.err.println("❌ ERROR: Attempted to update status with null/empty School ID!");
            return;
        }
        statusMap.put(schoolId, status);
        System.out.println("✅ Status Updated -> " + schoolId + ": " + status);
    }

    public String getStatus(String schoolId) {
        String status = statusMap.getOrDefault(schoolId, "Pending");
        System.out.println("ℹ️ Fetching Status -> " + schoolId + ": " + status);
        return status;
    }

}

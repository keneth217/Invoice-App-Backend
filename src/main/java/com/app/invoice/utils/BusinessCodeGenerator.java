package com.app.invoice.utils;


import com.app.invoice.master.repos.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BusinessCodeGenerator {

    private final BusinessRepository businessRepository;
    private final AtomicInteger counter;

    @Autowired
    public BusinessCodeGenerator(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;

        String lastCode = businessRepository.findLastBusinessCode();
        int lastUsed = extractNumber(lastCode);
        this.counter = new AtomicInteger(lastUsed);
    }

    public String generate() {
        int next = counter.incrementAndGet();
        return "BUS-" + String.format("%04d", next);
    }

    private int extractNumber(String code) {
        try {
            if (code != null && code.startsWith("BUS-")) {
                return Integer.parseInt(code.substring(4));
            }
        } catch (NumberFormatException ignored) {}
        return 0;
    }
}

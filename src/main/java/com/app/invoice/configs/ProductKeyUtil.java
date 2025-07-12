package com.app.invoice.configs;

import ch.qos.logback.core.util.SystemInfo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class ProductKeyUtil {


        private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

//        public static String generateProductKey(String schoolID) {
//            SystemInfo systemInfo = new SystemInfo();
//            HardwareAbstractionLayer hardware = systemInfo.getHardware();
//
//            String motherboardSerial = hardware.getComputerSystem().getBaseboard().getSerialNumber();
//            String processorId = hardware.getProcessor().getProcessorIdentifier().getProcessorID();
//
//            return schoolID + "-" + motherboardSerial + "-" + processorId;
//        }
//
//        public static String hashProductKey(String productKey) {
//            return encoder.encode(productKey);
//        }
//
//        public static boolean isValidProductKey(String rawKey, String hashedKey) {
//            return encoder.matches(rawKey, hashedKey);
//        }
}


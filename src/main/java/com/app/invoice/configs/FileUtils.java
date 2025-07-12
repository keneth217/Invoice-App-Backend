package com.app.invoice.configs;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    public static MultipartFile createMultipartFile(byte[] fileBytes, String filename, String contentType) {
        return new ByteArrayMultipartFile(fileBytes, filename, contentType);
    }
}

package com.app.invoice.configs;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.lang.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayMultipartFile implements MultipartFile {
    private final byte[] fileBytes;
    private final String filename;
    private final String contentType;

    public ByteArrayMultipartFile(@NonNull byte[] fileBytes, String filename, String contentType) {
        this.fileBytes = fileBytes;
        this.filename = filename;
        this.contentType = contentType;
    }

    @Override
    @NonNull
    public String getName() {
        return filename != null ? filename : "";
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileBytes.length == 0;
    }

    @Override
    public long getSize() {
        return fileBytes.length;
    }

    @Override
    @NonNull
    public byte[] getBytes() throws IOException {
        return fileBytes;
    }

    @Override
    @NonNull
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileBytes);
    }

    @Override
    public void transferTo(@NonNull java.io.File dest) throws IOException {
        throw new UnsupportedOperationException("transferTo is not supported in this implementation.");
    }
}

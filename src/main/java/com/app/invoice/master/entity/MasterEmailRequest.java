package com.app.invoice.master.entity;

import lombok.Data;

import java.util.List;

@Data
public class MasterEmailRequest {
    private String subject;
    private String htmlContent;
    private String senderName;
    private String senderEmail;
    private List<String> recipientEmails;

    // Getters and Setters

}

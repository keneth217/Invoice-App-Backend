package com.app.invoice.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class ErrorResponse {
    private String message;
    private  int statusCode;
    private String error;
    private String path= "";
}


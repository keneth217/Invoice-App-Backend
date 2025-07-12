package com.app.invoice.master.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Test Endpoints", description = "Public test endpoints for API verification")
public class TestController {

    @Operation(
        summary = "Health check endpoint",
        description = "Simple health check to verify the API is running"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API is healthy",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "status": "UP",
                        "timestamp": "2024-01-15T10:30:00",
                        "message": "Invoice Management API is running"
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Invoice Management API is running");
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Test connection endpoint",
        description = "Test endpoint to verify connectivity without authentication"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Connection successful",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "success": true,
                        "message": "Connection test successful",
                        "timestamp": "2024-01-15T10:30:00",
                        "version": "1.0.0"
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Connection test successful");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Echo endpoint",
        description = "Echo endpoint that returns the input data for testing"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Echo successful",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "echo": "Hello World",
                        "timestamp": "2024-01-15T10:30:00",
                        "received": true
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> echo(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("echo", data);
        response.put("timestamp", LocalDateTime.now());
        response.put("received", true);
        return ResponseEntity.ok(response);
    }
}

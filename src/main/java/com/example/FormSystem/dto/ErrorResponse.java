package com.example.FormSystem.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private boolean success;
    private Map<String, Object> errors;
    private String path;
    private LocalDateTime timestamp;

    public ErrorResponse() {
        this.success = false;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(Map<String, Object> errors, String path) {
        this();
        this.errors = errors;
        this.path = path;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

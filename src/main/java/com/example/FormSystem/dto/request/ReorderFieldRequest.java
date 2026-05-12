package com.example.FormSystem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class ReorderFieldRequest {
    @NotEmpty(message = "Field IDs list cannot be empty")
    private List<Long> fieldIds;

    public ReorderFieldRequest() {
    }

    public List<Long> getFieldIds() {
        return fieldIds;
    }

    public void setFieldIds(List<Long> fieldIds) {
        this.fieldIds = fieldIds;
    }
}

package com.example.FormSystem.dto.request;

import com.example.FormSystem.constant.MessageConstant;
import jakarta.validation.constraints.NotNull;

public class FieldValueRequest {

    @NotNull(message = MessageConstant.SUBMISSION_FIELD_ID_REQUIRED)
    private Long fieldId;

    private String value;

    public FieldValueRequest() {
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

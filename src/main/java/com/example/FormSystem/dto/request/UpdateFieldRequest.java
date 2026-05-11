package com.example.FormSystem.dto.request;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.enums.FieldType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class UpdateFieldRequest {

    @NotBlank(message = MessageConstant.FIELD_LABEL_REQUIRED)
    private String fieldLabel;

    @NotNull(message = MessageConstant.FIELD_TYPE_REQUIRED)
    private FieldType fieldType;

    @NotNull(message = MessageConstant.FIELD_ORDER_REQUIRED)
    @Min(value = 1, message = MessageConstant.FIELD_ORDER_MIN)
    private Integer fieldOrder;

    @NotNull(message = MessageConstant.FIELD_IS_REQUIRED_REQUIRED)
    private Boolean isRequired;

    private List<String> options;

    @JsonIgnore
    @AssertTrue(message = MessageConstant.FIELD_OPTIONS_NOT_ALLOWED)
    public boolean isOptionsAllowed() {
        if (fieldType != FieldType.SELECT && options != null && !options.isEmpty()) {
            return false;
        }
        return true;
    }

    @JsonIgnore
    @AssertTrue(message = MessageConstant.FIELD_OPTIONS_REQUIRED)
    public boolean isOptionsProvided() {
        if (fieldType == FieldType.SELECT && (options == null || options.isEmpty())) {
            return false;
        }
        return true;
    }

    public UpdateFieldRequest() {
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}

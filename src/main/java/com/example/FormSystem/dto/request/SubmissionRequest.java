package com.example.FormSystem.dto.request;

import com.example.FormSystem.constant.MessageConstant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class SubmissionRequest {

    @NotEmpty(message = MessageConstant.SUBMISSION_VALUES_EMPTY)
    @Valid
    private List<FieldValueRequest> values;

    public SubmissionRequest() {
    }

    public List<FieldValueRequest> getValues() {
        return values;
    }

    public void setValues(List<FieldValueRequest> values) {
        this.values = values;
    }
}

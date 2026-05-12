package com.example.FormSystem.dto.request;

import com.example.FormSystem.constant.MessageConstant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class SubmissionRequest {

    @NotNull(message = MessageConstant.SUBMISSION_USER_ID_REQUIRED)
    private Long userId;

    @NotEmpty(message = MessageConstant.SUBMISSION_VALUES_EMPTY)
    @Valid
    private List<FieldValueRequest> values;

    public SubmissionRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<FieldValueRequest> getValues() {
        return values;
    }

    public void setValues(List<FieldValueRequest> values) {
        this.values = values;
    }
}

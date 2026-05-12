package com.example.FormSystem.dto.response;

import java.time.LocalDateTime;

import com.example.FormSystem.entity.Form;

public class SubmissionResponseDTO {
    private Long submissionId;
    private FormDtoResponse form;
    private LocalDateTime submittedAt;

    public SubmissionResponseDTO() {
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public FormDtoResponse getForm() {
        return form;
    }

    public void setForm(FormDtoResponse form) {
        this.form = form;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}

package com.example.FormSystem.service;

import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.entity.Submission;

public interface SubmissionService {
    Submission submitForm(Long formId, SubmissionRequest request);
}

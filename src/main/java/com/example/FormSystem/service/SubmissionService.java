package com.example.FormSystem.service;

import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.entity.Submission;
import java.util.List;

public interface SubmissionService {
    Submission submitForm(Long formId, SubmissionRequest request);

    List<Submission> getAllSubmissions();

    List<Submission> getSubmissionsByUserId(Long userId);
}

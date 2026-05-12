package com.example.FormSystem.service;

import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.Submission;

public interface SubmissionService {
    Submission submitForm(Long formId, SubmissionRequest request);

    PageResponse<FormDtoResponse> getSubmissionsForCurrentUser(int page, int size);

    PageResponse<FormDtoResponse> getAllSubmissions(int page, int size);
}

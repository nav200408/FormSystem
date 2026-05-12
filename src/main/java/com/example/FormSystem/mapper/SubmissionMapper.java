package com.example.FormSystem.mapper;

import com.example.FormSystem.dto.response.SubmissionResponseDTO;
import com.example.FormSystem.entity.Submission;


public class SubmissionMapper {

    public static SubmissionResponseDTO toDTO(Submission submission) {
        SubmissionResponseDTO dto = new SubmissionResponseDTO();
        dto.setSubmissionId(submission.getSubmissionId());
        dto.setForm(FormMapper.toResponse(submission.getForm()));
        dto.setSubmittedAt(submission.getSubmittedAt());
        return dto;
    }
}

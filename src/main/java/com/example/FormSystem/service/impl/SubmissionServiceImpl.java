package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.FieldValueRequest;
import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.*;
import com.example.FormSystem.mapper.FormMapper;
import com.example.FormSystem.repository.FormRepository;
import com.example.FormSystem.repository.SubmissionRepository;
import com.example.FormSystem.service.SubmissionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private FormRepository formRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
            FormRepository formRepository) {
        this.submissionRepository = submissionRepository;
        this.formRepository = formRepository;
    }

    @Override
    @Transactional
    public Submission submitForm(Long formId, SubmissionRequest request) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstant.FORM_NOT_FOUND + formId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Field> formFields = form.getFields();
        List<SubmissionValue> submissionValues = new ArrayList<>();
        Map<Long, String> requestValues = request.getValues().stream()
                .collect(Collectors.toMap(FieldValueRequest::getFieldId, FieldValueRequest::getValue));

        for (Field field : formFields) {
            String value = requestValues.get(field.getFieldId());

            if (value != null && !value.trim().isEmpty()) {
                SubmissionValue subValue = new SubmissionValue();
                subValue.setField(field);
                subValue.setValue(value);
                submissionValues.add(subValue);
            }
        }

        Submission submission = new Submission();
        submission.setForm(form);
        submission.setUser(user);
        submission.setValues(submissionValues);

        for (SubmissionValue val : submissionValues) {
            val.setSubmission(submission);
        }

        return submissionRepository.save(submission);
    }

    @Override
    public PageResponse<FormDtoResponse> getSubmissionsForCurrentUser(int page, int size) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int pageIndex = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageIndex, size);

        Page<Form> formPage = formRepository.findFormsSubmittedByUserId(user.getId(), pageable);

        List<FormDtoResponse> content = formPage.getContent().stream()
                .map(FormMapper::toResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(content, formPage.getNumber() + 1, formPage.getSize(), formPage.hasNext());
    }

    @Override
    public PageResponse<FormDtoResponse> getAllSubmissions(int page, int size) {
        int pageIndex = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageIndex, size);

        Page<Submission> submissionPage = submissionRepository.findAll(pageable);

        List<FormDtoResponse> content = submissionPage.getContent().stream()
                .map(s -> FormMapper.toResponse(s.getForm()))
                .distinct()
                .collect(Collectors.toList());

        return new PageResponse<>(content, submissionPage.getNumber() + 1, submissionPage.getSize(),
                submissionPage.hasNext());
    }
}

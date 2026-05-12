package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.FieldValueRequest;
import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.entity.*;
import com.example.FormSystem.repository.FormRepository;
import com.example.FormSystem.repository.SubmissionRepository;
import com.example.FormSystem.repository.UserRepository;
import com.example.FormSystem.service.SubmissionService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserRepository userRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
            FormRepository formRepository,
            UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.formRepository = formRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Submission submitForm(Long formId, SubmissionRequest request) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstant.FORM_NOT_FOUND + formId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(MessageConstant.USER_NOT_FOUND + request.getUserId()));

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
}

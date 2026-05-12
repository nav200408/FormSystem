package com.example.FormSystem.validation;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.FieldValueRequest;
import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.repository.FormRepository;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class SubmissionValidationMiddleware {
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private SubmissionValidation submissionValidation;

    public SubmissionValidationMiddleware(FormRepository formRepository, SubmissionValidation submissionValidation) {
        this.formRepository = formRepository;
        this.submissionValidation = submissionValidation;
    }

    @Before("execution(* com.example.FormSystem.controller.SubmissionController.submitForm(..)) && args(id, request)")
    public void validateSubmission(JoinPoint joinPoint, Long id, SubmissionRequest request) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstant.FORM_NOT_FOUND + id));
        List<Field> formFields = form.getFields();
        Map<Long, String> requestValues = request.getValues().stream()
                .collect(Collectors.toMap(FieldValueRequest::getFieldId, FieldValueRequest::getValue));

        for (Field field : formFields) {
            String value = requestValues.get(field.getFieldId());

            submissionValidation.validateRequired(field, value);
            submissionValidation.validateFieldValue(field, value);
        }
    }
}

package com.example.FormSystem.validation;

import com.example.FormSystem.dto.request.FieldValueRequest;
import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.repository.FormRepository;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class SubmissionValidationMiddlewareTest {

    @Mock
    private FormRepository formRepository;

    @Mock
    private SubmissionValidation submissionValidation;

    @Mock
    private JoinPoint joinPoint;

    @InjectMocks
    private SubmissionValidationMiddleware middleware;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateSubmission_Success() {
        Long formId = 1L;
        SubmissionRequest request = new SubmissionRequest();
        
        FieldValueRequest valRequest = new FieldValueRequest();
        valRequest.setFieldId(10L);
        valRequest.setValue("Some Value");
        request.setValues(Arrays.asList(valRequest));

        Form form = new Form();
        Field field = new Field();
        field.setFieldId(10L);
        form.setFields(Arrays.asList(field));

        when(formRepository.findById(formId)).thenReturn(Optional.of(form));

        middleware.validateSubmission(joinPoint, formId, request);

        verify(formRepository, times(1)).findById(formId);
        verify(submissionValidation, times(1)).validateRequired(field, "Some Value");
        verify(submissionValidation, times(1)).validateFieldValue(field, "Some Value");
    }

    @Test
    public void testValidateSubmission_FormNotFound() {
        Long formId = 1L;
        SubmissionRequest request = new SubmissionRequest();

        when(formRepository.findById(formId)).thenReturn(Optional.empty());

        try {
            middleware.validateSubmission(joinPoint, formId, request);
        } catch (Exception e) {
            // Expected
        }

        verify(formRepository, times(1)).findById(formId);
        verify(submissionValidation, never()).validateRequired(any(), any());
    }
}

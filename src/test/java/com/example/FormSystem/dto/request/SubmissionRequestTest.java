package com.example.FormSystem.dto.request;

import com.example.FormSystem.constant.MessageConstant;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SubmissionRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidSubmissionRequest() {
        SubmissionRequest request = new SubmissionRequest();
        List<FieldValueRequest> values = new ArrayList<>();
        
        FieldValueRequest val = new FieldValueRequest();
        val.setFieldId(1L);
        val.setValue("Test Value");
        values.add(val);
        
        request.setValues(values);

        Set<ConstraintViolation<SubmissionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Should have no violations for valid request");
    }

    @Test
    public void testEmptyValues() {
        SubmissionRequest request = new SubmissionRequest();
        request.setValues(new ArrayList<>());

        Set<ConstraintViolation<SubmissionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Should have violations for empty values");
        
        ConstraintViolation<SubmissionRequest> violation = violations.iterator().next();
        assertEquals(MessageConstant.SUBMISSION_VALUES_EMPTY, violation.getMessage());
    }

    @Test
    public void testNullValues() {
        SubmissionRequest request = new SubmissionRequest();
        request.setValues(null);

        Set<ConstraintViolation<SubmissionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Should have violations for null values");
        
        ConstraintViolation<SubmissionRequest> violation = violations.iterator().next();
        assertEquals(MessageConstant.SUBMISSION_VALUES_EMPTY, violation.getMessage());
    }

    @Test
    public void testNestedValidation_NullFieldId() {
        SubmissionRequest request = new SubmissionRequest();
        List<FieldValueRequest> values = new ArrayList<>();
        
        FieldValueRequest val = new FieldValueRequest();
        val.setFieldId(null); // Invalid
        val.setValue("Test Value");
        values.add(val);
        
        request.setValues(values);

        Set<ConstraintViolation<SubmissionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Should have violations for null fieldId in nested object");
        
        ConstraintViolation<SubmissionRequest> violation = violations.iterator().next();
        assertEquals(MessageConstant.SUBMISSION_FIELD_ID_REQUIRED, violation.getMessage());
    }
}

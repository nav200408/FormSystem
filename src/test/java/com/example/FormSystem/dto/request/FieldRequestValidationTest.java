package com.example.FormSystem.dto.request;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.enums.FieldType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private <T> void assertViolation(Set<ConstraintViolation<T>> violations, String expectedMessage) {
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(expectedMessage)),
                "Expected violation message not found: " + expectedMessage);
    }

    @Test
    void createFieldRequest_ValidData_NonSelect() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Name");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(1);
        request.setIsRequired(true);
        request.setOptions(null);

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createFieldRequest_ValidData_Select() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Gender");
        request.setFieldType(FieldType.SELECT);
        request.setFieldOrder(1);
        request.setIsRequired(true);
        request.setOptions(Arrays.asList("Male", "Female"));

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createFieldRequest_BlankLabel() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(1);
        request.setIsRequired(true);

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertViolation(violations, MessageConstant.FIELD_LABEL_REQUIRED);
    }

    @Test
    void createFieldRequest_NullType() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Label");
        request.setFieldType(null);
        request.setFieldOrder(1);
        request.setIsRequired(true);

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertViolation(violations, MessageConstant.FIELD_TYPE_REQUIRED);
    }

    @Test
    void createFieldRequest_NullOrder() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Label");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(null);
        request.setIsRequired(true);

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertViolation(violations, MessageConstant.FIELD_ORDER_REQUIRED);
    }

    @Test
    void createFieldRequest_InvalidOrder() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Label");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(0);
        request.setIsRequired(true);

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertViolation(violations, MessageConstant.FIELD_ORDER_MIN);
    }

    @Test
    void createFieldRequest_NullIsRequired() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Label");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(1);
        request.setIsRequired(null);

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertViolation(violations, MessageConstant.FIELD_IS_REQUIRED_REQUIRED);
    }

    @Test
    void createFieldRequest_OptionsNotAllowed() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Label");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(1);
        request.setIsRequired(true);
        request.setOptions(Collections.singletonList("Option 1"));

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertViolation(violations, MessageConstant.FIELD_OPTIONS_NOT_ALLOWED);
    }

    @Test
    void createFieldRequest_OptionsRequired_Null() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Label");
        request.setFieldType(FieldType.SELECT);
        request.setFieldOrder(1);
        request.setIsRequired(true);
        request.setOptions(null);

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertViolation(violations, MessageConstant.FIELD_OPTIONS_REQUIRED);
    }

    @Test
    void createFieldRequest_OptionsRequired_Empty() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Label");
        request.setFieldType(FieldType.SELECT);
        request.setFieldOrder(1);
        request.setIsRequired(true);
        request.setOptions(Collections.emptyList());

        Set<ConstraintViolation<CreateFieldRequest>> violations = validator.validate(request);
        assertViolation(violations, MessageConstant.FIELD_OPTIONS_REQUIRED);
    }

    @Test
    void updateFieldRequest_ValidData() {
        UpdateFieldRequest request = new UpdateFieldRequest();
        request.setFieldLabel("Label");
        request.setFieldType(FieldType.NUMBER);
        request.setFieldOrder(2);
        request.setIsRequired(false);

        Set<ConstraintViolation<UpdateFieldRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void updateFieldRequest_InvalidData() {
        UpdateFieldRequest request = new UpdateFieldRequest();
        request.setFieldLabel("");
        request.setFieldType(FieldType.SELECT);
        request.setFieldOrder(-1);
        request.setIsRequired(null);
        request.setOptions(null);

        Set<ConstraintViolation<UpdateFieldRequest>> violations = validator.validate(request);

        assertEquals(4, violations.size());

        assertViolation(violations, MessageConstant.FIELD_LABEL_REQUIRED);
        assertViolation(violations, MessageConstant.FIELD_ORDER_MIN);
        assertViolation(violations, MessageConstant.FIELD_IS_REQUIRED_REQUIRED);
        assertViolation(violations, MessageConstant.FIELD_OPTIONS_REQUIRED);
    }
}

package com.example.FormSystem.dto.request;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.enums.FormStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // --- CreateFormRequest Tests ---

    @Test
    void createFormRequest_WithValidData() {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName("Test Form");
        request.setFormDescription("Test Description");
        request.setStatus(FormStatus.DRAFT);
        request.setOrder(1);

        Set<ConstraintViolation<CreateFormRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void createFormRequest_WithBlankName() {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName("");
        request.setFormDescription("Test Description");
        request.setOrder(1);

        Set<ConstraintViolation<CreateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_NAME_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void createFormRequest_WithNullName() {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName(null);
        request.setFormDescription("Test Description");
        request.setOrder(1);

        Set<ConstraintViolation<CreateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_NAME_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void createFormRequest_WithBlankDescription() {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName("Test Form");
        request.setFormDescription("   ");
        request.setOrder(1);

        Set<ConstraintViolation<CreateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_DESCRIPTION_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void createFormRequest_WithNullDescription() {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName("Test Form");
        request.setFormDescription(null);
        request.setOrder(1);

        Set<ConstraintViolation<CreateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_DESCRIPTION_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void createFormRequest_WithNullOrder() {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName("Test Form");
        request.setFormDescription("Test Description");
        request.setOrder(null);

        Set<ConstraintViolation<CreateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_ORDER_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void createFormRequest_WithInvalidOrder() {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName("Test Form");
        request.setFormDescription("Test Description");
        request.setOrder(0);

        Set<ConstraintViolation<CreateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_ORDER_MIN, violations.iterator().next().getMessage());
    }

    // --- UpdateFormRequest Tests ---

    @Test
    void updateFormRequest_WithValidData() {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName("Updated Form");
        request.setFormDescription("Updated Description");
        request.setStatus(FormStatus.ACTIVE);
        request.setOrder(5);

        Set<ConstraintViolation<UpdateFormRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void updateFormRequest_WithBlankName() {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName("");
        request.setFormDescription("Updated Description");
        request.setOrder(5);

        Set<ConstraintViolation<UpdateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_NAME_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void updateFormRequest_WithNullName() {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName(null);
        request.setFormDescription("Updated Description");
        request.setOrder(5);

        Set<ConstraintViolation<UpdateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_NAME_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void updateFormRequest_WithBlankDescription() {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName("Updated Form");
        request.setFormDescription("   ");
        request.setOrder(5);

        Set<ConstraintViolation<UpdateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_DESCRIPTION_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void updateFormRequest_WithNullDescription() {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName("Updated Form");
        request.setFormDescription(null);
        request.setOrder(5);

        Set<ConstraintViolation<UpdateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_DESCRIPTION_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void updateFormRequest_WithNullOrder() {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName("Updated Form");
        request.setFormDescription("Updated Description");
        request.setOrder(null);

        Set<ConstraintViolation<UpdateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_ORDER_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void updateFormRequest_WithInvalidOrder() {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName("Updated Form");
        request.setFormDescription("Updated Description");
        request.setOrder(-1);

        Set<ConstraintViolation<UpdateFormRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(MessageConstant.FORM_ORDER_MIN, violations.iterator().next().getMessage());
    }
}

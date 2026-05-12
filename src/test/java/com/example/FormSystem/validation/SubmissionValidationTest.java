package com.example.FormSystem.validation;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.enums.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SubmissionValidationTest {

    private SubmissionValidation submissionValidation;

    @BeforeEach
    public void setUp() {
        submissionValidation = new SubmissionValidation();
    }

    @Test
    public void testValidateRequired_Success() {
        Field field = new Field();
        field.setIsRequired(true);
        field.setFieldLabel("Test Label");

        assertDoesNotThrow(() -> submissionValidation.validateRequired(field, "Valid Value"));
    }

    @Test
    public void testValidateRequired_Failure() {
        Field field = new Field();
        field.setIsRequired(true);
        field.setFieldLabel("Test Label");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> submissionValidation.validateRequired(field, ""));

        assertEquals(MessageConstant.SUBMISSION_FIELD_REQUIRED + "Test Label", exception.getMessage());
    }

    @Test
    public void testValidateFieldValue_TextLength_Failure() {
        Field field = new Field();
        field.setFieldType(FieldType.TEXT);
        field.setFieldLabel("Text Field");

        String longText = "a".repeat(201);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> submissionValidation.validateFieldValue(field, longText));

        assertEquals(MessageConstant.SUBMISSION_TEXT_TOO_LONG + "Text Field", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "-1", "101", "abc" })
    public void testValidateFieldValue_Number_Failure(String invalidValue) {
        Field field = new Field();
        field.setFieldType(FieldType.NUMBER);
        field.setFieldLabel("Number Field");

        assertThrows(IllegalArgumentException.class,
                () -> submissionValidation.validateFieldValue(field, invalidValue));
    }

    @Test
    public void testValidateFieldValue_Date_Failure() {
        Field field = new Field();
        field.setFieldType(FieldType.DATE);
        field.setFieldLabel("Date Field");

        // Past date
        assertThrows(IllegalArgumentException.class,
                () -> submissionValidation.validateFieldValue(field, "2000-01-01"));

        // Invalid format
        assertThrows(IllegalArgumentException.class,
                () -> submissionValidation.validateFieldValue(field, "01-01-2025"));
    }

    @Test
    public void testValidateFieldValue_Color_Failure() {
        Field field = new Field();
        field.setFieldType(FieldType.COLOR);
        field.setFieldLabel("Color Field");

        assertThrows(IllegalArgumentException.class,
                () -> submissionValidation.validateFieldValue(field, "red"));
        assertThrows(IllegalArgumentException.class,
                () -> submissionValidation.validateFieldValue(field, "#GGGGGG"));
    }

    @Test
    public void testValidateFieldValue_Select_Failure() {
        Field field = new Field();
        field.setFieldType(FieldType.SELECT);
        field.setFieldLabel("Select Field");
        field.setOptions(Arrays.asList("Option1", "Option2"));

        assertThrows(IllegalArgumentException.class,
                () -> submissionValidation.validateFieldValue(field, "Option3"));
    }
    @Test
    void testValidateFieldValue_Text_Success() {
        Field field = new Field();
        field.setFieldType(FieldType.TEXT);
        assertDoesNotThrow(() -> submissionValidation.validateFieldValue(field, "Valid text"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "0", "50", "100" })
    void testValidateFieldValue_Number_Success(String validValue) {
        Field field = new Field();
        field.setFieldType(FieldType.NUMBER);
        assertDoesNotThrow(() -> submissionValidation.validateFieldValue(field, validValue));
    }

    @Test
    void testValidateFieldValue_Date_Success() {
        Field field = new Field();
        field.setFieldType(FieldType.DATE);
        String futureDate = java.time.LocalDate.now().plusDays(1).toString();
        assertDoesNotThrow(() -> submissionValidation.validateFieldValue(field, futureDate));
    }

    @ParameterizedTest
    @ValueSource(strings = { "#FFF", "#FFFFFF", "#000000", "#abc" })
    void testValidateFieldValue_Color_Success(String validColor) {
        Field field = new Field();
        field.setFieldType(FieldType.COLOR);
        assertDoesNotThrow(() -> submissionValidation.validateFieldValue(field, validColor));
    }

    @Test
    void testValidateFieldValue_Select_Success() {
        Field field = new Field();
        field.setFieldType(FieldType.SELECT);
        field.setOptions(Arrays.asList("Option1", "Option2"));
        assertDoesNotThrow(() -> submissionValidation.validateFieldValue(field, "Option1"));
    }

    @Test
    void testValidateFieldValue_Optional_Success() {
        Field field = new Field();
        field.setFieldType(FieldType.NUMBER);
        field.setIsRequired(false);
        assertDoesNotThrow(() -> submissionValidation.validateFieldValue(field, null));
        assertDoesNotThrow(() -> submissionValidation.validateFieldValue(field, ""));
    }
}

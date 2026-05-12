package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.dto.request.UpdateFieldRequest;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.enums.FieldType;
import com.example.FormSystem.repository.FieldRepository;
import com.example.FormSystem.service.FormService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FieldServiceImplTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private FormService formService;

    @InjectMocks
    private FieldServiceImpl fieldService;

    private Form mockForm;
    private Field mockField;

    @BeforeEach
    void setUp() {
        mockForm = new Form();
        mockForm.setFormId(1L);
        mockForm.setFormName("Test Form");

        mockField = new Field();
        mockField.setFieldId(1L);
        mockField.setFieldLabel("Name");
        mockField.setFieldOrder(1);
        mockField.setForm(mockForm);
    }

    @Test
    void addFieldToForm_Success() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("New Field");
        request.setFieldOrder(2);
        request.setFieldType(FieldType.TEXT);
        request.setIsRequired(true);

        when(formService.getFormById(1L)).thenReturn(mockForm);
        when(fieldRepository.existsByFormAndFieldOrder(mockForm, 2)).thenReturn(false);
        when(fieldRepository.save(any(Field.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Field result = fieldService.addFieldToForm(1L, request);

        assertNotNull(result);
        assertEquals("New Field", result.getFieldLabel());
        assertEquals(2, result.getFieldOrder());
        verify(fieldRepository).save(any(Field.class));
    }

    @Test
    void addFieldToForm_DuplicateOrder_ShouldThrowException() {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldOrder(1);

        when(formService.getFormById(1L)).thenReturn(mockForm);
        when(fieldRepository.existsByFormAndFieldOrder(mockForm, 1)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fieldService.addFieldToForm(1L, request));
        assertEquals(MessageConstant.FIELD_ORDER_DUPLICATED, exception.getMessage());
    }

    @Test
    void updateField_Success() {
        UpdateFieldRequest request = new UpdateFieldRequest();
        request.setFieldLabel("Updated Label");
        request.setFieldOrder(2); // Changing order
        request.setFieldType(FieldType.NUMBER);
        request.setIsRequired(false);

        when(formService.getFormById(1L)).thenReturn(mockForm);
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(mockField));
        when(fieldRepository.existsByFormAndFieldOrder(mockForm, 2)).thenReturn(false);
        when(fieldRepository.save(any(Field.class))).thenReturn(mockField);

        Field result = fieldService.updateField(1L, 1L, request);

        assertNotNull(result);
        assertEquals("Updated Label", result.getFieldLabel());
        assertEquals(2, result.getFieldOrder());
        verify(fieldRepository).save(mockField);
    }

    @Test
    void updateField_SameOrder_Success() {
        UpdateFieldRequest request = new UpdateFieldRequest();
        request.setFieldLabel("Updated Label");
        request.setFieldOrder(1);
        request.setFieldType(FieldType.NUMBER);

        when(formService.getFormById(1L)).thenReturn(mockForm);
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(mockField));
        when(fieldRepository.save(any(Field.class))).thenReturn(mockField);

        fieldService.updateField(1L, 1L, request);

        verify(fieldRepository, never()).existsByFormAndFieldOrder(any(), any());
        verify(fieldRepository).save(mockField);
    }

    @Test
    void updateField_FieldNotBelongToForm_ShouldThrowException() {
        UpdateFieldRequest request = new UpdateFieldRequest();

        Form anotherForm = new Form();
        anotherForm.setFormId(2L);
        mockField.setForm(anotherForm);

        when(formService.getFormById(1L)).thenReturn(mockForm);
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(mockField));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fieldService.updateField(1L, 1L, request));
        assertEquals(MessageConstant.FORM_NOT_FOUND, exception.getMessage());
    }

    @Test
    void updateField_NotFound_ShouldThrowException() {
        UpdateFieldRequest request = new UpdateFieldRequest();
        when(formService.getFormById(1L)).thenReturn(mockForm);
        when(fieldRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> fieldService.updateField(1L, 99L, request));
    }

    @Test
    void deleteField_Success() {
        when(formService.getFormById(1L)).thenReturn(mockForm);
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(mockField));

        fieldService.deleteField(1L, 1L);

        verify(fieldRepository).delete(mockField);
    }

    @Test
    void deleteField_NotBelongToForm_ShouldThrowException() {
        Form anotherForm = new Form();
        anotherForm.setFormId(2L);
        mockField.setForm(anotherForm);

        when(formService.getFormById(1L)).thenReturn(mockForm);
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(mockField));

        assertThrows(IllegalArgumentException.class, () -> fieldService.deleteField(1L, 1L));
        verify(fieldRepository, never()).delete(any());
    }
}

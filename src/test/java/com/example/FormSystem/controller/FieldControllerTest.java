package com.example.FormSystem.controller;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.dto.request.UpdateFieldRequest;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.enums.FieldType;
import com.example.FormSystem.exception.GlobalExceptionHandler;
import com.example.FormSystem.service.FieldService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FieldControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FieldService fieldService;

    @InjectMocks
    private FieldController fieldController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fieldController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addFieldToForm_Success() throws Exception {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Name");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(1);
        request.setIsRequired(true);

        Field field = new Field();
        field.setFieldId(1L);
        field.setFieldLabel("Name");

        when(fieldService.addFieldToForm(eq(1L), any(CreateFieldRequest.class))).thenReturn(field);

        mockMvc.perform(post("/api/forms/1/fields")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(MessageConstant.CREATE_SUCCESS))
                .andExpect(jsonPath("$.data.fieldId").value(1))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void addFieldToForm_FormNotFound() throws Exception {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Name");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(1);
        request.setIsRequired(true);

        when(fieldService.addFieldToForm(eq(99L), any(CreateFieldRequest.class)))
                .thenThrow(new EntityNotFoundException(MessageConstant.FORM_NOT_FOUND + 99));

        mockMvc.perform(post("/api/forms/99/fields")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.message").value(MessageConstant.FORM_NOT_FOUND + 99));
    }

    @Test
    void addFieldToForm_ValidationError() throws Exception {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel(""); // Invalid

        mockMvc.perform(post("/api/forms/1/fields")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.message").value(MessageConstant.VALIDATION_FAILED))
                .andExpect(jsonPath("$.errors.validation.fieldLabel").value(MessageConstant.FIELD_LABEL_REQUIRED));
    }

    @Test
    void addFieldToForm_DuplicateOrder() throws Exception {
        CreateFieldRequest request = new CreateFieldRequest();
        request.setFieldLabel("Name");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(1);
        request.setIsRequired(true);

        when(fieldService.addFieldToForm(eq(1L), any(CreateFieldRequest.class)))
                .thenThrow(new IllegalArgumentException(MessageConstant.FIELD_ORDER_DUPLICATED));

        mockMvc.perform(post("/api/forms/1/fields")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.message").value(MessageConstant.FIELD_ORDER_DUPLICATED));
    }

    @Test
    void updateField_Success() throws Exception {
        UpdateFieldRequest request = new UpdateFieldRequest();
        request.setFieldLabel("Updated Name");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(2);
        request.setIsRequired(false);

        Field field = new Field();
        field.setFieldId(1L);
        field.setFieldLabel("Updated Name");

        when(fieldService.updateField(eq(1L), eq(1L), any(UpdateFieldRequest.class))).thenReturn(field);

        mockMvc.perform(put("/api/forms/1/fields/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(MessageConstant.UPDATE_SUCCESS))
                .andExpect(jsonPath("$.data.fieldLabel").value("Updated Name"));
    }

    @Test
    void updateField_FieldNotFound() throws Exception {
        UpdateFieldRequest request = new UpdateFieldRequest();
        request.setFieldLabel("Updated Name");
        request.setFieldType(FieldType.TEXT);
        request.setFieldOrder(2);
        request.setIsRequired(false);

        when(fieldService.updateField(eq(1L), eq(99L), any(UpdateFieldRequest.class)))
                .thenThrow(new EntityNotFoundException(MessageConstant.FIELD_NOT_FOUND + 99));

        mockMvc.perform(put("/api/forms/1/fields/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.message").value(MessageConstant.FIELD_NOT_FOUND + 99));
    }

    @Test
    void deleteField_Success() throws Exception {
        doNothing().when(fieldService).deleteField(1L, 1L);

        mockMvc.perform(delete("/api/forms/1/fields/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(MessageConstant.DELETE_SUCCESS));
        
        verify(fieldService).deleteField(1L, 1L);
    }

    @Test
    void deleteField_NotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstant.FIELD_NOT_FOUND + 1))
                .when(fieldService).deleteField(1L, 1L);

        mockMvc.perform(delete("/api/forms/1/fields/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.message").value(MessageConstant.FIELD_NOT_FOUND + 1));
    }
}

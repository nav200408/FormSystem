package com.example.FormSystem.controller;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.dto.response.ResponseData;
import com.example.FormSystem.enums.FormStatus;
import com.example.FormSystem.service.FormService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormControllerTest {

    @Mock
    private FormService formService;

    @InjectMocks
    private FormController formController;

    private PageResponse<FormDtoResponse> mockPageResponse;

    @BeforeEach
    void setUp() {
        List<FormDtoResponse> forms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FormDtoResponse form = new FormDtoResponse();
            form.setFormId((long) i);
            form.setFormName("Form " + i);
            form.setStatus(FormStatus.DRAFT);
            form.setOrder(i + 1);
            forms.add(form);
        }
        mockPageResponse = new PageResponse<>(forms, 1, 5, true);
    }

    @Test
    void getAllForms_WithValidParameters() {

        when(formService.getForms(2, 5)).thenReturn(mockPageResponse);

        ResponseEntity<ResponseData<PageResponse<FormDtoResponse>>> responseEntity = formController.getAllForms(2, 5);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseData<PageResponse<FormDtoResponse>> body = responseEntity.getBody();
        assertTrue(body.isSuccess());
        assertEquals(200, body.getStatusCode());
        assertEquals(MessageConstant.RETRIEVE_DATA_SUCCESS, body.getMessage());
        assertEquals(1, body.getData().getPage());
        assertEquals(5, body.getData().getSize());
        assertTrue(body.getData().isHasNext());

        verify(formService, times(1)).getForms(2, 5);
    }

    @Test
    void getAllForms_WithNegativeParameters() {

        when(formService.getForms(1, 1)).thenReturn(mockPageResponse);

        ResponseEntity<ResponseData<PageResponse<FormDtoResponse>>> responseEntity = formController.getAllForms(-5, 0);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseData<PageResponse<FormDtoResponse>> body = responseEntity.getBody();
        assertTrue(body.isSuccess());

        verify(formService, times(1)).getForms(1, 1);
    }
}

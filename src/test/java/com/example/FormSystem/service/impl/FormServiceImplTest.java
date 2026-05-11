package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFormRequest;
import com.example.FormSystem.dto.request.UpdateFormRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.enums.FormStatus;
import com.example.FormSystem.repository.FormRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormServiceImplTest {

    @Mock
    private FormRepository formRepository;

    @InjectMocks
    private FormServiceImpl formService;

    private List<Form> mockForms;
    private Form mockForm;

    @BeforeEach
    void setUp() {
        mockForms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Form form = new Form();
            form.setFormId((long) i);
            form.setFormName("Form " + i);
            form.setFormDescription("Description " + i);
            form.setStatus(FormStatus.DRAFT);
            form.setOrder(i + 1);
            mockForms.add(form);
        }

        mockForm = new Form();
        mockForm.setFormId(1L);
        mockForm.setFormName("Test Form");
        mockForm.setFormDescription("Test Description");
        mockForm.setStatus(FormStatus.DRAFT);
        mockForm.setOrder(1);
    }

    @Test
    void getForms_HasNextIsTrue() {
        int page = 1;
        int size = 4;
        when(formRepository.findAllWithDeferredPagination(size + 1, 0)).thenReturn(mockForms);

        PageResponse<FormDtoResponse> response = formService.getForms(page, size);

        assertTrue(response.isHasNext());
        assertEquals(4, response.getContent().size());
        verify(formRepository).findAllWithDeferredPagination(size + 1, 0);
    }

    @Test
    void getForms_HasNextIsFalse() {
        int page = 1;
        int size = 5;
        when(formRepository.findAllWithDeferredPagination(size + 1, 0)).thenReturn(mockForms);

        PageResponse<FormDtoResponse> response = formService.getForms(page, size);

        assertFalse(response.isHasNext());
        assertEquals(5, response.getContent().size());
    }

    @Test
    void createForm_Success() {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName("New Form");
        request.setFormDescription("Desc");
        request.setOrder(1);

        when(formRepository.save(any(Form.class))).thenReturn(mockForm);

        FormDtoResponse result = formService.createForm(request);

        assertNotNull(result);
        assertEquals("Test Form", result.getFormName());
        verify(formRepository).save(any(Form.class));
    }

    @Test
    void getFormById_Success() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(mockForm));

        Form result = formService.getFormById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getFormId());
    }

    @Test
    void getFormById_NotFound() {
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> formService.getFormById(99L));
        assertTrue(exception.getMessage().contains(MessageConstant.FORM_NOT_FOUND));
    }

    @Test
    void updateForm_Success() {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName("Updated Name");
        request.setFormDescription("Updated Desc");
        request.setStatus(FormStatus.ACTIVE);
        request.setOrder(2);

        when(formRepository.findById(1L)).thenReturn(Optional.of(mockForm));
        when(formRepository.save(any(Form.class))).thenReturn(mockForm);

        FormDtoResponse result = formService.updateForm(1L, request);

        assertNotNull(result);
        verify(formRepository).save(mockForm);
    }

    @Test
    void updateForm_NotFound() {
        UpdateFormRequest request = new UpdateFormRequest();
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> formService.updateForm(99L, request));
    }

    @Test
    void deleteForm_Success() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(mockForm));

        formService.deleteForm(1L);

        verify(formRepository).delete(mockForm);
    }

    @Test
    void deleteForm_NotFound() {
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> formService.deleteForm(99L));
        verify(formRepository, never()).delete(any());
    }
}

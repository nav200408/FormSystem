package com.example.FormSystem.service.impl;

import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.enums.FormStatus;
import com.example.FormSystem.repository.FormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormServiceImplTest {

    @Mock
    private FormRepository formRepository;

    @InjectMocks
    private FormServiceImpl formService;

    private List<Form> mockForms;

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
    }

    @Test
    void getForms_HasNextIsTrue() {

        int page = 1;
        int size = 4;
        when(formRepository.findAllWithDeferredPagination(size + 1, 0)).thenReturn(mockForms);

        PageResponse<FormDtoResponse> response = formService.getForms(page, size);

        assertTrue(response.isHasNext());
        assertEquals(4, response.getContent().size());
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals("Form 0", response.getContent().get(0).getFormName());
        verify(formRepository, times(1)).findAllWithDeferredPagination(size + 1, 0);
    }

    @Test
    void getForms_HasNextIsFalse() {

        int page = 1;
        int size = 5;
        when(formRepository.findAllWithDeferredPagination(size + 1, 0)).thenReturn(mockForms);

        PageResponse<FormDtoResponse> response = formService.getForms(page, size);

        assertFalse(response.isHasNext());
        assertEquals(5, response.getContent().size());
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        verify(formRepository, times(1)).findAllWithDeferredPagination(size + 1, 0);
    }

    @Test
    void getForms_EmptyList() {

        int page = 2;
        int size = 10;
        int offset = (page - 1) * size;
        when(formRepository.findAllWithDeferredPagination(size + 1, offset)).thenReturn(new ArrayList<>());

        PageResponse<FormDtoResponse> response = formService.getForms(page, size);

        assertFalse(response.isHasNext());
        assertTrue(response.getContent().isEmpty());
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        verify(formRepository, times(1)).findAllWithDeferredPagination(size + 1, offset);
    }

    @Test
    void getForms_CorrectOffsetCalculation() {

        int page = 3;
        int size = 10;
        int expectedOffset = 20;
        when(formRepository.findAllWithDeferredPagination(size + 1, expectedOffset)).thenReturn(new ArrayList<>());

        formService.getForms(page, size);

        verify(formRepository, times(1)).findAllWithDeferredPagination(size + 1, expectedOffset);
    }
}

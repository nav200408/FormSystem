package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.FieldValueRequest;
import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.*;
import com.example.FormSystem.repository.FormRepository;
import com.example.FormSystem.repository.SubmissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubmissionServiceImplTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private FormRepository formRepository;

    @InjectMocks
    private SubmissionServiceImpl submissionService;

    private User mockUser;
    private Form mockForm;
    private Field mockField;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        mockForm = new Form();
        mockForm.setFormId(1L);
        mockForm.setFormName("Test Form");

        mockField = new Field();
        mockField.setFieldId(1L);
        mockField.setFieldLabel("Name");
        mockField.setForm(mockForm);
        mockForm.setFields(Collections.singletonList(mockField));

        // Mock SecurityContext
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void submitForm_Success() {
        SubmissionRequest request = new SubmissionRequest();
        FieldValueRequest valRequest = new FieldValueRequest();
        valRequest.setFieldId(1L);
        valRequest.setValue("John Doe");
        request.setValues(Collections.singletonList(valRequest));

        when(formRepository.findById(1L)).thenReturn(Optional.of(mockForm));
        when(submissionRepository.save(any(Submission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Submission result = submissionService.submitForm(1L, request);

        assertNotNull(result);
        assertEquals(mockForm, result.getForm());
        assertEquals(mockUser, result.getUser());
        assertEquals(1, result.getValues().size());
        assertEquals("John Doe", result.getValues().get(0).getValue());
        verify(submissionRepository).save(any(Submission.class));
    }

    @Test
    void submitForm_FormNotFound_ShouldThrowException() {
        SubmissionRequest request = new SubmissionRequest();
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> submissionService.submitForm(99L, request));
    }

    @Test
    void getSubmissionsForCurrentUser_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Form> formPage = new PageImpl<>(Collections.singletonList(mockForm), pageable, 1);

        when(formRepository.findFormsSubmittedByUserId(eq(1L), any(Pageable.class))).thenReturn(formPage);

        PageResponse<FormDtoResponse> response = submissionService.getSubmissionsForCurrentUser(1, 10);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(1, response.getPage());
        assertEquals("Test Form", response.getContent().get(0).getFormName());
        assertFalse(response.isHasNext());
    }

    @Test
    void getSubmissionsForCurrentUser_NoSubmissions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Form> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(formRepository.findFormsSubmittedByUserId(eq(1L), any(Pageable.class))).thenReturn(emptyPage);

        PageResponse<FormDtoResponse> response = submissionService.getSubmissionsForCurrentUser(1, 10);

        assertNotNull(response);
        assertTrue(response.getContent().isEmpty());
        assertFalse(response.isHasNext());
    }
}

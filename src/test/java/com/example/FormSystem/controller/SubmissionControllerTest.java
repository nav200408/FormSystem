package com.example.FormSystem.controller;

import com.example.FormSystem.dto.request.FieldValueRequest;
import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.Submission;
import com.example.FormSystem.entity.User;
import com.example.FormSystem.enums.Role;
import com.example.FormSystem.service.SubmissionService;
import com.example.FormSystem.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SubmissionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubmissionService submissionService;

    @InjectMocks
    private SubmissionController submissionController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(submissionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGetSubmissionsForCurrentUser_Success() throws Exception {
        FormDtoResponse formDto = new FormDtoResponse();
        formDto.setFormId(1L);
        formDto.setFormName("Test Form");

        PageResponse<FormDtoResponse> pageResponse = new PageResponse<>(
                Collections.singletonList(formDto), 1, 10, false
        );

        when(submissionService.getSubmissionsForCurrentUser(1, 10))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/submissions")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.content[0].formId").value(1))
                .andExpect(jsonPath("$.data.content[0].formName").value("Test Form"));
    }

    @Test
    public void testSubmitForm_Success() throws Exception {
        Long formId = 1L;
        SubmissionRequest request = new SubmissionRequest();
        FieldValueRequest val = new FieldValueRequest();
        val.setFieldId(1L);
        val.setValue("Test Value");
        request.setValues(Collections.singletonList(val));

        Submission submission = new Submission();
        submission.setSubmissionId(100L);

        when(submissionService.submitForm(eq(formId), any(SubmissionRequest.class)))
                .thenReturn(submission);

        mockMvc.perform(post("/forms/{id}/submit", formId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.data.submissionId").value(100));
    }

    @Test
    public void testSubmitForm_FormNotFound() throws Exception {
        Long formId = 1L;
        SubmissionRequest request = new SubmissionRequest();
        FieldValueRequest val = new FieldValueRequest();
        val.setFieldId(1L);
        val.setValue("Test Value");
        request.setValues(Collections.singletonList(val));

        when(submissionService.submitForm(eq(formId), any(SubmissionRequest.class)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Form not found"));

        mockMvc.perform(post("/forms/{id}/submit", formId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.message").value("Form not found"));
    }

    @Test
    public void testSubmitForm_IllegalArgument() throws Exception {
        Long formId = 1L;
        SubmissionRequest request = new SubmissionRequest();
        FieldValueRequest val = new FieldValueRequest();
        val.setFieldId(1L);
        val.setValue("Test Value");
        request.setValues(Collections.singletonList(val));

        when(submissionService.submitForm(eq(formId), any(SubmissionRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid input"));

        mockMvc.perform(post("/forms/{id}/submit", formId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.message").value("Invalid input"));
    }
    
}
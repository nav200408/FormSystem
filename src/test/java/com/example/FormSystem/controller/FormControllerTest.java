package com.example.FormSystem.controller;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFormRequest;
import com.example.FormSystem.dto.request.UpdateFormRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.entity.User;
import com.example.FormSystem.enums.FormStatus;
import com.example.FormSystem.enums.Role;
import com.example.FormSystem.exception.GlobalExceptionHandler;
import com.example.FormSystem.service.FormService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FormControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FormService formService;

    @InjectMocks
    private FormController formController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(formController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllForms_Success() throws Exception {
        List<FormDtoResponse> forms = new ArrayList<>();
        FormDtoResponse f = new FormDtoResponse();
        f.setFormId(1L);
        f.setFormName("Test Form");
        forms.add(f);
        PageResponse<FormDtoResponse> pageResponse = new PageResponse<>(forms, 1, 10, false);

        when(formService.getForms(1, 10)).thenReturn(pageResponse);

        mockMvc.perform(get("/forms")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is(MessageConstant.RETRIEVE_DATA_SUCCESS)))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].formName", is("Test Form")));
    }

    @Test
    void createForm_Success() throws Exception {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName("New Form");
        request.setFormDescription("Desc");
        request.setOrder(1);

        FormDtoResponse response = new FormDtoResponse();
        response.setFormId(1L);
        response.setFormName("New Form");

        when(formService.createForm(any(CreateFormRequest.class))).thenReturn(response);

        mockMvc.perform(post("/forms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is(MessageConstant.CREATE_SUCCESS)));
    }

    @Test
    void createForm_InvalidRequest() throws Exception {
        CreateFormRequest request = new CreateFormRequest();
        request.setFormName(""); // Invalid: NotBlank

        mockMvc.perform(post("/forms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.message", is(MessageConstant.VALIDATION_FAILED)))
                .andExpect(jsonPath("$.errors.validation.formName", notNullValue()));
    }


    @Test
    void getFormById_Success() throws Exception {
        Form form = new Form();
        form.setFormId(1L);
        form.setFormName("Detail Form");

        when(formService.getFormById(1L)).thenReturn(form);

        mockMvc.perform(get("/forms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.formName", is("Detail Form")));
    }

    @Test
    void getFormById_NotFound() throws Exception {
        when(formService.getFormById(99L)).thenThrow(new EntityNotFoundException(MessageConstant.FORM_NOT_FOUND + 99));

        mockMvc.perform(get("/forms/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.message", containsString(MessageConstant.FORM_NOT_FOUND)));
    }


    @Test
    void updateForm_Success() throws Exception {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName("Updated");
        request.setFormDescription("Updated Desc");
        request.setStatus(FormStatus.ACTIVE);
        request.setOrder(1);

        FormDtoResponse response = new FormDtoResponse();
        response.setFormId(1L);
        response.setFormName("Updated");

        when(formService.updateForm(eq(1L), any(UpdateFormRequest.class))).thenReturn(response);

        mockMvc.perform(put("/forms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(MessageConstant.UPDATE_SUCCESS)));
    }

    @Test
    void updateForm_NotFound() throws Exception {
        UpdateFormRequest request = new UpdateFormRequest();
        request.setFormName("Updated");
        request.setFormDescription("Desc");
        request.setOrder(1);

        when(formService.updateForm(eq(99L), any(UpdateFormRequest.class)))
                .thenThrow(new EntityNotFoundException(MessageConstant.FORM_NOT_FOUND + 99));

        mockMvc.perform(put("/forms/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteForm_Success() throws Exception {
        doNothing().when(formService).deleteForm(1L);

        mockMvc.perform(delete("/forms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(MessageConstant.DELETE_SUCCESS)));
    }

    @Test
    void deleteForm_NotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstant.FORM_NOT_FOUND + 99))
                .when(formService).deleteForm(99L);

        mockMvc.perform(delete("/forms/99"))
                .andExpect(status().isNotFound());
    }
}

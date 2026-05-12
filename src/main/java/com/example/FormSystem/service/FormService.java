package com.example.FormSystem.service;

import com.example.FormSystem.dto.request.CreateFormRequest;
import com.example.FormSystem.dto.request.UpdateFormRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.Form;

public interface FormService {
    PageResponse<FormDtoResponse> getForms(int page, int size);

    FormDtoResponse createForm(CreateFormRequest request);

    Form getFormById(Long id);

    FormDtoResponse updateForm(Long id, UpdateFormRequest request);
    
    java.util.List<FormDtoResponse> getActiveForms();

    void deleteForm(Long id);
}

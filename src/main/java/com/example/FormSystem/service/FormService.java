package com.example.FormSystem.service;

import com.example.FormSystem.dto.request.CreateFormRequest;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.Form;

public interface FormService {
    PageResponse<Form> getForms(int page, int size);

    Form createForm(CreateFormRequest request);
}

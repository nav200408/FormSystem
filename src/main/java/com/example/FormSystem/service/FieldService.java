package com.example.FormSystem.service;

import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.entity.Field;

public interface FieldService {
    Field addFieldToForm(Long formId, CreateFieldRequest request);
}

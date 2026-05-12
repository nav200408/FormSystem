package com.example.FormSystem.service;

import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.dto.request.UpdateFieldRequest;
import com.example.FormSystem.entity.Field;

public interface FieldService {
    Field addFieldToForm(Long formId, CreateFieldRequest request);

    Field updateField(Long formId, Long fieldId, UpdateFieldRequest request);
    
    void deleteField(Long formId, Long fieldId);
}

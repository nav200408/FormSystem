package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.repository.FieldRepository;
import com.example.FormSystem.service.FieldService;
import com.example.FormSystem.service.FormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;
    private final FormService formService;

    public FieldServiceImpl(FieldRepository fieldRepository, FormService formService) {
        this.fieldRepository = fieldRepository;
        this.formService = formService;
    }

    @Override
    @Transactional
    public Field addFieldToForm(Long formId, CreateFieldRequest request) {
        // Check if form exists
        Form form = formService.getFormById(formId);

        // Check if order is duplicated in this form
        if (fieldRepository.existsByFormAndFieldOrder(form, request.getFieldOrder())) {
            throw new IllegalArgumentException(MessageConstant.FIELD_ORDER_DUPLICATED);
        }

        Field field = new Field();
        field.setFieldLabel(request.getFieldLabel());
        field.setFieldType(request.getFieldType());
        field.setFieldOrder(request.getFieldOrder());
        field.setIsRequired(request.getIsRequired());
        field.setOptions(request.getOptions());
        field.setForm(form);

        return fieldRepository.save(field);
    }
}

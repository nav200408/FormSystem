package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.dto.request.UpdateFieldRequest;
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

        Form form = formService.getFormById(formId);

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

    @Override
    @Transactional
    public Field updateField(Long formId, Long fieldId, UpdateFieldRequest request) {

        Form form = formService.getFormById(formId);

        Field field = getFieldById(fieldId);

        if (!field.getForm().getFormId().equals(formId)) {
            throw new IllegalArgumentException(MessageConstant.FORM_NOT_FOUND);
        }

        if (!field.getFieldOrder().equals(request.getFieldOrder())) {
            if (fieldRepository.existsByFormAndFieldOrder(form, request.getFieldOrder())) {
                throw new IllegalArgumentException(MessageConstant.FIELD_ORDER_DUPLICATED);
            }
        }

        field.setFieldLabel(request.getFieldLabel());
        field.setFieldType(request.getFieldType());
        field.setFieldOrder(request.getFieldOrder());
        field.setIsRequired(request.getIsRequired());
        field.setOptions(request.getOptions());

        return fieldRepository.save(field);
    }

    private Field getFieldById(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(
                        () -> new jakarta.persistence.EntityNotFoundException(MessageConstant.FIELD_NOT_FOUND + id));
    }
}

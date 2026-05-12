package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.dto.request.ReorderFieldRequest;
import com.example.FormSystem.dto.request.UpdateFieldRequest;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.mapper.FieldMapper;
import com.example.FormSystem.repository.FieldRepository;
import com.example.FormSystem.service.FieldService;
import com.example.FormSystem.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FormService formService;

    public FieldServiceImpl(FieldRepository fieldRepository, FormService formService) {
        this.fieldRepository = fieldRepository;
        this.formService = formService;
    }

    @Override
    @Transactional
    public void reorderFields(Long formId, ReorderFieldRequest request) {
        Form form = formService.getFormById(formId);
        List<Field> fields = form.getFields();
        Map<Long, Field> fieldMap = fields.stream()
                .collect(Collectors.toMap(Field::getFieldId, f -> f));

        List<Long> requestIds = request.getFieldIds();

        for (int i = 0; i < requestIds.size(); i++) {
            Field field = fieldMap.get(requestIds.get(i));
            field.setFieldOrder(i + 1000);
            fieldRepository.save(field);
        }

        fieldRepository.flush();

        for (int i = 0; i < requestIds.size(); i++) {
            Field field = fieldMap.get(requestIds.get(i));
            field.setFieldOrder(i + 1);
            fieldRepository.save(field);
        }
    }

    @Override
    @Transactional
    public Field addFieldToForm(Long formId, CreateFieldRequest request) {
        Form form = formService.getFormById(formId);

        if (fieldRepository.existsByFormAndFieldOrder(form, request.getFieldOrder())) {
            throw new IllegalArgumentException(MessageConstant.FIELD_ORDER_DUPLICATED);
        }

        Field field = FieldMapper.toEntityFromCreateRequest(request, form);
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

        FieldMapper.toEntityFromUpdateRequest(field, request);
        return fieldRepository.save(field);
    }

    @Override
    @Transactional
    public void deleteField(Long formId, Long fieldId) {
        formService.getFormById(formId);
        Field field = getFieldById(fieldId);

        if (!field.getForm().getFormId().equals(formId)) {
            throw new IllegalArgumentException(MessageConstant.FORM_NOT_FOUND);
        }

        fieldRepository.delete(field);
    }

    private Field getFieldById(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(
                        () -> new jakarta.persistence.EntityNotFoundException(MessageConstant.FIELD_NOT_FOUND + id));
    }
}

package com.example.FormSystem.mapper;

import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.dto.request.UpdateFieldRequest;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.entity.Form;

public class FieldMapper {

    public static Field toEntityFromCreateRequest(CreateFieldRequest request, Form form) {
        Field field = new Field();
        field.setFieldLabel(request.getFieldLabel());
        field.setFieldType(request.getFieldType());
        field.setFieldOrder(request.getFieldOrder());
        field.setIsRequired(request.getIsRequired());
        field.setOptions(request.getOptions());
        field.setForm(form);
        return field;
    }

    public static void toEntityFromUpdateRequest(Field field, UpdateFieldRequest request) {
        field.setFieldLabel(request.getFieldLabel());
        field.setFieldType(request.getFieldType());
        field.setFieldOrder(request.getFieldOrder());
        field.setIsRequired(request.getIsRequired());
        field.setOptions(request.getOptions());
    }
}

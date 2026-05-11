package com.example.FormSystem.mapper;

import com.example.FormSystem.dto.request.CreateFormRequest;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.enums.FormStatus;

public class FormMapper {

    public static Form toEntity(CreateFormRequest request) {
        Form form = new Form();
        form.setFormName(request.getFormName());
        form.setFormDescription(request.getFormDescription());
        form.setStatus(request.getStatus() != null ? request.getStatus() : FormStatus.DRAFT);
        form.setOrder(request.getOrder() != null ? request.getOrder() : 1);
        return form;
    }
}

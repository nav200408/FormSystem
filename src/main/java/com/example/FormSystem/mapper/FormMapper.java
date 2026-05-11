package com.example.FormSystem.mapper;

import com.example.FormSystem.dto.request.CreateFormRequest;
import com.example.FormSystem.dto.request.UpdateFormRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.enums.FormStatus;

public class FormMapper {

    public static Form toEntityFromCreateFormRequest(CreateFormRequest request) {
        Form form = new Form();
        form.setFormName(request.getFormName());
        form.setFormDescription(request.getFormDescription());
        form.setStatus(request.getStatus() != null ? request.getStatus() : FormStatus.DRAFT);
        form.setOrder(request.getOrder() != null ? request.getOrder() : 1);
        return form;
    }

    public static void toEntityFromUpdateFormRequest(Form form,UpdateFormRequest request) {
        form.setFormName(request.getFormName());
        form.setFormDescription(request.getFormDescription());
        form.setStatus(request.getStatus() != null ? request.getStatus() : FormStatus.DRAFT);
        form.setOrder(request.getOrder());
    }

    public static FormDtoResponse toResponse(Form form) {
        FormDtoResponse response = new FormDtoResponse();
        response.setFormId(form.getFormId());
        response.setFormName(form.getFormName());
        response.setFormDescription(form.getFormDescription());
        response.setStatus(form.getStatus());
        response.setOrder(form.getOrder());
        return response;
    }
}

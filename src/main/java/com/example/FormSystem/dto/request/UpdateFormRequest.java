package com.example.FormSystem.dto.request;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.enums.FormStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateFormRequest {

    @NotBlank(message = MessageConstant.FORM_NAME_REQUIRED)
    private String formName;

    @NotBlank(message = MessageConstant.FORM_DESCRIPTION_REQUIRED)
    private String formDescription;

    private FormStatus status;

    @NotNull(message = MessageConstant.FORM_ORDER_REQUIRED)
    @Min(value = 1, message = MessageConstant.FORM_ORDER_MIN)
    private Integer order;

    public UpdateFormRequest() {
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormDescription() {
        return formDescription;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }

    public FormStatus getStatus() {
        return status;
    }

    public void setStatus(FormStatus status) {
        this.status = status;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}

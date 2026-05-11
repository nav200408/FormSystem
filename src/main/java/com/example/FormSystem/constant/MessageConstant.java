package com.example.FormSystem.constant;

public class MessageConstant {
    // api return messages
    public static final String VALIDATION_FAILED = "Your request is not validated";
    public static final String RETRIEVE_DATA_SUCCESS = "Data retrieved successfully";
    public static final String CREATE_SUCCESS = "Data created successfully";
    public static final String UPDATE_SUCCESS = "Data updated successfully";
    public static final String DELETE_SUCCESS = "Data deleted successfully";

    // form validation messages
    public static final String FORM_NAME_REQUIRED = "Form name is required";
    public static final String FORM_DESCRIPTION_REQUIRED = "Form description is required";
    public static final String FORM_ORDER_REQUIRED = "Form order is required";
    public static final String FORM_ORDER_MIN = "Form order must be at least 1";
    public static final String FORM_NOT_FOUND = "Form not found with id: ";

    // field validation messages
    public static final String FIELD_LABEL_REQUIRED = "Field label is required";
    public static final String FIELD_TYPE_REQUIRED = "Field type is required";
    public static final String FIELD_ORDER_REQUIRED = "Field order is required";
    public static final String FIELD_ORDER_MIN = "Field order must be at least 1";
    public static final String FIELD_IS_REQUIRED_REQUIRED = "Is required field is required";
    public static final String FIELD_OPTIONS_NOT_ALLOWED = "Options are only allowed for SELECT field type";
    public static final String FIELD_OPTIONS_REQUIRED = "Options are required for SELECT field type";
    public static final String FIELD_ORDER_DUPLICATED = "Field order is already taken in this form";
    public static final String FIELD_NOT_FOUND = "Field not found with id: ";
}

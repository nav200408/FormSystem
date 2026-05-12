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

    // submission validation messages
    public static final String USER_NOT_FOUND = "User not found with id: ";
    public static final String SUBMISSION_FIELD_REQUIRED = "Field is required: ";
    public static final String SUBMISSION_TEXT_TOO_LONG = "Text value exceeds 200 characters for field: ";
    public static final String SUBMISSION_NUMBER_INVALID_RANGE = "Number must be between 0 and 100 for field: ";
    public static final String SUBMISSION_NUMBER_FORMAT = "Invalid number format for field: ";
    public static final String SUBMISSION_DATE_PAST = "Date cannot be in the past for field: ";
    public static final String SUBMISSION_DATE_FORMAT = "Invalid date format (Use YYYY-MM-DD) for field: ";
    public static final String SUBMISSION_COLOR_FORMAT = "Invalid color HEX code for field: ";
    public static final String SUBMISSION_SELECT_INVALID = "Invalid option selected for field: ";

    public static final String SUBMISSION_USER_ID_REQUIRED = "User ID is required";
    public static final String SUBMISSION_FIELD_ID_REQUIRED = "Field ID is required";
    public static final String SUBMISSION_VALUES_EMPTY = "Submission must contain at least one value";

    // auth messages
    public static final String REGISTER_SUCCESS = "User registered successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String USERNAME_ALREADY_TAKEN = "Username is already taken";
}

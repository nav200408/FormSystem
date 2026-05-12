package com.example.FormSystem.validation;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.enums.FieldType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Component
public class SubmissionValidation {

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

    public void validateRequired(Field field, String value) {
        if (field.getIsRequired() && (value == null || value.trim().isEmpty())) {
            throw new IllegalArgumentException(MessageConstant.SUBMISSION_FIELD_REQUIRED + field.getFieldLabel());
        }
    }

    public void validateFieldValue(Field field, String value) {
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        FieldType type = field.getFieldType();

        switch (type) {
            case TEXT:
                if (value.length() > 200) {
                    throw new IllegalArgumentException(MessageConstant.SUBMISSION_TEXT_TOO_LONG + field.getFieldLabel());
                }
                break;

            case NUMBER:
                try {
                    int num = Integer.parseInt(value);
                    if (num < 0 || num > 100) {
                        throw new IllegalArgumentException(MessageConstant.SUBMISSION_NUMBER_INVALID_RANGE + field.getFieldLabel());
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(MessageConstant.SUBMISSION_NUMBER_FORMAT + field.getFieldLabel());
                }
                break;

            case DATE:
                try {
                    LocalDate date = LocalDate.parse(value);
                    if (date.isBefore(LocalDate.now())) {
                        throw new IllegalArgumentException(MessageConstant.SUBMISSION_DATE_PAST + field.getFieldLabel());
                    }
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException(MessageConstant.SUBMISSION_DATE_FORMAT + field.getFieldLabel());
                }
                break;

            case COLOR:
                if (!HEX_COLOR_PATTERN.matcher(value).matches()) {
                    throw new IllegalArgumentException(MessageConstant.SUBMISSION_COLOR_FORMAT + field.getFieldLabel());
                }
                break;

            case SELECT:
                if (field.getOptions() == null || !field.getOptions().contains(value)) {
                    throw new IllegalArgumentException(MessageConstant.SUBMISSION_SELECT_INVALID + field.getFieldLabel());
                }
                break;
        }
    }
}

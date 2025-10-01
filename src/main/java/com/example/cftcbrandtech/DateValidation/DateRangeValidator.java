package com.example.cftcbrandtech.Validation;

import com.example.cftcbrandtech.DateValidation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(ValidDateRange annotation) {
        this.startField = annotation.startField();
        this.endField = annotation.endField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field startFieldObj = value.getClass().getDeclaredField(startField);
            Field endFieldObj = value.getClass().getDeclaredField(endField);

            startFieldObj.setAccessible(true);
            endFieldObj.setAccessible(true);

            LocalDateTime startTime = (LocalDateTime) startFieldObj.get(value);
            LocalDateTime endTime = (LocalDateTime) endFieldObj.get(value);

            if (startTime == null || endTime == null) {
                return true; // @NotNull will handle null checks
            }

            return startTime.isBefore(endTime);

        } catch (Exception e) {
            return false;
        }
    }
}
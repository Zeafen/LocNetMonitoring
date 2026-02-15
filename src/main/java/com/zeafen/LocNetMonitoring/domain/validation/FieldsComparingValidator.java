package com.zeafen.LocNetMonitoring.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class FieldsComparingValidator implements ConstraintValidator<FieldsCompare, Object> {
    private String greaterField;
    private String lesserField;
    private Boolean reverseComparing;
    private String message;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object greaterValue = new BeanWrapperImpl(o).getPropertyValue(greaterField);
        Object lesserValue = new BeanWrapperImpl(o).getPropertyValue(lesserField);

        boolean isValid = false;
        if (greaterValue != null && lesserValue != null) {
            if (greaterValue instanceof LocalDateTime greaterDate && lesserValue instanceof LocalDateTime lesserDate)
                isValid = reverseComparing ? lesserDate.isAfter(greaterDate) : lesserDate.isBefore(greaterDate);
            else if (greaterValue instanceof Integer greaterNum && lesserValue instanceof Integer lesserNum)
                isValid = reverseComparing ? lesserNum > greaterNum : lesserNum < greaterNum;
            else if (greaterValue instanceof BigDecimal greaterNum && lesserValue instanceof BigDecimal lesserNum)
                isValid = reverseComparing ? lesserNum.compareTo(greaterNum) > 0 : lesserNum.compareTo(greaterNum) < 0;
        } else if (greaterValue != null || lesserValue != null) {
            isValid = true;
        }

        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(greaterField)
                    .addConstraintViolation();
        }
        return isValid;
    }

    @Override
    public void initialize(FieldsCompare constraintAnnotation) {
        greaterField = constraintAnnotation.greaterField();
        lesserField = constraintAnnotation.lesserField();
        message = constraintAnnotation.message();
        reverseComparing = constraintAnnotation.comparingDirection() == ComparingDirection.Reverse;
    }
}

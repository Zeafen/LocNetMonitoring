package com.zeafen.LocNetMonitoring.domain.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldsComparingValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsCompare {
    String message() default "Fields do not match"; // Custom error message
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String greaterField();
    String lesserField();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsCompare[] value();
    }
}

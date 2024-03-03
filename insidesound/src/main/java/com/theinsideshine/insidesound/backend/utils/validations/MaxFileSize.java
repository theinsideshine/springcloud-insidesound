package com.theinsideshine.insidesound.backend.utils.validations;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxFileSizeValidator.class)
@Documented
public @interface MaxFileSize {
    String message() default "El tamaño del archivo excede el límite máximo permitido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    long value();
}

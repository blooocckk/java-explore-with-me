package ru.practicum.exception.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventDate {
    String message() default "Event date cannot be within 2 hours of the current time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package kh.com.kshrd.authentication.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinAgeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge {

    String message() default "Age must be at least {value} years";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value();
}

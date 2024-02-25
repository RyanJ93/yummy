package dev.enricosola.yummy.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = RestaurantTableListValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestaurantTableListConstraint {
    String message() default "Invalid restaurant table list.";
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
}

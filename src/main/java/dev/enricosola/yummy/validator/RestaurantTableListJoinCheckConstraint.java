package dev.enricosola.yummy.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = RestaurantTableListJoinCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestaurantTableListJoinCheckConstraint {
    String message() default "Cannot select multiple tables when a non-joinable table is selected as well.";
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
}

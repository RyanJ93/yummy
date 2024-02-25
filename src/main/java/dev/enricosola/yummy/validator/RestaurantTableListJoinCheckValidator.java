package dev.enricosola.yummy.validator;

import dev.enricosola.yummy.service.RestaurantTableService;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidator;
import java.util.List;

public class RestaurantTableListJoinCheckValidator implements ConstraintValidator<RestaurantTableListJoinCheckConstraint, List<Integer>> {
    private final RestaurantTableService restaurantTableService;

    public RestaurantTableListJoinCheckValidator(RestaurantTableService restaurantTableService){
        this.restaurantTableService = restaurantTableService;
    }

    @Override
    public boolean isValid(List<Integer> valueList, ConstraintValidatorContext constraintValidatorContext){
        return this.restaurantTableService.checkGroupJoinability(this.restaurantTableService.getMultiById(valueList));
    }
}

package dev.enricosola.yummy.validator;

import dev.enricosola.yummy.service.RestaurantTableService;
import jakarta.validation.ConstraintValidatorContext;
import dev.enricosola.yummy.entity.RestaurantTable;
import jakarta.validation.ConstraintValidator;
import java.util.HashSet;
import java.util.List;

public class RestaurantTableListValidator implements ConstraintValidator<RestaurantTableListConstraint, List<Integer>> {
    private final RestaurantTableService restaurantTableService;

    public RestaurantTableListValidator(RestaurantTableService restaurantTableService){
        this.restaurantTableService = restaurantTableService;
    }

    @Override
    public boolean isValid(List<Integer> valueList, ConstraintValidatorContext constraintValidatorContext){
        List<RestaurantTable> restaurantTableList = this.restaurantTableService.getAvailableList();
        List<Integer> IDList = restaurantTableList.stream().map(RestaurantTable::getId).toList();
        return new HashSet<>(IDList).containsAll(valueList);
    }
}

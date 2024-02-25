package dev.enricosola.yummy.helper;

import dev.enricosola.yummy.entity.RestaurantTable;
import java.util.stream.Collectors;
import java.util.Set;

public class RestaurantTableSetViewerHelper {
    public String displayRestaurantTableSet(Set<RestaurantTable> restaurantTableSet){
        if ( restaurantTableSet != null && !restaurantTableSet.isEmpty() ){
            return restaurantTableSet.stream().map(rt -> {
                return String.valueOf(rt.getId());
            }).collect(Collectors.joining(", "));
        }
        return "";
    }
}

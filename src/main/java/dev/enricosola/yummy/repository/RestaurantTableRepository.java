package dev.enricosola.yummy.repository;

import dev.enricosola.yummy.entity.RestaurantTable;
import java.util.List;

public interface RestaurantTableRepository {
    RestaurantTable findOne(Integer primaryKey);

    RestaurantTable update(RestaurantTable restaurantTable);

    RestaurantTable store(RestaurantTable restaurantTable);

    List<RestaurantTable> findMultiById(List<Integer> IdList);

    List<RestaurantTable> findAvailable();

    List<RestaurantTable> findJoinable();

    List<RestaurantTable> findAll();

    void delete(RestaurantTable restaurantTable);
}

package dev.enricosola.yummy.repository;

import dev.enricosola.yummy.entity.RestaurantTable;
import dev.enricosola.yummy.entity.Customer;
import java.util.List;

public interface CustomerRepository {
    Customer findByRestaurantTable(RestaurantTable restaurantTable);

    Customer findOne(Integer primaryKey);

    Customer update(Customer customer);

    Customer store(Customer customer);

    List<Customer> findAll(boolean includeFinalized);

    void delete(Customer customer);
}

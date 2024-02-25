package dev.enricosola.yummy.repository;

import dev.enricosola.yummy.entity.Customer;
import dev.enricosola.yummy.entity.Order;
import java.util.List;

public interface OrderRepository {
    Order findOne(Integer primaryKey);

    Order update(Order order);

    Order store(Order order);

    List<Order> findByCustomer(Customer customer, boolean includeReceived, boolean includeReady);

    List<Order> findAll();

    void delete(Order order);
}

package dev.enricosola.yummy.service;

import dev.enricosola.yummy.form.order.OrderForm;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.yummy.repository.OrderRepository;
import dev.enricosola.yummy.form.order.OrderCreateForm;
import dev.enricosola.yummy.form.order.OrderEditForm;
import org.springframework.stereotype.Service;
import dev.enricosola.yummy.entity.Customer;
import dev.enricosola.yummy.entity.MenuItem;
import dev.enricosola.yummy.entity.Order;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final MenuItemService menuItemService;
    private Order order;

    public OrderService(OrderRepository orderRepository, CustomerService customerService, MenuItemService menuItemService){
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.menuItemService = menuItemService;
    }

    public void setOrder(Order order){
        this.order = order;
    }

    public Order getOrder(){
        return this.order;
    }

    public Order getById(int id){
        return this.order = this.orderRepository.findOne(id);
    }

    public List<Order> getAllByCustomer(Customer customer, boolean includeReceived, boolean includeReady){
        return this.orderRepository.findByCustomer(customer, includeReceived, includeReady);
    }

    public Order createFromForm(Customer customer, OrderForm orderForm){
        MenuItem menuItem = this.menuItemService.getById(orderForm.getMenuItemId());
        return this.create(customer, menuItem, orderForm.getQuantity());
    }

    public Order updateFromForm(OrderEditForm orderEditForm){
        return this.update(orderEditForm.getQuantity());
    }

    public Order create(Customer customer, MenuItem menuItem, int quantity){
        if ( menuItem == null ){
            throw new IllegalArgumentException("You must pick a menu item.");
        }
        if ( customer == null ){
            throw new IllegalArgumentException("You must pick a customer.");
        }
        if ( quantity <= 0 ){
            throw new IllegalArgumentException("Quantity cannot be lower than 1.");
        }
        Order order = new Order();
        order.setCustomer(customer);
        order.setMenuItem(menuItem);
        order.setQuantity(quantity);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setReceived(false);
        order.setReady(false);
        return this.order = this.orderRepository.store(order);
    }

    public Order update(int quantity){
        this.order.setQuantity(quantity);
        this.order.setUpdatedAt(new Date());
        return this.orderRepository.update(this.order);
    }

    public Order updateReceived(boolean received){
        this.order.setUpdatedAt(new Date());
        this.order.setReceived(received);
        return this.orderRepository.update(this.order);
    }

    public Order updateReady(boolean ready){
        this.order.setUpdatedAt(new Date());
        this.order.setReady(ready);
        return this.orderRepository.update(this.order);
    }

    public void delete(){
        this.orderRepository.delete(this.order);
        this.order = null;
    }
}

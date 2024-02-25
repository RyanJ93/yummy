package dev.enricosola.yummy.service;

import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.yummy.form.customer.CustomerCreateForm;
import dev.enricosola.yummy.repository.CustomerRepository;
import dev.enricosola.yummy.entity.RestaurantTable;
import org.springframework.stereotype.Service;
import dev.enricosola.yummy.entity.Customer;
import dev.enricosola.yummy.entity.Menu;
import java.util.HashSet;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CustomerService {
    private final RestaurantTableService restaurantTableService;
    private final CustomerRepository customerRepository;
    private final MenuService menuService;
    private Customer customer;

    public CustomerService(CustomerRepository customerRepository, MenuService menuService, RestaurantTableService restaurantTableService){
        this.restaurantTableService = restaurantTableService;
        this.customerRepository = customerRepository;
        this.menuService = menuService;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public Customer getCustomer(){
        return this.customer;
    }

    public Customer getById(int id){
        return this.customer = this.customerRepository.findOne(id);
    }

    public Customer getCustomerByRestaurantTable(RestaurantTable restaurantTable){
        return this.customerRepository.findByRestaurantTable(restaurantTable);
    }

    public List<Customer> getAll(boolean includeFinalized){
        return this.customerRepository.findAll(includeFinalized);
    }

    public Customer createFromForm(CustomerCreateForm customerCreateForm){
        Menu menu = this.menuService.getById(customerCreateForm.getMenuId());
        Set<RestaurantTable> restaurantTableSet = new HashSet<>();
        customerCreateForm.getRestaurantTableIdList().forEach(rt -> {
            RestaurantTable restaurantTable = this.restaurantTableService.getById(rt);
            if ( restaurantTable != null ){
                restaurantTableSet.add(restaurantTable);
            }
        });
        return this.create(menu, restaurantTableSet, customerCreateForm.getName());
    }

    public Customer create(Menu menu, Set<RestaurantTable> restaurantTableSet, String name){
        if ( restaurantTableSet == null || restaurantTableSet.isEmpty() ){
            throw new IllegalArgumentException("At least one table must be assigned.");
        }
        if ( menu == null ){
            throw new IllegalArgumentException("You must pick a menu.");
        }
        Customer customer = new Customer();
        customer.setRestaurantTableSet(restaurantTableSet);
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        customer.setName(name);
        customer.setMenu(menu);
        this.customer = this.customerRepository.store(customer);
        if ( name == null || name.isEmpty() || name.isBlank() ){
            this.customer.setName("C-" + this.customer.getId());
            this.customerRepository.update(this.customer);
        }
        return this.customer;
    }

    public void toggleFinalized(){
        this.customer.setFinalizedAt(this.customer.getFinalizedAt() == null ? new Date() : null);
        this.customerRepository.update(this.customer);
    }

    public void delete(){
        this.customerRepository.delete(this.customer);
        this.customer = null;
    }
}

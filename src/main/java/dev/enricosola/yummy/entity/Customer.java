package dev.enricosola.yummy.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Date;
import java.io.Serial;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 5449572455193184760L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "customer_restaurant_tables",
        joinColumns = { @JoinColumn(name = "customer_id") },
        inverseJoinColumns = { @JoinColumn(name = "restaurant_table_id") }
    )
    private Set<RestaurantTable> restaurantTableSet = new HashSet<>();

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finalized_at")
    private Date finalizedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<Order> orderList = new HashSet<>();

    public Customer(){}

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setMenu(Menu menu){
        this.menu = menu;
    }

    public Menu getMenu(){
        return this.menu;
    }

    public void setRestaurantTableSet(Set<RestaurantTable> restaurantTableSet){
        this.restaurantTableSet = restaurantTableSet;
    }

    public Set<RestaurantTable> getRestaurantTableSet(){
        return this.restaurantTableSet;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setFinalizedAt(Date finalizedAt){
        this.finalizedAt = finalizedAt;
    }

    public Date getFinalizedAt(){
        return this.finalizedAt;
    }

    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }

    public Date getCreatedAt(){
        return this.createdAt;
    }

    public void setUpdatedAt(Date updatedAt){
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt(){
        return this.updatedAt;
    }

    public Set<Order> getOrderSet(){
        return this.orderList;
    }

    public double getSubtotal(){
        Iterator<Order> orderIterator = this.getOrderSet().iterator();
        double subtotal = 0;
        while ( orderIterator.hasNext() ){
            Order order = orderIterator.next();
            MenuItem menuItem = order.getMenuItem();
            if ( menuItem != null ){
                subtotal += order.getQuantity() * menuItem.getPrice();
            }
        }
        return subtotal;
    }

    public int getPurchaseCount(){
        Iterator<Order> orderIterator = this.getOrderSet().iterator();
        int purchaseCount = 0;
        while ( orderIterator.hasNext() ){
            Order order = orderIterator.next();
            purchaseCount += order.getQuantity();
        }
        return purchaseCount;
    }
}

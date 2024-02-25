package dev.enricosola.yummy.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "available_seats")
    private int availableSeats;

    @Column(name = "joinable")
    private boolean joinable;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "restaurantTableSet")
    private Set<Customer> customerSet = new HashSet<>();

    public RestaurantTable(){}

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setAvailableSeats(int availableSeats){
        this.availableSeats = availableSeats;
    }

    public int getAvailableSeats(){
        return this.availableSeats;
    }

    public void setJoinable(boolean joinable){
        this.joinable = joinable;
    }

    public boolean getJoinable(){
        return this.joinable;
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

    public Set<Customer> getCustomerSet(){
        return this.customerSet;
    }
}

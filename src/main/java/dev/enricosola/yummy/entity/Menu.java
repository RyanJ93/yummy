package dev.enricosola.yummy.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Date;
import java.io.Serial;
import java.util.Set;

@Entity
@Table(name = "menus")
public class Menu implements Serializable {
    @Serial
    private static final long serialVersionUID = -6105165802458409103L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    private Set<MenuSection> menuSectionSet = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    private Set<MenuItem> menuItemSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    private Set<Customer> customerSet = new HashSet<>();

    public Menu(){}

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
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

    public Set<MenuSection> getMenuSectionSet(){
        return this.menuSectionSet;
    }

    public Set<MenuItem> getMenuItemSet(){
        return this.menuItemSet;
    }

    public Set<Customer> getCustomerSet(){
        return this.customerSet;
    }
}

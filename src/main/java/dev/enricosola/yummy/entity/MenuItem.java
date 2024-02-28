package dev.enricosola.yummy.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Date;
import java.io.Serial;
import java.util.Set;

@Entity
@Table(name = "menu_items")
public class MenuItem implements Serializable {
    @Serial
    private static final long serialVersionUID = -5666434958120393167L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_section_id")
    private MenuSection menuSection;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "price")
    private float price;

    @Column(name = "picture")
    private String picture;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menuItem")
    private Set<Order> orderSet = new HashSet<>();

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

    public void setMenuSection(MenuSection menuSection){
        this.menuSection = menuSection;
    }

    public MenuSection getMenuSection(){
        return this.menuSection;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    public String getIngredients(){
        return this.ingredients;
    }

    public void setPrice(float price){
        this.price = price;
    }

    public float getPrice(){
        return this.price;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }

    public String getPicture(){
        return this.picture;
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
        return this.orderSet;
    }
}

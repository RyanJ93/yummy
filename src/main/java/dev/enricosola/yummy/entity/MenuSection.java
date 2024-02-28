package dev.enricosola.yummy.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Date;
import java.io.Serial;
import java.util.Set;

@Entity
@Table(name = "menu_sections")
public class MenuSection implements Serializable {
    @Serial
    private static final long serialVersionUID = 7454482221601729930L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "name")
    private String name;

    @Column(name = "ordering")
    private int ordering;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menuSection")
    private Set<MenuItem> menuItemSet = new HashSet<>();

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

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setOrdering(int ordering){
        this.ordering = ordering;
    }

    public int getOrdering(){
        return this.ordering;
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

    public Set<MenuItem> getMenuItemSet(){
        return this.menuItemSet;
    }
}

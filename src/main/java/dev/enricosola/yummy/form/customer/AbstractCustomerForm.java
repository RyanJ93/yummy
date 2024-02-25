package dev.enricosola.yummy.form.customer;

import dev.enricosola.yummy.validator.RestaurantTableListJoinCheckConstraint;
import dev.enricosola.yummy.validator.RestaurantTableListConstraint;
import jakarta.validation.constraints.Size;
import dev.enricosola.yummy.form.Form;
import java.util.List;

public abstract class AbstractCustomerForm implements Form, CustomerForm {
    @RestaurantTableListJoinCheckConstraint(message = "You cannot select multiple tables when a non-joinable table is selected as well.")
    @RestaurantTableListConstraint(message = "At least one available table must be selected.")
    protected List<Integer> restaurantTableIdList;

    @Size(max = 100, message = "Name cannot be longer than 100 characters.")
    protected String name;

    protected int menuId;

    public void setRestaurantTableIdList(List<Integer> restaurantTableIdList){
        this.restaurantTableIdList = restaurantTableIdList;
    }

    public List<Integer> getRestaurantTableIdList(){
        return this.restaurantTableIdList;
    }

    public void setMenuId(int menuId){
        this.menuId = menuId;
    }

    public int getMenuId(){
        return this.menuId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}

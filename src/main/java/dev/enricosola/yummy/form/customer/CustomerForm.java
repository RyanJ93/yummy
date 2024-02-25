package dev.enricosola.yummy.form.customer;

import java.util.List;

public interface CustomerForm {
    void setRestaurantTableIdList(List<Integer> restaurantTableIdList);

    List<Integer> getRestaurantTableIdList();

    void setMenuId(int menuId);

    int getMenuId();

    void setName(String name);

    String getName();
}

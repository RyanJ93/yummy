package dev.enricosola.yummy.form.restauranttable;

import dev.enricosola.yummy.entity.RestaurantTable;
import dev.enricosola.yummy.form.Form;

public class RestaurantTableEditForm extends AbstractRestaurantTableForm implements Form, RestaurantTableForm {
    public RestaurantTableEditForm(){}

    public RestaurantTableEditForm(RestaurantTable restaurantTable){
        this.availableSeats = restaurantTable.getAvailableSeats();
        this.joinable = restaurantTable.getJoinable();
    }
}

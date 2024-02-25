package dev.enricosola.yummy.form.restauranttable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import dev.enricosola.yummy.form.Form;

public abstract class AbstractRestaurantTableForm implements Form, RestaurantTableForm {
    @Min(value = 1, message = "Table must have at least one available seat.")
    @Max(value = 99, message = "Table must have max 99 seats.")
    protected int availableSeats;
    protected boolean joinable;

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
}

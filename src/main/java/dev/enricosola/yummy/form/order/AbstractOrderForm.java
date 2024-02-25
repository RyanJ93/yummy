package dev.enricosola.yummy.form.order;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import dev.enricosola.yummy.form.Form;

public abstract class AbstractOrderForm implements Form, OrderForm {
    @Min(value = 1, message = "A menu item must be selected.")
    protected int menuItemId;

    @Max(value = 99, message = "Order quantity cannot be greater than 99.")
    @Min(value = 1, message = "Order quantity cannot be lower than 0.")
    protected int quantity;

    public void setMenuItemId(int menuItemId){
        this.menuItemId = menuItemId;
    }

    public int getMenuItemId(){
        return this.menuItemId;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return this.quantity;
    }
}

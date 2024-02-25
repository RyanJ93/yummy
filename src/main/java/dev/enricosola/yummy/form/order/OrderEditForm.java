package dev.enricosola.yummy.form.order;

import dev.enricosola.yummy.entity.Order;
import dev.enricosola.yummy.form.Form;

public class OrderEditForm extends AbstractOrderForm implements Form, OrderForm {
    public OrderEditForm(Order order){
        this.menuItemId = order.getMenuItem().getId();
        this.quantity = order.getQuantity();
    }

    public OrderEditForm(){}
}

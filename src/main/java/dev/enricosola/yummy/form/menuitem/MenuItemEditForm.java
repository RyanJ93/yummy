package dev.enricosola.yummy.form.menuitem;

import dev.enricosola.yummy.entity.MenuItem;
import dev.enricosola.yummy.form.Form;

public class MenuItemEditForm extends AbstractMenuItemForm implements Form, MenuItemForm {
    private boolean removePicture = false;

    public MenuItemEditForm(){}

    public MenuItemEditForm(MenuItem menuItem){
        this.description = menuItem.getDescription();
        this.ingredients = menuItem.getIngredients();
        this.price = menuItem.getPrice();
        this.name = menuItem.getName();
    }

    public void setRemovePicture(boolean removePicture){
        this.removePicture = removePicture;
    }

    public boolean getRemovePicture(){
        return this.removePicture;
    }
}

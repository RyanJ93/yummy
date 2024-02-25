package dev.enricosola.yummy.form.menu;

import dev.enricosola.yummy.entity.Menu;
import dev.enricosola.yummy.form.Form;

public class MenuEditForm extends AbstractMenuForm implements MenuForm, Form {
    public MenuEditForm(){}

    public MenuEditForm(Menu menu){
        this.name = menu.getName();
    }
}

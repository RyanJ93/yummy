package dev.enricosola.yummy.form.menusection;

import dev.enricosola.yummy.entity.MenuSection;
import dev.enricosola.yummy.form.Form;

public class MenuSectionEditForm extends AbstractMenuSectionForm implements Form, MenuSectionForm {
    public MenuSectionEditForm(){}

    public MenuSectionEditForm(MenuSection menuSection){
        this.ordering = menuSection.getOrdering();
        this.name = menuSection.getName();
    }
}

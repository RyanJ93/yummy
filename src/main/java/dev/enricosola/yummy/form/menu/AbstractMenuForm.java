package dev.enricosola.yummy.form.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import dev.enricosola.yummy.form.Form;

public abstract class AbstractMenuForm implements MenuForm, Form {
    @Size(max = 100, message = "Name cannot be longer than 100 characters.")
    @NotBlank(message = "You must provide a valid name.")
    protected String name;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}

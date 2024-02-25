package dev.enricosola.yummy.form.menusection;

import jakarta.validation.constraints.*;
import dev.enricosola.yummy.form.Form;

public abstract class AbstractMenuSectionForm implements Form, MenuSectionForm {
    @Min(value = 0, message = "Order value cannot be lower than 0.")
    protected int ordering;

    @Size(max = 100, message = "Name cannot be longer than 100 characters.")
    @NotBlank(message = "You must provide a valid name.")
    protected String name;

    public void setOrdering(int ordering){
        this.ordering = ordering;
    }

    public int getOrdering(){
        return this.ordering;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}

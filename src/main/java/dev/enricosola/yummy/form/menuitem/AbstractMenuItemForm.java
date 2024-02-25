package dev.enricosola.yummy.form.menuitem;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;
import dev.enricosola.yummy.form.Form;

public abstract class AbstractMenuItemForm implements Form, MenuItemForm {
    protected Integer menuSectionId;
    protected MultipartFile picture;

    @Size(max = 65535, message = "Description is too long.")
    protected String description;

    @Size(max = 65535, message = "Ingredients description is too long.")
    protected String ingredients;

    @Min(value = 0, message = "Price cannot be negative.")
    protected float price;

    @Size(max = 100, message = "Name cannot be longer than 100 characters.")
    @NotBlank(message = "You must provide a valid name.")
    protected String name;

    public void setMenuSectionId(Integer menuSectionId){
        this.menuSectionId = menuSectionId;
    }

    public Integer getMenuSectionId(){
        return this.menuSectionId;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    public String getIngredients(){
        return this.ingredients;
    }

    public void setPrice(float price){
        this.price = price;
    }

    public float getPrice(){
        return this.price;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setPicture(MultipartFile picture){
        this.picture = picture;
    }

    public MultipartFile getPicture(){
        return this.picture;
    }
}

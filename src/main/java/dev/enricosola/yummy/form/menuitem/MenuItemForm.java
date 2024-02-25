package dev.enricosola.yummy.form.menuitem;

import org.springframework.web.multipart.MultipartFile;

public interface MenuItemForm {
    void setMenuSectionId(Integer menuSectionId);

    Integer getMenuSectionId();

    void setDescription(String description);

    String getDescription();

    void setIngredients(String ingredients);

    String getIngredients();

    void setPrice(float price);

    float getPrice();

    void setName(String name);

    String getName();

    void setPicture(MultipartFile picture);

    MultipartFile getPicture();
}

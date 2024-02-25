package dev.enricosola.yummy.service;

import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.yummy.form.menuitem.MenuItemCreateForm;
import dev.enricosola.yummy.form.menuitem.MenuItemEditForm;
import dev.enricosola.yummy.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import dev.enricosola.yummy.form.menuitem.MenuItemForm;
import dev.enricosola.yummy.entity.MenuSection;
import org.springframework.stereotype.Service;
import dev.enricosola.yummy.entity.MenuItem;
import dev.enricosola.yummy.utils.FileUtils;
import dev.enricosola.yummy.entity.Menu;
import java.io.IOException;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import java.io.File;

@Service
@Transactional
public class MenuItemService {
    private final MenuSectionService menuSectionService;
    private final MenuItemRepository menuItemRepository;
    private MenuItem menuItem;

    @Value("${yummy.storage}")
    private String storage;

    private String storeUploadedPicture(MultipartFile picture) throws IOException {
        String originalFileName = Objects.requireNonNull(picture.getOriginalFilename());
        String extension = FileUtils.getExtensionByStringHandling(originalFileName);
        String filename = this.menuItem.getId() + "." + extension;
        picture.transferTo(new File(this.storage + "/" + filename));
        return filename;
    }

    private MenuSection lookupMenuSectionFromForm(MenuItemForm menuItemForm){
        Integer menuSectionId = menuItemForm.getMenuSectionId();
        MenuSection menuSection = null;
        if ( menuSectionId != null && menuSectionId > 0 ){
            menuSection = this.menuSectionService.getById(menuSectionId);
        }
        return menuSection;
    }

    public MenuItemService(MenuItemRepository menuItemRepository, MenuSectionService menuSectionService){
        this.menuItemRepository = menuItemRepository;
        this.menuSectionService = menuSectionService;
    }

    public MenuItem getById(int id){
        return this.menuItem = this.menuItemRepository.findOne(id);
    }

    public void setMenuItem(MenuItem menuItem){
        this.menuItem = menuItem;
    }

    public MenuItem getMenuItem(){
        return this.menuItem;
    }

    public List<MenuItem> getAll(Menu menu){
        return this.menuItemRepository.findAll(menu);
    }

    public MenuItem createFromForm(Menu menu, MenuItemCreateForm menuItemCreateForm) throws IOException {
        MenuSection menuSection = this.lookupMenuSectionFromForm(menuItemCreateForm);
        this.create(menu, menuSection, menuItemCreateForm.getName(), menuItemCreateForm.getPrice(), menuItemCreateForm.getDescription(), menuItemCreateForm.getIngredients(), null);
        MultipartFile picture = menuItemCreateForm.getPicture();
        if ( picture != null && picture.getOriginalFilename() != null ){
            this.menuItem.setPicture(this.storeUploadedPicture(picture));
            this.menuItemRepository.update(this.menuItem);
        }
        return this.menuItem;
    }

    public MenuItem updateFromForm(MenuItemEditForm menuItemEditForm){
        MenuSection menuSection = this.lookupMenuSectionFromForm(menuItemEditForm);
        if ( menuItemEditForm.getRemovePicture() ){
            this.removePicture();
        }
        return this.update(menuSection, menuItemEditForm.getName(), menuItemEditForm.getPrice(), menuItemEditForm.getDescription(), menuItemEditForm.getIngredients(), null);
    }

    public MenuItem create(Menu menu, MenuSection menuSection, String name, float price, String description, String ingredients, String picture){
        this.menuItem = new MenuItem();
        this.menuItem.setDescription(description);
        this.menuItem.setIngredients(ingredients);
        this.menuItem.setMenuSection(menuSection);
        this.menuItem.setCreatedAt(new Date());
        this.menuItem.setUpdatedAt(new Date());
        this.menuItem.setPicture(picture);
        this.menuItem.setPrice(price);
        this.menuItem.setName(name);
        this.menuItem.setMenu(menu);
        return this.menuItemRepository.store(this.menuItem);
    }

    public MenuItem update(MenuSection menuSection, String name, float price, String description, String ingredients, String picture){
        this.menuItem.setDescription(description);
        this.menuItem.setIngredients(ingredients);
        this.menuItem.setMenuSection(menuSection);
        this.menuItem.setUpdatedAt(new Date());
        this.menuItem.setPicture(picture);
        this.menuItem.setPrice(price);
        this.menuItem.setName(name);
        return this.menuItemRepository.update(this.menuItem);
    }

    public void removePicture(){
        String picture = this.menuItem.getPicture();
        if ( picture != null && !picture.isEmpty() ){
            File file = new File(this.storage + "/" + picture);
            if ( file.exists() && file.delete() ){
                this.menuItem.setPicture(null);
                this.menuItemRepository.update(this.menuItem);
            }
        }
    }

    public void delete(){
        this.menuItemRepository.delete(this.menuItem);
        this.menuItem = null;
    }
}

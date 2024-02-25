package dev.enricosola.yummy.service;

import dev.enricosola.yummy.form.menusection.MenuSectionCreateForm;
import dev.enricosola.yummy.form.menusection.MenuSectionEditForm;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.yummy.repository.MenuSectionRepository;
import dev.enricosola.yummy.entity.MenuSection;
import org.springframework.stereotype.Service;
import dev.enricosola.yummy.entity.Menu;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MenuSectionService {
    private final MenuSectionRepository menuSectionRepository;
    private MenuSection menuSection;

    public MenuSectionService(MenuSectionRepository menuSectionRepository){
        this.menuSectionRepository = menuSectionRepository;
    }

    public MenuSection getById(int id){
        return this.menuSection = this.menuSectionRepository.findOne(id);
    }

    public void setMenuSection(MenuSection menuSection){
        this.menuSection = menuSection;
    }

    public MenuSection getMenuSection(){
        return this.menuSection;
    }

    public List<MenuSection> getAll(Menu menu){
        return this.menuSectionRepository.findAll(menu);
    }

    public MenuSection createFromForm(Menu menu, MenuSectionCreateForm menuSectionCreateForm){
        return this.create(menu, menuSectionCreateForm.getName(), menuSectionCreateForm.getOrdering());
    }

    public MenuSection updateFromForm(MenuSectionEditForm menuSectionEditForm){
        return this.update(menuSectionEditForm.getName(), menuSectionEditForm.getOrdering());
    }

    public MenuSection create(Menu menu, String name, int ordering){
        this.menuSection = new MenuSection();
        this.menuSection.setOrdering(ordering);
        this.menuSection.setMenu(menu);
        this.menuSection.setName(name);
        this.menuSection.setCreatedAt(new Date());
        this.menuSection.setUpdatedAt(new Date());
        return this.menuSectionRepository.store(this.menuSection);
    }

    public MenuSection update(String name, int ordering){
        this.menuSection.setUpdatedAt(new Date());
        this.menuSection.setOrdering(ordering);
        this.menuSection.setName(name);
        return this.menuSectionRepository.update(this.menuSection);
    }

    public void delete(){
        this.menuSectionRepository.delete(this.menuSection);
        this.menuSection = null;
    }
}

package dev.enricosola.yummy.service;

import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.yummy.repository.MenuRepository;
import dev.enricosola.yummy.form.menu.MenuCreateForm;
import dev.enricosola.yummy.form.menu.MenuEditForm;
import org.springframework.stereotype.Service;
import dev.enricosola.yummy.entity.MenuItem;
import dev.enricosola.yummy.entity.Menu;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.*;

@Service
@Transactional
public class MenuService {
    private static Logger logger = LoggerFactory.getLogger(MenuService.class);
    private final MenuRepository menuRepository;
    private Menu menu;

    public MenuService(MenuRepository menuRepository){
        this.menuRepository = menuRepository;
    }

    public void setMenu(Menu menu){
        this.menu = menu;
    }

    public Menu getMenu(){
        return this.menu;
    }

    public Menu getById(int id){
        return this.menu = this.menuRepository.findOne(id);
    }

    public List<Menu> getAll(){
        return this.menuRepository.findAll();
    }

    public Menu createFromForm(MenuCreateForm menuCreateForm){
        return this.create(menuCreateForm.getName());
    }

    public Menu updateFromForm(MenuEditForm menuEditForm){
        return this.update(menuEditForm.getName());
    }

    public Menu create(String name){
        this.menu = new Menu();
        this.menu.setName(name);
        this.menu.setCreatedAt(new Date());
        this.menu.setUpdatedAt(new Date());
        return this.menuRepository.store(this.menu);
    }

    public Menu update(String name){
        this.menu.setUpdatedAt(new Date());
        this.menu.setName(name);
        MenuService.logger.info("Updating menu " + this.menu.getId());
        return this.menuRepository.update(this.menu);
    }

    public void delete(){
        this.menuRepository.delete(this.menu);
        this.menu = null;
    }

    public Map<String, Set<MenuItem>> getExpandedMenu(){
        Map<String, Set<MenuItem>> expandedMenu = new HashMap<>();
        this.menu.getMenuItemSet().forEach((menuItem) -> {
            String sectionName = null;
            if ( menuItem.getMenuSection() != null ){
                sectionName = menuItem.getMenuSection().getName();
            }
            if ( !expandedMenu.containsKey(sectionName) ){
                expandedMenu.put(sectionName, new HashSet<>());
            }
            expandedMenu.get(sectionName).add(menuItem);
        });
        return expandedMenu;
    }
}

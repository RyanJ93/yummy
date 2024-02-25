package dev.enricosola.yummy.repository;

import dev.enricosola.yummy.entity.Menu;
import dev.enricosola.yummy.entity.MenuSection;
import java.util.List;

public interface MenuSectionRepository {
    MenuSection findOne(Integer primaryKey);

    MenuSection update(MenuSection menuSection);

    MenuSection store(MenuSection menuSection);

    List<MenuSection> findAll(Menu menu);

    void delete(MenuSection menuSection);
}

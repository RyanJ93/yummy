package dev.enricosola.yummy.repository;

import dev.enricosola.yummy.entity.MenuSection;
import dev.enricosola.yummy.entity.MenuItem;
import dev.enricosola.yummy.entity.Menu;
import java.util.List;

public interface MenuItemRepository {
    MenuItem findOne(Integer primaryKey);

    MenuItem update(MenuItem menuItem);

    MenuItem store(MenuItem menuItem);

    List<MenuItem> findAllInSection(Menu menu, MenuSection menuSection);

    List<MenuItem> findAll(Menu menu);

    void delete(MenuItem menuItem);
}

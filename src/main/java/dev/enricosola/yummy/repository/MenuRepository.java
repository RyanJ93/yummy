package dev.enricosola.yummy.repository;

import dev.enricosola.yummy.entity.Menu;
import java.util.List;

public interface MenuRepository {
    Menu findOne(Integer primaryKey);

    Menu update(Menu menu);

    Menu store(Menu menu);

    List<Menu> findAll();

    void delete(Menu menu);
}

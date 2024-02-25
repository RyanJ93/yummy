package dev.enricosola.yummy.repository;

import org.springframework.stereotype.Repository;
import dev.enricosola.yummy.entity.Menu;
import java.util.List;

@Repository
public class MenuRepositoryImpl extends AbstractRepository<Menu, Integer> implements MenuRepository {
    public Menu findOne(Integer primaryKey){
        return super.findOne(primaryKey);
    }

    public Menu update(Menu menu){
        return super.update(menu);
    }

    public Menu store(Menu menu){
        return super.store(menu);
    }

    public List<Menu> findAll(){
        return super.findAll();
    }

    public void delete(Menu menu){
        super.delete(menu);
    }
}

package dev.enricosola.yummy.repository;

import dev.enricosola.yummy.entity.Menu;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.stereotype.Repository;
import dev.enricosola.yummy.entity.MenuSection;
import java.util.List;

@Repository
public class MenuSectionRepositoryImpl extends AbstractRepository<MenuSection, Integer> implements MenuSectionRepository {
    public MenuSection findOne(Integer primaryKey){
        return super.findOne(primaryKey);
    }

    public MenuSection update(MenuSection menuSection){
        return super.update(menuSection);
    }

    public MenuSection store(MenuSection menuSection){
        return super.store(menuSection);
    }

    public List<MenuSection> findAll(Menu menu){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MenuSection> query = criteriaBuilder.createQuery(this.entityClass);
        CriteriaQuery<MenuSection> filter = query.where(criteriaBuilder.equal(query.from(this.entityClass).get("menu"), menu));
        return this.entityManager.createQuery(filter).getResultList();
    }

    public void delete(MenuSection menuSection){
        super.delete(menuSection);
    }
}

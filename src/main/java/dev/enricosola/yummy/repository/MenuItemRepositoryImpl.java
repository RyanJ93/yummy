package dev.enricosola.yummy.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.stereotype.Repository;
import dev.enricosola.yummy.entity.MenuSection;
import jakarta.persistence.criteria.Predicate;
import dev.enricosola.yummy.entity.MenuItem;
import dev.enricosola.yummy.entity.Menu;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MenuItemRepositoryImpl extends AbstractRepository<MenuItem, Integer> implements MenuItemRepository {
    public MenuItem findOne(Integer primaryKey){
        return super.findOne(primaryKey);
    }

    public MenuItem update(MenuItem menuItem){
        return super.update(menuItem);
    }

    public MenuItem store(MenuItem menuItem){
        return super.store(menuItem);
    }

    public List<MenuItem> findAllInSection(Menu menu, MenuSection menuSection){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MenuItem> query = criteriaBuilder.createQuery(this.entityClass);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(query.from(this.entityClass).get("menuSection"), menuSection));
        predicates.add(criteriaBuilder.equal(query.from(this.entityClass).get("menu"), menu));
        query.select(query.from(this.entityClass)).where(predicates.toArray(new Predicate[]{}));
        return this.entityManager.createQuery(query).getResultList();
    }

    public List<MenuItem> findAll(Menu menu){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MenuItem> query = criteriaBuilder.createQuery(this.entityClass);
        CriteriaQuery<MenuItem> filter = query.where(criteriaBuilder.equal(query.from(this.entityClass).get("menu"), menu));
        return this.entityManager.createQuery(filter).getResultList();
    }

    public void delete(MenuItem menuItem){
        super.delete(menuItem);
    }
}

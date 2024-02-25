package dev.enricosola.yummy.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import dev.enricosola.yummy.entity.RestaurantTable;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RestaurantTableRepositoryImpl extends AbstractRepository<RestaurantTable, Integer> implements RestaurantTableRepository {
    public RestaurantTable findOne(Integer primaryKey){
        return super.findOne(primaryKey);
    }

    public RestaurantTable update(RestaurantTable restaurantTable){
        return super.update(restaurantTable);
    }

    public RestaurantTable store(RestaurantTable restaurantTable){
        return super.store(restaurantTable);
    }

    public List<RestaurantTable> findMultiById(List<Integer> IdList){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<RestaurantTable> query = criteriaBuilder.createQuery(this.entityClass);
        CriteriaQuery<RestaurantTable> filter = query.where(query.from(this.entityClass).get("id").in(IdList));
        return this.entityManager.createQuery(filter).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<RestaurantTable> findAvailable(){
        // TODO: Implement this query using JPA features rather than using a native MySQL query.
        StringBuilder queryBuilder = new StringBuilder("SELECT restaurant_tables.* FROM restaurant_tables WHERE ");
        queryBuilder.append("restaurant_tables.id NOT IN ( SELECT DISTINCT restaurant_tables.id FROM restaurant_tables ");
        queryBuilder.append("JOIN customer_restaurant_tables ON restaurant_tables.id = customer_restaurant_tables.restaurant_table_id ");
        queryBuilder.append("JOIN customers ON customers.id = customer_restaurant_tables.customer_id ");
        queryBuilder.append("WHERE customers.finalized_at IS NULL ) ORDER BY restaurant_tables.id DESC;");
        return this.entityManager.createNativeQuery(queryBuilder.toString(), RestaurantTable.class).getResultList();
    }

    public List<RestaurantTable> findJoinable(){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<RestaurantTable> query = criteriaBuilder.createQuery(this.entityClass);
        CriteriaQuery<RestaurantTable> filter = query.where(criteriaBuilder.equal(query.from(this.entityClass).get("joinable"), true));
        return this.entityManager.createQuery(filter).getResultList();
    }

    public List<RestaurantTable> findAll(){
        return super.findAll();
    }

    public void delete(RestaurantTable restaurantTable){
        super.delete(restaurantTable);
    }
}

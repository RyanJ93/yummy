package dev.enricosola.yummy.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import dev.enricosola.yummy.entity.RestaurantTable;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.Predicate;
import dev.enricosola.yummy.entity.Customer;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepositoryImpl extends AbstractRepository<Customer, Integer> implements CustomerRepository {
    public Customer findByRestaurantTable(RestaurantTable restaurantTable){
        // TODO: Implement this query using JPA features rather than using a native MySQL query.
        StringBuilder queryBuilder = new StringBuilder("SELECT customer.* FROM customers ");
        queryBuilder.append("JOIN customer_restaurant_tables ON customers.id = customer_restaurant_tables.customer_id ");
        queryBuilder.append("JOIN restaurant_tables ON restaurant_tables.id = customer_restaurant_tables.restaurant_table_id ");
        queryBuilder.append("WHERE customers.finalized_at IS NULL AND restaurant_tables.id = :id ORDER BY id DESC;");
        Query query = this.entityManager.createNativeQuery(queryBuilder.toString(), Customer.class);
        query.setParameter("id", restaurantTable.getId());
        return (Customer)query.getSingleResult();
    }

    public List<Customer> findAll(boolean includeFinalized){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(this.entityClass);
        List<Predicate> predicates = new ArrayList<>();
        if ( !includeFinalized ){
            predicates.add(criteriaBuilder.isNull(query.from(this.entityClass).get("finalizedAt")));
        }
        query.select(query.from(this.entityClass));
        if ( !predicates.isEmpty() ){
            query.where(predicates.toArray(new Predicate[]{}));
        }
        return this.entityManager.createQuery(query).getResultList();
    }

    public Customer findOne(Integer primaryKey){
        return super.findOne(primaryKey);
    }

    public Customer update(Customer customer){
        return super.update(customer);
    }

    public Customer store(Customer customer){
        return super.store(customer);
    }

    public void delete(Customer customer){
        super.delete(customer);
    }
}

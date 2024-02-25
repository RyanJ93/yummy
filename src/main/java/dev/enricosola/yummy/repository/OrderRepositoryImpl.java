package dev.enricosola.yummy.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;
import dev.enricosola.yummy.entity.Customer;
import dev.enricosola.yummy.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends AbstractRepository<Order, Integer> implements OrderRepository {
    public Order findOne(Integer primaryKey){
        return super.findOne(primaryKey);
    }

    public Order update(Order order){
        return super.update(order);
    }

    public Order store(Order order){
        return super.store(order);
    }

    public List<Order> findByCustomer(Customer customer, boolean includeReceived, boolean includeReady){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(this.entityClass);
        List<Predicate> predicates = new ArrayList<>();
        if ( !includeReceived ){
            predicates.add(criteriaBuilder.equal(query.from(this.entityClass).get("received"), true));
        }
        if ( !includeReady ){
            predicates.add(criteriaBuilder.equal(query.from(this.entityClass).get("ready"), true));
        }
        predicates.add(criteriaBuilder.equal(query.from(this.entityClass).get("customer"), customer));
        query.select(query.from(this.entityClass)).where(predicates.toArray(new Predicate[]{}));
        return this.entityManager.createQuery(query).getResultList();
    }

    public List<Order> findAll(){
        return super.findAll();
    }

    public void delete(Order order){
        super.delete(order);
    }
}

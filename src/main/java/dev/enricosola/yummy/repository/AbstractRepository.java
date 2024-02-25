package dev.enricosola.yummy.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractRepository<E extends Serializable, P extends Serializable> implements Repository<E,P> {
    @PersistenceContext
    protected EntityManager entityManager;
    protected final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractRepository(){
        ParameterizedType parameterizedType = (ParameterizedType)this.getClass().getGenericSuperclass();
        this.entityClass = (Class<E>)parameterizedType.getActualTypeArguments()[0];
    }

    public E findOne(P primaryKey){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(this.entityClass);
        CriteriaQuery<E> filter = query.where(criteriaBuilder.equal(query.from(this.entityClass).get("id"), primaryKey));
        return this.entityManager.createQuery(filter).getSingleResult();
    }

    public List<E> findAll(){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(this.entityClass);
        return this.entityManager.createQuery(query.select(query.from(this.entityClass))).getResultList();
    }

    public E update(E entity){
        this.entityManager.merge(entity);
        this.flushAndClear();
        return entity;
    }

    public E store(E entity){
        this.entityManager.persist(entity);
        this.flushAndClear();
        return entity;
    }

    public void delete(E entity){
        this.entityManager.remove(this.entityManager.contains(entity) ? entity : this.entityManager.merge(entity));
        this.flushAndClear();
    }

    private void flushAndClear(){
        this.entityManager.flush();
        this.entityManager.clear();
    }
}

package dev.enricosola.yummy.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.stereotype.Repository;
import dev.enricosola.yummy.entity.User;
import java.util.List;

@Repository
public class UserRepositoryImpl extends AbstractRepository<User, Integer> implements UserRepository {
    public User findByUsername(String username){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(this.entityClass);
        CriteriaQuery<User> filter = query.where(criteriaBuilder.equal(query.from(this.entityClass).get("username"), username));
        return this.entityManager.createQuery(filter).getSingleResult();
    }

    public User findOne(Integer primaryKey){
        return super.findOne(primaryKey);
    }

    public User update(User user){
        return super.update(user);
    }

    public User store(User user){
        return super.store(user);
    }

    public List<User> findAll(){
        return super.findAll();
    }

    public void delete(User user){
        super.delete(user);
    }
}

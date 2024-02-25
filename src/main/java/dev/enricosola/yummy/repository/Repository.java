package dev.enricosola.yummy.repository;

import java.io.Serializable;
import java.util.List;

public interface Repository<E extends Serializable, P extends Serializable> {
    E findOne(P primaryKey);

    List<E> findAll();

    E update(E entity);

    E store(E entity);

    void delete(E entity);
}

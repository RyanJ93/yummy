package dev.enricosola.yummy.repository;

import dev.enricosola.yummy.entity.User;
import java.util.List;

public interface UserRepository {
    User findByUsername(String username);

    User findOne(Integer primaryKey);

    User update(User user);

    User store(User user);

    List<User> findAll();

    void delete(User user);
}

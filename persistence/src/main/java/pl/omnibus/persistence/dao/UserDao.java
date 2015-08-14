package pl.omnibus.persistence.dao;

import pl.omnibus.domain.User;

import java.util.Optional;
import java.util.Set;

public interface UserDao {
    Set<User> fetchAll();
    Optional<User> fetchById(Long id);
    Optional<User> fetchByMail(String mail);
    Optional<User> fetchByUsername(String username);
}

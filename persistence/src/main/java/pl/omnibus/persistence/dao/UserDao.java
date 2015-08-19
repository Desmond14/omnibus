package pl.omnibus.persistence.dao;

import pl.omnibus.domain.User;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public interface UserDao {
    Set<User> fetchAll() throws SQLException;
    Optional<User> fetchById(Long id) throws SQLException;
    Optional<User> fetchByMail(String mail) throws SQLException;
    Optional<User> fetchByUsername(String username);
    Set<User> fetchByUsernameOrEmail(String username, String email);
    User save(User user);
}

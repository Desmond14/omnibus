package pl.omnibus.persistence.dao.impl;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.omnibus.domain.User;
import pl.omnibus.persistence.dao.UserDao;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static pl.omnibus.persistence.jooq.Tables.USERS;

@Repository
public class UserDaoImpl implements UserDao {
    private final Configuration configuration;

    @Autowired
    public UserDaoImpl(DataSource dataSource) {
        this.configuration = new DefaultConfiguration().set(SQLDialect.POSTGRES_9_4).set(dataSource);
    }

    public Set<User> fetchAll(){
        return new HashSet<>(DSL.using(configuration).select().from(USERS).fetch().into(User.class));
    }

    @Override
    public Optional<User> fetchById(Long id){
        return Optional.ofNullable(DSL.using(configuration)
                .select()
                .from(USERS)
                .where(USERS.ID.equal(id))
                .fetchOneInto(User.class));
    }

    @Override
    public Optional<User> fetchByMail(String mail) {
        return Optional.ofNullable(DSL.using(configuration)
                .select()
                .from(USERS)
                .where(USERS.EMAIL.equal(mail))
                .fetchOneInto(User.class));
    }

    @Override
    public Optional<User> fetchByUsername(String username) {
        return Optional.ofNullable(DSL.using(configuration)
                .select()
                .from(USERS)
                .where(USERS.USERNAME.equal(username))
                .fetchOneInto(User.class));
    }

    @Override
    public Set<User> fetchByUsernameOrEmail(String username, String email) {
        return new HashSet<>(DSL.using(configuration).
                select().from(USERS)
                .where(
                        USERS.USERNAME.equal(username).or(USERS.EMAIL.equal(email))
                )
                .fetch().into(User.class)
        );
    }

    @Override
    public User save(User user) {
        return DSL.using(configuration)
                .insertInto(USERS, USERS.USERNAME, USERS.EMAIL, USERS.PASSWORD)
                .values(user.getUsername(), user.getMailAddress(), user.getPassword())
                .returning(USERS.ID)
                .fetchOne().into(User.class);
    }
}

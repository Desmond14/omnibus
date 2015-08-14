package pl.omnibus.persistence.dao.impl;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import pl.omnibus.domain.User;
import pl.omnibus.persistence.dao.UserDao;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static pl.omnibus.persistence.jooq.Tables.USERS;

public class UserDaoImpl implements UserDao {
    private final DataSource dataSource;
    private final DSLContext create;

    public UserDaoImpl(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        this.create = DSL.using(dataSource.getConnection(), SQLDialect.POSTGRES_9_4);
    }

    public Set<User> fetchAll(){
        return new HashSet<>(create.select().from(USERS).fetch().into(User.class));
    }

    @Override
    public Optional<User> fetchById(Long id){
        return Optional.ofNullable(create.select().from(USERS).where(USERS.ID.equal(id)).fetchOneInto(User.class));
    }

    @Override
    public Optional<User> fetchByMail(String mail) {
        return Optional.ofNullable(create.select().from(USERS).where(USERS.EMAIL.equal(mail)).fetchOneInto(User.class));
    }

    @Override
    public Optional<User> fetchByUsername(String username) {
        return Optional.ofNullable(create.select().from(USERS).where(USERS.USERNAME.equal(username)).fetchOneInto(User.class));
    }

    @Override
    public Set<User> fetchByUsernameOrEmail(String username, String email) {
        return new HashSet<>(create.
                select().from(USERS)
                .where(
                        USERS.USERNAME.equal(username).or(USERS.EMAIL.equal(email))
                )
                .fetch().into(User.class)
        );
    }

    @Override
    public User save(User user) {
        return create.insertInto(USERS, USERS.USERNAME, USERS.EMAIL, USERS.PASSWORD)
                .values(user.getUsername(), user.getMailAddress(), user.getPassword())
                .returning(USERS.ID)
                .fetchOne().into(User.class);
    }
}

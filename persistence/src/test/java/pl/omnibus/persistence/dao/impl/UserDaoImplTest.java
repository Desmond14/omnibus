package pl.omnibus.persistence.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.omnibus.domain.User;
import pl.omnibus.persistence.dao.DaoTestBase;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DataSourceAutoConfiguration.class)
public class UserDaoImplTest extends DaoTestBase{
    @Autowired
    private DataSource dataSource;
    private UserDaoImpl userDao;

    @Before
    public void setUp() throws Exception {
        userDao = new UserDaoImpl(dataSource);
        prepareDatabase();
    }

    @Test
    public void shouldFetchAllUsers() {
        Set<User> users = userDao.fetchAll();
        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(equalTo(2)));
    }

    @Test
    public void shouldFetchById() {
        Optional<User> user = userDao.fetchById(1L);
        assertThat(user.isPresent(), is(true));
    }

    @Test
    public void shouldNotFetchById() {
        Optional<User> user = userDao.fetchById(2L);
        assertThat(user.isPresent(), is(false));
    }

    @Test
    public void shouldFetchId() {
        Optional<User> user = userDao.fetchById(1L);
        assertThat(user.isPresent(), is(true));
        assertThat(user.get().getId(), is(equalTo(1L)));
    }

    @Test
    public void shouldFetchUsername() {
        Optional<User> user = userDao.fetchById(1L);
        assertThat(user.isPresent(), is(true));
        assertThat(user.get().getUsername(), is(equalTo("user1")));
    }

    @Test
    public void shouldFetchPassword() {
        Optional<User> user = userDao.fetchById(1L);
        assertThat(user.isPresent(), is(true));
        assertThat(user.get().getPassword(), is(equalTo("password")));
    }

    @Test
    public void shouldFetchMailAddress() {
        Optional<User> user = userDao.fetchById(1L);
        assertThat(user.isPresent(), is(true));
        assertThat(user.get().getMailAddress(), is(equalTo("user1@wp.pl")));
    }

    @Test
    public void shouldFetchByEmail() {
        Optional<User> user = userDao.fetchByMail("user1@wp.pl");
        assertThat(user.isPresent(), is(true));
    }

    @Test
    public void shouldNotFetchByEmail() {
        Optional<User> user = userDao.fetchByMail("user2@wp.pl");
        assertThat(user.isPresent(), is(false));
    }

    @Test
    public void shouldFetchByUsername() {
        Optional<User> user = userDao.fetchByUsername("user3");
        assertThat(user.isPresent(), is(true));
    }

    @Test
    public void shouldNotFetchByUsername() {
        Optional<User> user = userDao.fetchByUsername("user2");
        assertThat(user.isPresent(), is(false));
    }

    @Test
    public void shouldFetchOneByUsernameAndOneByEmail() {
        Set<User> users = userDao.fetchByUsernameOrEmail("user1", "user3@wp.pl");
        assertThat(users.size(), is(equalTo(2)));
    }

    @Test
    public void shouldFetchOnlyByUsername() {
        Set<User> users = userDao.fetchByUsernameOrEmail("user1", "user2@wp.pl");
        assertThat(users.size(), is(equalTo(1)));
        assertThat(users.iterator().next().getUsername(), is(equalTo("user1")));
    }

    @Test
    public void shouldFetchOnlyByEmail() {
        Set<User> users = userDao.fetchByUsernameOrEmail("user2", "user3@wp.pl");
        assertThat(users.size(), is(equalTo(1)));
        assertThat(users.iterator().next().getMailAddress(), is(equalTo("user3@wp.pl")));
    }

    @Test
    public void shouldReturnEmptySetWhenConditionNotMatched() {
        Set<User> users = userDao.fetchByUsernameOrEmail("user2", "user2@wp.pl");
        assertThat(users, is(notNullValue()));
        assertThat(users.isEmpty(), is(true));
    }

    @Test
    public void shouldSaveNewUserAndReturnId() {
        User savedUser = userDao.save(new User("user4", "password", "user4@onet.pl"));
        assertThat(savedUser, is(notNullValue()));
        assertThat(savedUser.getId(), is(notNullValue()));
    }

}

package pl.omnibus.persistence.dao.impl;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.omnibus.domain.User;
import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@ContextConfiguration(locations = "/daos.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoImplTest {
    private static final String JDBC_DRIVER = org.postgresql.Driver.class.getName();
    private static final String JDBC_URL = "jdbc:postgresql:omnibus";
    private static final String USER = "omnibus";
    private static final String PASSWORD = "omnibus123";
    private static final String DATASET_FILE = "/dataset.xml";

    @Resource(name = "userDao")
    private UserDaoImpl userDao;

    @Before
    public void setUp() throws Exception {
        cleanlyInsert(readDataSet());
    }

    private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(
                getClass().getResourceAsStream(DATASET_FILE)
        );
    }

    private void cleanlyInsert(IDataSet dataSet) throws Exception {
        IDatabaseTester databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
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
}
package pl.omnibus.service.registration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.omnibus.domain.User;
import pl.omnibus.persistence.dao.UserDao;
import java.util.Collections;
import java.util.HashSet;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceImplTest {
    private RegistrationService registrationService;
    @Mock
    private UserDao mockedUserDao;
    @Mock
    private User savedUser;

    @Before
    public void setUp() {
        registrationService = new RegistrationServiceImpl(mockedUserDao);
    }

    @Test
    public void shouldPassValidation() {
        when(mockedUserDao.fetchByUsernameOrEmail("user1", "user1@wp.pl")).thenReturn(Collections.<User>emptySet());
        when(mockedUserDao.save(any(User.class))).thenReturn(savedUser);
        RegistrationResult result = registrationService.register("user1", "user1@wp.pl", "password");
        assertThat(result.getValidationResult(), is(equalTo(ValidationResult.PASSED)));
    }

    @Test
    public void shouldReturnSavedObject() {
        when(mockedUserDao.fetchByUsernameOrEmail("user1", "user1@wp.pl")).thenReturn(Collections.<User>emptySet());
        when(mockedUserDao.save(any(User.class))).thenReturn(savedUser);
        RegistrationResult result = registrationService.register("user1", "user1@wp.pl", "password");
        assertThat(result.getRegisteredUser().isPresent(), is(true));
        assertThat(result.getRegisteredUser().get(), is(equalTo(savedUser)));
    }

    @Test
    public void shouldNotPassValidationDueToIncorrectEmail() {
        RegistrationResult result = registrationService.register("user1", "not_an_email.pl", "password");
        assertThat(result.getValidationResult(), is(equalTo(ValidationResult.INCORRECT_EMAIL)));
    }

    @Test
    public void shouldNotReturnUserWhenEmailValidationNotPassed() {
        RegistrationResult result = registrationService.register("user1", "not_an_email.pl", "password");
        assertThat(result.getRegisteredUser().isPresent(), is(false));
    }

    @Test
    public void shouldNotPassValidationDueToNotUniqueEmail() {
        when(mockedUserDao.fetchByUsernameOrEmail("user1", "user1@wp.pl")).thenReturn(new HashSet<>(singletonList(
                new User("other_username", "user1@wp.pl", "password")
        )));
        RegistrationResult result = registrationService.register("user1", "user1@wp.pl", "password");
        assertThat(result.getValidationResult(), is(equalTo(ValidationResult.EMAIL_ALREADY_IN_USE)));
    }

    @Test
    public void shouldNotReturnUserWhenValidationNotPassedDueToNotUniqueEmail() {
        when(mockedUserDao.fetchByUsernameOrEmail("user1", "user1@wp.pl")).thenReturn(new HashSet<>(singletonList(
                new User("other_username", "user1@wp.pl", "password")
        )));
        RegistrationResult result = registrationService.register("user1", "user1@wp.pl", "password");
        assertThat(result.getRegisteredUser().isPresent(), is(false));
    }

    @Test
    public void shouldNotPassValidationDueToNotUniqueUsername() {
        when(mockedUserDao.fetchByUsernameOrEmail("user1", "user1@wp.pl")).thenReturn(new HashSet<>(singletonList(
                new User("user1", "other_email@wp.pl", "password")
        )));
        RegistrationResult result = registrationService.register("user1", "user1@wp.pl", "password");
        assertThat(result.getValidationResult(), is(equalTo(ValidationResult.USERNAME_ALREADY_IN_USE)));
    }

    @Test
    public void shouldNotReturnUserWhenValidationNotPassedDueToNotUniqueUsername() {
        when(mockedUserDao.fetchByUsernameOrEmail("user1", "user1@wp.pl")).thenReturn(new HashSet<>(singletonList(
                new User("user1", "other_email@wp.pl", "password")
        )));
        RegistrationResult result = registrationService.register("user1", "user1@wp.pl", "password");
        assertThat(result.getRegisteredUser().isPresent(), is(false));
    }

    @Test
    public void shouldReturnUsernameAlreadyInUseOrEmailAlreadyInUseWhenBothPresentInDatabase() {
        when(mockedUserDao.fetchByUsernameOrEmail("user1", "user1@wp.pl")).thenReturn(new HashSet<>(asList(
                new User("user1", "other_email@wp.pl", "password"),
                new User("other_username", "user1@wp.pl", "password")
        )));
        RegistrationResult result = registrationService.register("user1", "user1@wp.pl", "password");
        assertThat(result.getValidationResult(), is(anyOf(
                equalTo(ValidationResult.EMAIL_ALREADY_IN_USE),
                equalTo(ValidationResult.USERNAME_ALREADY_IN_USE)
        )));
    }
}
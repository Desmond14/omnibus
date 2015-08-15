package pl.omnibus.service.registration;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.omnibus.domain.User;
import pl.omnibus.persistence.dao.UserDao;
import java.util.Set;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final UserDao userDao;

    @Autowired
    public RegistrationServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public RegistrationResult register(String username, String email, String password) {
        if (!emailValidator.isValid(email)) {
            return new RegistrationResult(ValidationResult.INCORRECT_EMAIL, null);
        }
        Set<User> users = userDao.fetchByUsernameOrEmail(username, email);
        if (users.isEmpty()) {
            return new RegistrationResult(ValidationResult.PASSED, userDao.save(new User(username, email, password)));
        } else {
            return new RegistrationResult(determineValidationResult(users, email), null);
        }
    }

    private ValidationResult determineValidationResult(Set<User> users, String email) {
        if (emailAlreadyInUse(users, email)) {
            return ValidationResult.EMAIL_ALREADY_IN_USE;
        } else {
            return ValidationResult.USERNAME_ALREADY_IN_USE;
        }
    }

    private boolean emailAlreadyInUse(Set<User> users, String email) {
        for (User user : users) {
            if (user.getMailAddress().equals(email)) {
                return true;
            }
        }
        return false;
    }

}

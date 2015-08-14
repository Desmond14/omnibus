package pl.omnibus.service.registration;

import pl.omnibus.domain.User;
import java.util.Optional;

public class RegistrationResult {
    private final ValidationResult validationResult;
    private final Optional<User> registeredUser;

    public RegistrationResult(ValidationResult validationResult, User registeredUser) {
        this.validationResult = validationResult;
        this.registeredUser = Optional.ofNullable(registeredUser);
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public Optional<User> getRegisteredUser() {
        return registeredUser;
    }

}

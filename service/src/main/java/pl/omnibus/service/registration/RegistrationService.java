package pl.omnibus.service.registration;

public interface RegistrationService {
    RegistrationResult register(String username, String email, String password);
}

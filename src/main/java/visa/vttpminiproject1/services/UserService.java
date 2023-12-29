package visa.vttpminiproject1.services;

import static visa.vttpminiproject1.Utils.ATTR_PASSWORD;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import visa.vttpminiproject1.models.Email;
import visa.vttpminiproject1.models.User;
import visa.vttpminiproject1.repos.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailSvc;

    @Value("${app.url}")
    String appURL;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Boolean usernameAvailable(String username) {
        if (userRepo.getUserData(username).isEmpty()) {
            return true;
        }
        return false;
    }

    public void createUser(User user) {
        String apikey = UUID.randomUUID().toString();
        String verificationString = "%s_%s".formatted(user.getUsername(), UUID.randomUUID().toString());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.createUser(user, apikey, verificationString);
        Email email = new Email();
        String verificationURL = "%suser/%s".formatted(appURL, verificationString);
        email.setRecipient(user.getEmail());
        email.setMessage(
                "Hello %s!\n\nThank you for using StockSentry! Please use the URL below to perfom your email verification and first log in!\n%s \nBest Regards,\nStockSentry Team"
                        .formatted(user.getUsername(), verificationURL));
        email.setSubject("Welcome To StockSentry");
        emailSvc.sendSimpleMail(email);
    }

    public Boolean authenticateUser(String username, String password) {
        Boolean authenticated = false;
        Optional<Map<String, String>> opt = userRepo.getUserData(username);
        if (!opt.isEmpty()) {
            Map<String, String> user = opt.get();
            authenticated = encoder.matches(password, user.get(ATTR_PASSWORD));
        }
        return authenticated;
    }

    public Integer verifyUser(String user, String verification) {
        Optional<String> opt = userRepo.getEmailVerificationString(user);
        if (opt.isEmpty()) {
            return 0;
        } else {
            String code = opt.get();
            if (code.equals(verification)) {
                userRepo.deleteVerification(user);
                return 0;
            }
            return 1;
        }
    }

    public Boolean userVerified(String user) {
        Boolean verified = false;
        Optional<String> opt = userRepo.getEmailVerificationString(user);
        if (opt.isEmpty()) {
            verified = true;
        }
        return verified;
    }
}

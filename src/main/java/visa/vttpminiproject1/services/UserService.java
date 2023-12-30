package visa.vttpminiproject1.services;

import static visa.vttpminiproject1.Utils.ATTR_PASSWORD;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.text.RandomStringGenerator;
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
    private RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('a', 'z').build();

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
                "Hello %s!\n\nThank you for using StockSentry! Please use the URL below to perfom your email verification and first log in!\n%s\n\nBest Regards,\nStockSentry Team"
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

    public String resetPassword(String email) {
        Optional<String> opt = userRepo.getUserUsingEmail(email);
        if (opt.isEmpty()) {
            return "Email not registered!";
        }
        String user = opt.get();
        String randomPassword = generator.generate(10);
        userRepo.setPassword(user, encoder.encode(randomPassword));
        Email resetEmail = new Email();
        resetEmail.setRecipient(email);
        resetEmail.setMessage(
                "Hello %s!\n\nPlease find below your new password. Do change your password once you have logged in!\nPassword: %s\n\nBest Regards,\nStockSentry Team"
                        .formatted(user, randomPassword));
        resetEmail.setSubject("Reset Password");
        emailSvc.sendSimpleMail(resetEmail);
        return "Email on password reset sent!";
    }

    public User getUserDetails(String user) {
        User userDetails = new User();
        userDetails.setUsername(user);
        userDetails.setEmail(userRepo.getEmail(user));
        return userDetails;
    }

    public void changeEmail(String user, String email) {
        userRepo.setEmail(user, email);
    }

    public String changePassword(String user, String oldPassword, String newPassword) {
        if (authenticateUser(user, oldPassword)) {
            userRepo.setPassword(user, encoder.encode(newPassword));
            return "Password changed successfully!";
        }
        return "Current password entered is incorrect!";
    }
}

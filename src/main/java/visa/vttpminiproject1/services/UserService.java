package visa.vttpminiproject1.services;

import static visa.vttpminiproject1.Utils.ATTR_PASSWORD;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import visa.vttpminiproject1.repos.UserRepo;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Boolean usernameAvailable(String username){
        if (userRepo.getUserData(username).isEmpty()){
            return true;
        }
        return false;
    }

    public void createUser(String username, String password){
        password = encoder.encode(password);
        userRepo.createUser(username, password);
    }

    public Boolean authenticateUser(String username, String password){
        Boolean authenticated = false;
        Optional<Map<String, String>> opt = userRepo.getUserData(username);
        if (!opt.isEmpty()){
            Map<String, String> user = opt.get();
            authenticated = encoder.matches(password, user.get(ATTR_PASSWORD));
        }
        return authenticated;
    }
}

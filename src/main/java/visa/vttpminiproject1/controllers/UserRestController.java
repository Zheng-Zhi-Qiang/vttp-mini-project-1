package visa.vttpminiproject1.controllers;

import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;
import visa.vttpminiproject1.Utils;
import visa.vttpminiproject1.services.UserService;

@RestController
@RequestMapping(path = "/user")
public class UserRestController {
    @Autowired
    private UserService userSvc;

    @PostMapping(path = "/username", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkUsernameAvaliability(@RequestBody String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String resp;
        try {
            if (userSvc.usernameAvailable(data.getString("username"))) {
                resp = Json.createObjectBuilder()
                        .add("result", "Username is available!")
                        .build().toString();
            } else {
                resp = Json.createObjectBuilder()
                        .add("result", "Username has been taken!")
                        .build().toString();
            }

            return ResponseEntity.status(200).body(resp);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("No data");
        }
    }

    @PostMapping(path = "/reset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> resetPassword(@RequestBody String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        try {
            String email = data.getString("email");
            String result = userSvc.resetPassword(email);
            String resp = Json.createObjectBuilder()
                    .add("result", result)
                    .build().toString();
            return ResponseEntity.status(200).body(resp);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("No data");
        }
    }

    @PostMapping(path = "/changeEmail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeEmail(@RequestBody String payload, HttpSession session) {
        Optional<ResponseEntity<String>> authentication = Utils.authenticatedForREST(session);
        if (!authentication.isEmpty()) {
            return authentication.get();
        }
        String user = (String) session.getAttribute("user");
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String email = data.getString("email");
        userSvc.changeEmail(user, email);
        return ResponseEntity.status(200).body("Success");
    }

    @PostMapping(path = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@RequestBody String payload, HttpSession session) {
        Optional<ResponseEntity<String>> authentication = Utils.authenticatedForREST(session);
        if (!authentication.isEmpty()) {
            return authentication.get();
        }
        String user = (String) session.getAttribute("user");
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String oldPassword = data.getString("oldpw");
        String newPassword = data.getString("newpw");
        String result = userSvc.changePassword(user, oldPassword, newPassword);
        String resp = Json.createObjectBuilder()
                .add("result", result)
                .build().toString();
        return ResponseEntity.status(200).body(resp);
    }
}

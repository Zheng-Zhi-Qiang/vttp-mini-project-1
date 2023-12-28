package visa.vttpminiproject1.controllers;

import java.io.StringReader;
import java.util.List;
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
import visa.vttpminiproject1.services.WatchListService;

@RestController
@RequestMapping(path = "/watchlist")
public class WatchListRestController {

    @Autowired
    private WatchListService watchlistSvc;

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> add(@RequestBody String payload, HttpSession session) {
        Optional<ResponseEntity<String>> authentication = Utils.authenticatedForREST(session);
        if (!authentication.isEmpty()) {
            return authentication.get();
        }
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String ticker = data.getString("ticker");
        String user = (String) session.getAttribute("user");
        List<String> watchlist = watchlistSvc.addToWatchList(user, ticker);
        JsonObject resp = Json.createObjectBuilder()
                .add("watchlist", watchlist.toString())
                .build();
        return ResponseEntity.status(200).body(resp.toString());
    }

    @PostMapping(path = "/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> remove(@RequestBody String payload, HttpSession session) {
        Optional<ResponseEntity<String>> authentication = Utils.authenticatedForREST(session);
        if (!authentication.isEmpty()) {
            return authentication.get();
        }
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String ticker = data.getString("ticker");
        String user = (String) session.getAttribute("user");
        List<String> watchlist = watchlistSvc.removeFromWatchList(user, ticker);
        JsonObject resp = Json.createObjectBuilder()
                .add("watchlist", watchlist.toString())
                .build();
        return ResponseEntity.status(200).body(resp.toString());
    }
}
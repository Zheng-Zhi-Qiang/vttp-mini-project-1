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
import visa.vttpminiproject1.models.News;
import visa.vttpminiproject1.services.StockNewsService;

@RestController
@RequestMapping(path = "/news")
public class NewsRestController {

    @Autowired
    private StockNewsService newsSvc;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getNews(@RequestBody String payload, HttpSession session) {
        Optional<ResponseEntity<String>> authentication = Utils.authenticatedForREST(session);
        if (!authentication.isEmpty()) {
            return authentication.get();
        }
        String user = (String) session.getAttribute("user");
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String newsType = data.getString("type");
        List<News> news = null;
        if (newsType.equals("General")) {
            news = newsSvc.getTickerNews("latest");
        } else if (newsType.equals("Markets")) {
            news = newsSvc.getTickerNews("markets");
        } else if (newsType.equals("Economy")) {
            news = newsSvc.getTickerNews("economy");
        } else if (newsType.equals("Related")) {
            news = newsSvc.getRelatedNews(user);
        }
        List<String> newsString = news.stream()
                .map(article -> News.toJsonString(article))
                .toList();

        return ResponseEntity.status(200).body(newsString.toString());
    }
}

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
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;
import visa.vttpminiproject1.Utils;
import visa.vttpminiproject1.models.News;
import visa.vttpminiproject1.models.StockData;
import visa.vttpminiproject1.services.PortfolioService;
import visa.vttpminiproject1.services.StockNewsService;

@RestController
@RequestMapping(path = "/data")
public class StockDataRestController {

    @Autowired
    private StockNewsService newsSvc;

    @Autowired
    private PortfolioService portfolioSvc;

    @PostMapping(path = "/news", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTickerNews(@RequestBody String payload, HttpSession session) {
        Optional<ResponseEntity<String>> authentication = Utils.authenticatedForREST(session);
        if (!authentication.isEmpty()) {
            return authentication.get();
        }
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String ticker = data.getString("ticker").toUpperCase();
        List<News> news = newsSvc.getTickerNews(ticker);
        List<String> newsString = news.stream()
                .map(article -> {
                    String json = Json.createObjectBuilder()
                            .add("title", article.getTitle())
                            .add("url", article.getUrl())
                            .add("sentiment", article.getSentiment())
                            .build().toString();
                    return json;
                })
                .toList();
        return ResponseEntity.status(200).body(newsString.toString());
    }

    @PostMapping(path = "/financials", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTickerFinancials(@RequestBody String payload, HttpSession session) {
        Optional<ResponseEntity<String>> authentication = Utils.authenticatedForREST(session);
        if (!authentication.isEmpty()) {
            return authentication.get();
        }
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String ticker = data.getString("ticker").toUpperCase();
        StockData stockData = portfolioSvc.getStockData(ticker).get();
        return ResponseEntity.status(200).body(StockData.toJsonString(stockData));
    }

    @PostMapping(path = "/earnings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTickerEarnings(@RequestBody String payload, HttpSession session) {
        Optional<ResponseEntity<String>> authentication = Utils.authenticatedForREST(session);
        if (!authentication.isEmpty()) {
            return authentication.get();
        }
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String ticker = data.getString("ticker").toUpperCase();
        JsonArray allEarnings = portfolioSvc.getStockEarnings(ticker);
        String resp;
        if (allEarnings.isEmpty()) {
            resp = Json.createObjectBuilder()
                    .add("result", "No available earnings data")
                    .build().toString();
        } else {
            if (allEarnings.size() < 8) {
                resp = Json.createObjectBuilder()
                        .add("result", "success")
                        .add("data", allEarnings)
                        .build().toString();
            } else {
                JsonArrayBuilder earnings = Json.createArrayBuilder();
                for (int i = 0; i < 8; i++) {
                    earnings.add(allEarnings.get(i));
                }
                resp = Json.createObjectBuilder()
                        .add("result", "success")
                        .add("data", earnings.build())
                        .build().toString();
            }
        }
        return ResponseEntity.status(200).body(resp);
    }
}

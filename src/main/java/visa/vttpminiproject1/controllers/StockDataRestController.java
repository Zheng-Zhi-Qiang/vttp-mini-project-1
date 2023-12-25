package visa.vttpminiproject1.controllers;

import java.io.StringReader;
import java.util.List;
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
    public ResponseEntity<String> getTickerNews(@RequestBody String payload){
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
    public ResponseEntity<String> getTickerFinancials(@RequestBody String payload){
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        String ticker = data.getString("ticker").toUpperCase();
        StockData stockData = portfolioSvc.getStockData(ticker).get();
        return ResponseEntity.status(200).body(StockData.toJsonString(stockData));
    }
}

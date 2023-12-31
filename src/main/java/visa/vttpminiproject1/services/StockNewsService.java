package visa.vttpminiproject1.services;

import java.io.StringReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import visa.vttpminiproject1.models.News;
import visa.vttpminiproject1.models.Position;
import visa.vttpminiproject1.repos.StockNewsRepo;

import static visa.vttpminiproject1.Utils.*;

@Service
public class StockNewsService {
    @Autowired
    private StockNewsRepo newsRepo;

    @Autowired
    private WatchListService watchlistService;

    @Autowired
    private PortfolioService portfolioService;

    @Value("${stock.api.key}")
    String stockNewsApiKey;

    private RestTemplate template = new RestTemplate();

    public List<News> getTickerNews(String ticker) {

        Optional<String> opt = newsRepo.getTickerNews(ticker);
        String data;

        if (opt.isEmpty()) {
            String url;
            if (ticker.equals("latest")) {
                url = UriComponentsBuilder.fromUriString(QUERY_RESOURCE)
                        .queryParam("function", FUNCTION_NEWS)
                        .queryParam("sort", "LATEST")
                        .queryParam("apikey", stockNewsApiKey)
                        .build()
                        .toUriString();
            } else if (ticker.equals("markets")) {
                url = UriComponentsBuilder.fromUriString(QUERY_RESOURCE)
                        .queryParam("function", FUNCTION_NEWS)
                        .queryParam("topics", ATTR_MARKETNEWS)
                        .queryParam("sort", "LATEST")
                        .queryParam("apikey", stockNewsApiKey)
                        .build()
                        .toUriString();
            } else if (ticker.equals("economy")) {
                url = UriComponentsBuilder.fromUriString(QUERY_RESOURCE)
                        .queryParam("function", FUNCTION_NEWS)
                        .queryParam("topics", ATTR_ECONOMY)
                        .queryParam("sort", "LATEST")
                        .queryParam("apikey", stockNewsApiKey)
                        .build()
                        .toUriString();
            } else {
                url = UriComponentsBuilder.fromUriString(QUERY_RESOURCE)
                        .queryParam("function", FUNCTION_NEWS)
                        .queryParam("tickers", ticker)
                        .queryParam("sort", "LATEST")
                        .queryParam("apikey", stockNewsApiKey)
                        .build()
                        .toUriString();
            }
            RequestEntity<Void> req = RequestEntity.get(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .build();

            try {
                ResponseEntity<String> resp = template.exchange(req, String.class);
                data = resp.getBody();
                newsRepo.cacheTickerNews(ticker, data);
            } catch (Exception e) {
                e.printStackTrace();
                return new LinkedList<>();
            }
        } else {
            data = opt.get();
        }

        JsonReader reader = Json.createReader(new StringReader(data));
        JsonArray feed = reader.readObject().getJsonArray(ATTR_FEED);
        List<News> news = feed.stream()
                .map(jsonValue -> News.toNews(jsonValue.asJsonObject()))
                .collect(Collectors.toCollection(LinkedList::new));

        return news;
    }

    public List<News> getRelatedNews(String user) {
        List<String> watchlist = watchlistService.getWatchList(user);
        List<Position> portfolio = portfolioService.getPortfolio(user).getPositions();
        Set<String> relatedTickers = Stream
                .concat(watchlist.stream(), portfolio.stream().map(position -> position.getTicker()))
                .collect(Collectors.toCollection(HashSet::new));
        if (relatedTickers.isEmpty()) {
            return getTickerNews("markets");
        } else {
            List<News> watchlistNews = new LinkedList<>();
            for (String ticker : relatedTickers) {
                watchlistNews = Stream.concat(watchlistNews.stream(), getTickerNews(ticker).stream())
                        .collect(Collectors.toCollection(LinkedList::new));
            }
            Collections.sort(watchlistNews, Comparator.comparing(News::getDatetime));
            return watchlistNews;
        }
    }
}

package visa.vttpminiproject1.services;

import static visa.vttpminiproject1.Utils.*;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import visa.vttpminiproject1.models.Portfolio;
import visa.vttpminiproject1.models.Position;
import visa.vttpminiproject1.models.StockData;
import visa.vttpminiproject1.repos.StockDataRepo;
import visa.vttpminiproject1.repos.UserRepo;

@Service
public class PortfolioService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StockDataRepo dataRepo;

    @Value("${stock.api.key}")
    private String apiKey;

    private RestTemplate template = new RestTemplate();

    public Integer addPosition(String userId, Position position){
        Optional<List<Position>> opt = userRepo.getPortfolio(userId);

        Optional<Position> positionValid = validatePosition(position);
        if (!positionValid.isEmpty()){
            List<Position> portfolio;
            if (!opt.isEmpty()){
                portfolio = opt.get();
                List<Position> existingPosition = portfolio.stream()
                                                    .filter(holding -> holding.getTicker().equals(position.getTicker()))
                                                    .toList();
                
                if (existingPosition.size() <= 0){
                    portfolio.add(position);
                }
                else {
                    Position currPosition = existingPosition.get(0);
                    addNewToExistingPosition(currPosition, position);
                }
            }
            else {
                portfolio = new LinkedList<>();
                portfolio.add(position);
            }
    
            userRepo.savePortfolio(userId, portfolio);
            return 0;
        }
        else {
            return 1;
        }
    }

    public Portfolio getPortfolio(String userId){
        Portfolio portfolio = new Portfolio();
        Optional<List<Position>> opt = userRepo.getPortfolio(userId);
        if (opt.isEmpty()){
            portfolio.setPositions(new LinkedList<>());
        }
        else {
            List<Position> data = opt.get();
            List<Position> positions = data.stream()
                                        .map(position -> {
                                            position.setLastTradedPrice(getLastTradedPrice(position.getTicker()));
                                            position.calculateData();
                                            return position;
                                        })
                                        .toList();
            portfolio.setPositions(positions);
        }

        return portfolio;
    }

    public Optional<StockData> getStockData(String ticker){
        Optional<StockData> opt = dataRepo.getData(ticker);
        if (opt.isEmpty()){
            String url = UriComponentsBuilder.fromUriString(QUERY_RESOURCE)
                            .queryParam(API_FUNCTION, FUNCTION_OVERVIEW)
                            .queryParam(ATTR_SYMBOL, ticker)
                            .queryParam(API_KEY, apiKey)
                            .build()
                            .toUriString();
    
            RequestEntity<Void> req = RequestEntity.get(url)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .build();
            ResponseEntity<String> resp = template.exchange(req, String.class);
    
            JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
            JsonObject respData = reader.readObject();
    
            if (respData.isEmpty()){
                return Optional.empty();
            }
            else {
                dataRepo.cacheData(ticker, respData.toString());
                return Optional.of(StockData.toStockData(respData));
            }
        }
        else {
            return opt;
        }
    }

    private void addNewToExistingPosition(Position currPosition, Position newPosition){
        Double newCostBasis = (Double.parseDouble(currPosition.getCostBasis()) * currPosition.getQuantityPurchased() + 
                                        Double.parseDouble(newPosition.getCostBasis()) * newPosition.getQuantityPurchased()) / (currPosition.getQuantityPurchased() + newPosition.getQuantityPurchased());
        Integer newTotalQuantityPurchased = currPosition.getQuantityPurchased() + newPosition.getQuantityPurchased();
        currPosition.setCostBasis(newCostBasis.toString());
        currPosition.setQuantityPurchased(newTotalQuantityPurchased);
    }

    private Optional<Position> validatePosition(Position position){
        String ticker = position.getTicker();
        Optional<StockData> opt = getStockData(ticker);

        if (opt.isEmpty()){
            return Optional.empty();
        }
        else {
            position.setCompanyName(opt.get().getName());
            return Optional.of(position);
        }
    }

    private Double getLastTradedPrice(String ticker){
        String url = UriComponentsBuilder.fromUriString(QUERY_RESOURCE)
                        .queryParam(API_FUNCTION, FUNCTION_QUOTE)
                        .queryParam(ATTR_SYMBOL, ticker)
                        .queryParam(API_KEY, apiKey)
                        .build().toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .build();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
        Double lastTradedPrice = Double.parseDouble(reader.readObject()
                                                        .getJsonObject(ATTR_GLOBALQUOTE).getString(ATTR_LASTTRADED));

        return lastTradedPrice;
    }
}

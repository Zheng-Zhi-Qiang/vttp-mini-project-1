package visa.vttpminiproject1.repos;

import static visa.vttpminiproject1.Utils.BEAN_TICKERREDIS;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import visa.vttpminiproject1.models.StockData;

@Repository
public class StockDataRepo {

    @Autowired
    @Qualifier(BEAN_TICKERREDIS)
    private RedisTemplate<String, String> template;

    private Long cacheDuration = 3L;

    public void cacheData(String ticker, String data) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        valueOps.set(ticker, data, Duration.ofHours(cacheDuration));
    }

    public Optional<StockData> getData(String ticker) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        String data = valueOps.get(ticker);
        if (null == data) {
            return Optional.empty();
        }
        return Optional.of(StockData.toStockData(data));
    }

    public void cacheQuoteData(String ticker, String data) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        valueOps.set("%s-quote".formatted(ticker), data, Duration.ofHours(cacheDuration));
    }

    public Optional<String> getQuoteData(String ticker) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        String data = valueOps.get("%s-quote".formatted(ticker));
        return Optional.ofNullable(data);
    }

    public void cacheEarningsData(String ticker, String data) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        valueOps.set("%s-earnings".formatted(ticker), data, Duration.ofHours(cacheDuration));
    }

    public Optional<String> getEarningsData(String ticker) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        String data = valueOps.get("%s-earnings".formatted(ticker));
        return Optional.ofNullable(data);
    }
}

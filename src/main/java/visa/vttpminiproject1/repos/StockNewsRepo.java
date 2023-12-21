package visa.vttpminiproject1.repos;

import java.time.Duration;
import java.util.Optional;
import static visa.vttpminiproject1.Utils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class StockNewsRepo {
    
    @Autowired @Qualifier(BEAN_NEWSREDIS)
    private RedisTemplate<String, String> template;

    @Value("${cache.duration}")
    private Long cacheDuration;

    public void cacheTickerNews(String ticker, String news){
        ValueOperations<String, String> valueOps = template.opsForValue();
        valueOps.append(ticker, news);
        // valueOps.set(ticker, news, Duration.ofSeconds(cacheDuration));
    }

    public Optional<String> getTickerNews(String ticker){
        ValueOperations<String, String> valueOps = template.opsForValue();
        String news = valueOps.get(ticker);
        return Optional.ofNullable(news);
    }
}

package visa.vttpminiproject1.repos;

import java.time.Duration;
import java.util.Optional;
import static visa.vttpminiproject1.Utils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class StockNewsRepo {

    @Autowired
    @Qualifier(BEAN_NEWSREDIS)
    private RedisTemplate<String, String> template;

    public void cacheTickerNews(String ticker, String news) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        valueOps.set(ticker, news, Duration.ofSeconds(20));
    }

    public Optional<String> getTickerNews(String ticker) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        String news = valueOps.get(ticker);
        return Optional.ofNullable(news);
    }
}

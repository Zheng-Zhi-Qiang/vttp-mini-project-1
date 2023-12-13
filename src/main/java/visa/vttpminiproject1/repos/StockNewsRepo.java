package visa.vttpminiproject1.repos;

import java.util.Optional;
import static visa.vttpminiproject1.Utils.BEAN_REDIS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class StockNewsRepo {
    
    @Autowired @Qualifier(BEAN_REDIS)
    private RedisTemplate<String, String> template;

    public void cacheTickerNews(String ticker, String news){
        ValueOperations<String, String> valueOps = template.opsForValue();
        valueOps.append(ticker, news);
    }

    public Optional<String> getTickerNews(String ticker){
        ValueOperations<String, String> valueOps = template.opsForValue();
        String news = valueOps.get(ticker);
        return Optional.ofNullable(news);
    }
}

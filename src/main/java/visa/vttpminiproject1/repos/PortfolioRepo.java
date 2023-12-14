package visa.vttpminiproject1.repos;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import static visa.vttpminiproject1.Utils.BEAN_REDIS;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import visa.vttpminiproject1.models.Position;

@Repository
public class PortfolioRepo {
    @Autowired @Qualifier(BEAN_REDIS)
    private RedisTemplate<String, String> template;
    
    public Optional<List<Position>> getPortfolio(String userId){
        HashOperations<String, String, String> hashOps = template.opsForHash();
        String data = hashOps.get(userId, "portfolio");
        if (data == null){
            return Optional.empty();
        }

        JsonReader reader = Json.createReader(new StringReader(data));
        List<Position> positions = reader.readArray().stream()
                                        .map(jsonValue -> Position.toPosition(jsonValue.asJsonObject()))
                                        .toList();

        return Optional.of(positions);
    }

    public void savePortfolio(String userId, List<Position> positions){
        HashOperations<String, String, String> hashOps = template.opsForHash();
        if (hashOps.get(userId, "portfolio") != null){
            hashOps.delete(userId, "portfolio");
        }
        List<String> positionsString = positions.stream()
                                        .map(position -> Position.toJsonString(position))
                                        .toList();

        hashOps.put(userId, "portfolio", positionsString.toString());
    }
}

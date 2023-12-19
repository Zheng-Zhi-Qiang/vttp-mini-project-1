package visa.vttpminiproject1.repos;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import static visa.vttpminiproject1.Utils.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import visa.vttpminiproject1.models.Position;

@Repository
public class UserRepo {
    @Autowired @Qualifier(BEAN_USERSREDIS)
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
                                        .collect(Collectors.toCollection(LinkedList::new));

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

    public Optional<Map<String,String>> getUserData(String username){
        HashOperations<String, String, String> hashOps = template.opsForHash();
        Map<String, String> data = hashOps.entries(username);
        if (data.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(data);
    }

    public void createUser(String username, String password){
        HashOperations<String, String, String> hashOps = template.opsForHash();
        hashOps.put(username, ATTR_USERNAME, username);
        hashOps.put(username, ATTR_PASSWORD, password);
    }


}

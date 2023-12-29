package visa.vttpminiproject1.repos;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import visa.vttpminiproject1.models.User;

@Repository
public class UserRepo {
    @Autowired
    @Qualifier(BEAN_USERREDIS)
    private RedisTemplate<String, String> template;

    public Optional<List<Position>> getPortfolio(String userId) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        String data = hashOps.get(userId, "portfolio");
        if (data == null) {
            return Optional.empty();
        }
        JsonReader reader = Json.createReader(new StringReader(data));
        List<Position> positions = reader.readArray().stream()
                .map(jsonValue -> Position.toPosition(jsonValue.asJsonObject()))
                .collect(Collectors.toCollection(LinkedList::new));

        return Optional.of(positions);
    }

    public void savePortfolio(String userId, List<Position> positions) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        if (hashOps.get(userId, "portfolio") != null) {
            hashOps.delete(userId, "portfolio");
        }
        List<String> positionsString = positions.stream()
                .map(position -> Position.toJsonString(position))
                .toList();

        hashOps.put(userId, "portfolio", positionsString.toString());
    }

    public Optional<Map<String, String>> getUserData(String username) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        Map<String, String> data = hashOps.entries(username);
        if (data.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(data);
    }

    public void createUser(User user, String apikey, String verification) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        hashOps.put(user.getUsername(), ATTR_USERNAME, user.getUsername());
        hashOps.put(user.getUsername(), ATTR_PASSWORD, user.getPassword());
        hashOps.put(user.getUsername(), ATTR_USEREMAIL, user.getEmail());
        hashOps.put(user.getUsername(), ATTR_USERAPIKEY, apikey);
        hashOps.put(user.getUsername(), ATTR_EMAILVERI, verification);
    }

    public void saveWatchList(String userId, List<String> watchlist) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        if (hashOps.get(userId, "watchlist") != null) {
            hashOps.delete(userId, "watchlist");
        }

        if (watchlist.size() > 0) {
            String watchlistString = watchlist.stream()
                    .collect(Collectors.joining(","));
            hashOps.put(userId, "watchlist", watchlistString);
        }
    }

    public Optional<List<String>> getWatchList(String userId) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        String data = hashOps.get(userId, "watchlist");
        if (data == null) {
            return Optional.empty();
        }
        String[] dataArray = data.split(",");
        List<String> watchlist = Arrays.stream(dataArray)
                .collect(Collectors.toCollection(LinkedList::new));

        return Optional.of(watchlist);
    }

    public Optional<String> getEmailVerificationString(String user) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        String data = hashOps.get(user, ATTR_EMAILVERI);
        return Optional.ofNullable(data);
    }

    public void deleteVerification(String user) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        hashOps.delete(user, ATTR_EMAILVERI);
    }

    public Optional<String> getUserUsingEmail(String email) {
        Set<String> redisKeys = template.keys("*");
        HashOperations<String, String, String> hashOps = template.opsForHash();
        for (String key : redisKeys) {
            if (hashOps.get(key, ATTR_USEREMAIL).equals(email)) {
                return Optional.of(key);
            }
        }
        return Optional.empty();
    }

    public void setPassword(String user, String password) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        hashOps.delete(user, ATTR_PASSWORD);
        hashOps.put(user, ATTR_PASSWORD, password);
    }
}

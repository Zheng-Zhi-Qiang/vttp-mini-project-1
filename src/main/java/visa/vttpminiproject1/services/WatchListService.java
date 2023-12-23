package visa.vttpminiproject1.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import visa.vttpminiproject1.repos.UserRepo;

@Service
public class WatchListService {
    
    @Autowired
    private UserRepo userRepo;

    public List<String> addToWatchList(String user, String ticker){
        List<String> watchlist = getWatchList(user);
        watchlist.add(ticker);
        userRepo.saveWatchList(user, watchlist);
        return watchlist;
    }

    public List<String> removeFromWatchList(String user, String ticker){
        List<String> watchlist = getWatchList(user);
        watchlist.remove(ticker);
        userRepo.saveWatchList(user, watchlist);
        return watchlist;
    }

    public List<String> getWatchList(String user){
        Optional<List<String>> opt = userRepo.getWatchList(user);
        if (opt.isEmpty()){
            return new LinkedList<>();
        }
        else {
            return opt.get();
        }
    }
}

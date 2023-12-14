package visa.vttpminiproject1.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import visa.vttpminiproject1.models.Position;
import visa.vttpminiproject1.repos.PortfolioRepo;

@Service
public class PortfolioService {
    
    @Autowired
    private PortfolioRepo portfolioRepo;

    public void addPosition(String userId, Position position){
        Optional<List<Position>> opt = portfolioRepo.getPortfolio(userId);
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

        portfolioRepo.savePortfolio(userId, portfolio);
    }

    private void addNewToExistingPosition(Position currPosition, Position newPosition){
        Double newCostBasis = (Double.parseDouble(currPosition.getCostBasis()) * currPosition.getQuantityPurchased() + 
                                        Double.parseDouble(newPosition.getCostBasis()) * newPosition.getQuantityPurchased()) / (currPosition.getQuantityPurchased() + newPosition.getQuantityPurchased());
        Integer newTotalQuantityPurchased = currPosition.getQuantityPurchased() + newPosition.getQuantityPurchased();
        currPosition.setCostBasis(newCostBasis.toString());
        currPosition.setQuantityPurchased(newTotalQuantityPurchased);
    }
}

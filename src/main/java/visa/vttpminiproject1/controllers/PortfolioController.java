package visa.vttpminiproject1.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import visa.vttpminiproject1.Utils;
import visa.vttpminiproject1.models.Portfolio;
import visa.vttpminiproject1.models.Position;
import visa.vttpminiproject1.services.PortfolioService;
import visa.vttpminiproject1.services.WatchListService;

@Controller
@RequestMapping(path = "/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioSvc;

    @Autowired
    private WatchListService watchlistSvc;
    
    @GetMapping(path = "/positionForm")
    public ModelAndView newPositionForm(HttpSession session){
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()){
            return opt.get();
        }
        ModelAndView mav = new ModelAndView("positionform");
        mav.addObject("position", new Position());
        return mav;
    }

    @PostMapping(path = "/newPosition")
    public ModelAndView createPosition(@Valid @ModelAttribute Position position, BindingResult result, HttpSession session){
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()){
            return opt.get();
        }

        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()){
            mav.setViewName("positionform");
            return mav;
        }
        String user = (String) session.getAttribute("user");
        Integer outcome = portfolioSvc.addPosition(user, position);
        if (!outcome.equals(0)){
            FieldError err = new FieldError("position", "ticker", position.getTicker(), false, null, null, "Invalid ticker!");
            result.addError(err);
            mav.setViewName("positionform");
            return mav;
        }

        mav.setViewName("redirect:/portfolio/positions");
        return mav;
    }

    @GetMapping(path = "/positions")
    public ModelAndView getPositions(HttpSession session){
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()){
            return opt.get();
        }
        ModelAndView mav = new ModelAndView("positionslist");
        String user = (String) session.getAttribute("user");
        Portfolio portfolio = portfolioSvc.getPortfolio(user);
        mav.addObject("portfolio", portfolio.getPositions());
        mav.addObject("nav", portfolio.getNAV());
        mav.addObject("watchlist", watchlistSvc.getWatchList(user));
        return mav;
    }
}

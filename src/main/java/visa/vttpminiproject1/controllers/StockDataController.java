package visa.vttpminiproject1.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import visa.vttpminiproject1.Utils;
import visa.vttpminiproject1.models.StockData;
import visa.vttpminiproject1.services.PortfolioService;
import visa.vttpminiproject1.services.WatchListService;

@Controller
@RequestMapping(path = "/stock")
public class StockDataController {

    @Autowired
    private PortfolioService portSvc;

    @Autowired
    private WatchListService watchlistSvc;
    
    @GetMapping(path = "/data")
    public ModelAndView getStockData(@RequestParam String ticker, HttpSession session){
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()){
            return opt.get();
        }
        ModelAndView mav = new ModelAndView();
        ticker = ticker.toUpperCase();
        Optional<StockData> result = portSvc.getStockData(ticker);
        if (!result.isEmpty()){
            mav.setViewName("stockdata");
            mav.addObject("data", result.get());
        }
        else {
            mav.setViewName("nostockdata");
        }
        mav.addObject("watchlist", watchlistSvc.getWatchList((String) session.getAttribute("user")));
        return mav;
    }

    @GetMapping(path = "/{ticker}")
    public ModelAndView showTickerData(@PathVariable String ticker, RedirectAttributes redirectAttributes){
        ModelAndView mav = new ModelAndView("redirect:/stock/data");
        redirectAttributes.addAttribute("ticker", ticker);
        return mav;
    }
}

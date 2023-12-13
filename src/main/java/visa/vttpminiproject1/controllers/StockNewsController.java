package visa.vttpminiproject1.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import visa.vttpminiproject1.models.News;
import visa.vttpminiproject1.services.StockNewsService;

@Controller
@RequestMapping(path = "/news")
public class StockNewsController {
    
    @Autowired
    private StockNewsService newsSvc;

    @GetMapping(path = "/query")
    public ModelAndView query(){
        ModelAndView mav = new ModelAndView("query");
        return mav;
    }

    @GetMapping(path = "/tickerNews")
    public ModelAndView getTickerNews(@RequestParam String ticker){
        ModelAndView mav = new ModelAndView("news");
        ticker = ticker.toUpperCase();
        List<News> news = newsSvc.getTickerNews(ticker);
        mav.addObject("news", news);
        mav.addObject("ticker", ticker);
        return mav;
    }
}

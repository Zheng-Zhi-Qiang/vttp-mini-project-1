package visa.vttpminiproject1.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import visa.vttpminiproject1.Utils;
import visa.vttpminiproject1.models.News;
import visa.vttpminiproject1.services.StockNewsService;

@Controller
@RequestMapping(path = {"/", "/index.html"})
public class LandingPageController {

    @Autowired
    private StockNewsService newsSvc;
    
    @GetMapping
    public ModelAndView index(HttpSession session){
        Optional<ModelAndView> opt = Utils.authenticated(session);
        ModelAndView mav = new ModelAndView("index");
        if (!opt.isEmpty()){
            session.setAttribute("authenticated", "false");
        }
        else {
            session.setAttribute("authenticated", "true");
        }
        return mav;
    }

    @GetMapping(path = "/latest")
    public ModelAndView getLatestNews(HttpSession session){
        ModelAndView mav = new ModelAndView("latest");
        List<News> news = newsSvc.getTickerNews("latest");
        mav.addObject("news", news);
        return mav;
    }
    

    
}

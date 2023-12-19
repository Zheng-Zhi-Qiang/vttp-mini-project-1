package visa.vttpminiproject1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path = {"/", "/index.html"})
public class LandingPageController {
    
    @GetMapping
    public ModelAndView index(HttpSession session){
        ModelAndView mav = new ModelAndView();
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || authenticated.booleanValue() == false){
            mav.setViewName("redirect:/user/login");
            return mav;
        }
        else {
            mav.setViewName("index.html");
            return mav;
        }
    }
}

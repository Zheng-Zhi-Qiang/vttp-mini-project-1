package visa.vttpminiproject1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import visa.vttpminiproject1.models.Position;
import visa.vttpminiproject1.services.PortfolioService;

@Controller
@RequestMapping(path = "/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioSvc;
    
    @GetMapping(path = "/positionForm")
    public ModelAndView newPositionForm(){
        ModelAndView mav = new ModelAndView("positionform");
        mav.addObject("position", new Position());
        return mav;
    }

    @PostMapping(path = "/newPosition")
    public ModelAndView createPosition(@Valid @ModelAttribute Position position, BindingResult result){
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()){
            mav.setViewName("positionform");
            return mav;
        }
        String userId = "test";
        portfolioSvc.addPosition(userId, position);
        mav.setViewName("positionslist");
        return mav;
    }
}

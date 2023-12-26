package visa.vttpminiproject1.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping(path = "/new")
    public ModelAndView createPosition(@Valid @ModelAttribute("new_position") Position position, BindingResult result,
            HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()) {
            return opt.get();
        }

        ModelAndView mav = new ModelAndView("positionslist");
        String user = (String) session.getAttribute("user");
        Portfolio portfolio = portfolioSvc.getPortfolio(user);
        mav.addObject("portfolio", portfolio.getPositions());
        mav.addObject("nav", portfolio.getNAV());
        mav.addObject("watchlist", watchlistSvc.getWatchList(user));
        mav.addObject("update_position", new Position());
        if (result.hasErrors()) {
            redirectAttributes.addAttribute("hasErrors", true);
            return mav;
        }
        Integer outcome = portfolioSvc.addPosition(user, position);
        if (!outcome.equals(0)) {
            redirectAttributes.addAttribute("hasErrors", true);
            FieldError err = new FieldError("position", "ticker", position.getTicker(), false, null, null,
                    "Invalid ticker!");
            result.addError(err);
            return mav;
        }
        mav.clear();
        mav.setViewName("redirect:/portfolio/positions");
        return mav;
    }

    @GetMapping(path = "/positions")
    public ModelAndView getPositions(HttpSession session) {
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()) {
            return opt.get();
        }
        ModelAndView mav = new ModelAndView("positionslist");
        String user = (String) session.getAttribute("user");
        Portfolio portfolio = portfolioSvc.getPortfolio(user);
        mav.addObject("portfolio", portfolio.getPositions());
        mav.addObject("nav", portfolio.getNAV());
        mav.addObject("watchlist", watchlistSvc.getWatchList(user));
        mav.addObject("update_position", new Position());
        mav.addObject("new_position", new Position());
        return mav;
    }

    @GetMapping(path = "/delete/{ticker}")
    public ModelAndView deletePosition(@PathVariable String ticker, HttpSession session) {
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()) {
            return opt.get();
        }
        ModelAndView mav = new ModelAndView("redirect:/portfolio/positions");
        String user = (String) session.getAttribute("user");
        portfolioSvc.deletePosition(user, ticker);
        return mav;
    }

    @PostMapping(path = "/update")
    public ModelAndView updatePosition(@Valid @ModelAttribute("update_position") Position position,
            BindingResult result,
            HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()) {
            return opt.get();
        }

        ModelAndView mav = new ModelAndView("positionslist");
        String user = (String) session.getAttribute("user");
        Portfolio portfolio = portfolioSvc.getPortfolio(user);
        mav.addObject("portfolio", portfolio.getPositions());
        mav.addObject("nav", portfolio.getNAV());
        mav.addObject("watchlist", watchlistSvc.getWatchList(user));
        mav.addObject("new_position", new Position());

        if (result.hasErrors()) {
            redirectAttributes.addAttribute("hasErrors", true);
            return mav;
        }
        Integer outcome = portfolioSvc.updatePosition(user, position);
        if (!outcome.equals(0)) {
            redirectAttributes.addAttribute("hasErrors", true);
            FieldError err = new FieldError("position", "ticker", position.getTicker(), false, null, null,
                    "Invalid ticker!");
            result.addError(err);
            return mav;
        }
        mav.clear();
        mav.setViewName("redirect:/portfolio/positions");
        return mav;
    }
}

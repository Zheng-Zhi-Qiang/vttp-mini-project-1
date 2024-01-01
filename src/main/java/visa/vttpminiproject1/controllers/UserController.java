package visa.vttpminiproject1.controllers;

import static visa.vttpminiproject1.Utils.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import visa.vttpminiproject1.Utils;
import visa.vttpminiproject1.models.User;
import visa.vttpminiproject1.services.StockNewsService;
import visa.vttpminiproject1.services.UserService;
import visa.vttpminiproject1.services.WatchListService;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userSvc;

    @Autowired
    private WatchListService watchListSvc;

    @Autowired
    private StockNewsService newsSvc;

    @GetMapping(path = "/register")
    public ModelAndView registerForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("user", new User());
        return mav;
    }

    @GetMapping(path = "/login")
    public ModelAndView loginForm() {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @PostMapping(path = "/register")
    public ModelAndView createNewUser(@Valid @ModelAttribute User user, BindingResult result, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("register");
            return mav;
        }

        if (userSvc.accountWithEmailExists(user.getEmail())) {
            mav.setViewName("register");
            mav.addObject("error", "Account with email exists. Please login with registered account.");
            return mav;
        }
        userSvc.createUser(user);
        mav.setViewName("registration_success");
        return mav;
    }

    @PostMapping(path = "/login")
    public ModelAndView loginUser(@RequestBody MultiValueMap<String, String> data, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        String username = data.getFirst(ATTR_USERNAME);
        String password = data.getFirst(ATTR_PASSWORD);
        if (userSvc.authenticateUser(username, password) && userSvc.userVerified(username)) {
            session.setAttribute("authenticated", "true");
            session.setAttribute("user", username);
            mav.setViewName("redirect:/user/home");
            return mav;
        } else {
            mav.setViewName("login");
            if (!userSvc.authenticateUser(username, password)) {
                mav.addObject("error", "Invalid username or password!");
            } else {
                mav.addObject("error", "Please verify your email!");
            }
            return mav;
        }
    }

    @GetMapping(path = "/logout")
    public ModelAndView logoutUser(HttpSession session) {
        ModelAndView mav = new ModelAndView("redirect:/");
        session.invalidate();
        return mav;
    }

    @GetMapping(path = "/home")
    public ModelAndView userHome(HttpSession session) {
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()) {
            return opt.get();
        }
        String user = (String) session.getAttribute("user");
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("watchlist", watchListSvc.getWatchList(user));
        mav.addObject("news", newsSvc.getRelatedNews(user));
        return mav;
    }

    @GetMapping(path = "/settings")
    public ModelAndView userSettings(HttpSession session) {
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()) {
            return opt.get();
        }
        String user = (String) session.getAttribute("user");
        ModelAndView mav = new ModelAndView("user_profile");
        mav.addObject("user", userSvc.getUserDetails(user));
        return mav;
    }

    @GetMapping(path = "/{emailVerificationCode}")
    public ModelAndView verifyEmail(@PathVariable String emailVerificationCode, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        String user = emailVerificationCode.split("_")[0];
        Integer verified = userSvc.verifyUser(user, emailVerificationCode);
        if (!verified.equals(0)) {
            mav.setViewName("redirect:/error");
            return mav;
        }
        session.setAttribute("authenticated", "true");
        session.setAttribute("user", user);
        mav.setViewName("redirect:/user/home");
        return mav;
    }

    @GetMapping(path = "/reset")
    public ModelAndView resetForm() {
        ModelAndView mav = new ModelAndView("reset_password");
        return mav;
    }

    @GetMapping(path = "/password")
    public ModelAndView changePasswordForm(HttpSession session) {
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()) {
            return opt.get();
        }
        ModelAndView mav = new ModelAndView("change_password");
        return mav;
    }

    @GetMapping(path = "/api")
    public ModelAndView exportDetails(HttpSession session) {
        Optional<ModelAndView> opt = Utils.authenticated(session);
        if (!opt.isEmpty()) {
            return opt.get();
        }
        String user = (String) session.getAttribute("user");
        ModelAndView mav = new ModelAndView("api");
        mav.addObject("apiKey", userSvc.getUserAPIKey(user));
        return mav;
    }
}

package visa.vttpminiproject1.controllers;

import static visa.vttpminiproject1.Utils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import visa.vttpminiproject1.services.UserService;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    
    @Autowired
    private UserService userSvc;

    @GetMapping(path = "/register")
    public ModelAndView registerForm(){
        ModelAndView mav = new ModelAndView("register");
        return mav;
    }

    @GetMapping(path = "/login")
    public ModelAndView loginForm(){
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @PostMapping(path = "/registerNewUser")
    public ModelAndView createNewUser(@RequestBody MultiValueMap<String, String> data, HttpSession session){
        ModelAndView mav = new ModelAndView();
        try{
            String username = data.getFirst(ATTR_USERNAME);
            String password = data.getFirst(ATTR_PASSWORD);
            userSvc.createUser(username, password);
            session.setAttribute("authenticated", "true");
            session.setAttribute("user", username);
            mav.setViewName("redirect:/");
        }
        catch (Exception e){
            mav.setViewName("error");
        }
        return mav;
    }

    @PostMapping(path = "/loginUser")
    public ModelAndView loginUser(@RequestBody MultiValueMap<String, String> data, HttpSession session){
        ModelAndView mav = new ModelAndView();
        try {
            String username = data.getFirst(ATTR_USERNAME);
            String password = data.getFirst(ATTR_PASSWORD);
            if (userSvc.authenticateUser(username, password)){
                session.setAttribute("authenticated", "true");
                session.setAttribute("user", username);
                mav.setViewName("redirect:/");
                return mav;
            }
            else {
                mav.setViewName("login");
                mav.addObject("error", "Invalid username or password!");
                return mav;
            }
        }
        catch (Exception e){
            mav.setViewName("error");
        }

        return mav;
    }

    @GetMapping(path = "/logout")
    public ModelAndView logoutUser(HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/");
        session.invalidate();
        return mav;
    }
}

package visa.vttpminiproject1.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = { "/error", "/error.html" })
public class ProjectErrorController implements ErrorController {

    public ModelAndView error(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode;
        if (status != null) {
            statusCode = Integer.valueOf(status.toString());
        } else {
            statusCode = 500;
        }
        mav.addObject("status", statusCode);
        return mav;
    }
}

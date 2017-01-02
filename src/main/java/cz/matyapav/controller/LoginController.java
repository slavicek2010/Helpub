package cz.matyapav.controller;

import cz.matyapav.models.User;
import cz.matyapav.models.dao.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    GenericDao<User, String> userDao;

    List<String> messages = new ArrayList<>();
    List<String> errors = new ArrayList<>();


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        if(request.getParameter("messages") != null){
            model.addObject("messages", request.getParameter("messages"));
        }
        if (error != null) {
            errors.clear();
            errors.add("Invalid username or password.");
            model.addObject("errors", errors);
        }

        if (logout != null) {
            messages.clear();;
            messages.add("You've been logged out successfully.");
            model.addObject("messages", messages);
        }
        model.setViewName("login");

        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
}

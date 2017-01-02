package cz.matyapav.controller;

import cz.matyapav.models.Bill;
import cz.matyapav.models.Item;
import cz.matyapav.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Pavel on 14.12.2016.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public ModelAndView home(HttpServletRequest request) {
        Item item = new Item();
        ModelAndView model = new ModelAndView();
        model.addObject("item", item);
        model.setViewName("home");
        return model;
    }

    @RequestMapping(value = "/login")
    public String login(){
        return "login";
    }
}

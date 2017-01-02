package cz.matyapav.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * Created by Pavel on 25.12.2016.
 */
@Controller
public class AccessController {

    // for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied(Principal user) {

        ModelAndView model = new ModelAndView();

        if (user != null) {
            model.addObject("msg", "Hi " + user.getName() + ", sadly you do not have permission to access this page!");
        } else {
            model.addObject("msg",
                    "Sadly, you do not have permission to access this page!");
        }

        model.setViewName("403");
        return model;

    }
}

package cz.matyapav.controller;

import cz.matyapav.models.User;
import cz.matyapav.models.UserRole;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.services.UserService;
import cz.matyapav.utils.StatusMessages;
import cz.matyapav.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 14.12.2016.
 */

@Controller
public class UserController {

    @Autowired
    GenericDao<User, String> userDAO;
    @Autowired
    GenericDao<UserRole, String> userRoleDAO;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView users(ModelAndView model, HttpServletRequest request) {
        if(request.getParameter("errors") != null){
            model.addObject("errors", request.getParameter("errors"));
        }
        if(request.getParameter("messages") != null){
            model.addObject("messages", request.getParameter("messages"));
        }

        List<User> userList = userService.getAllUsers();

        model.addObject("listUsers", userList);
        model.setViewName("users");
        model.addObject("loggedUserUsername", Utils.getLoggedUser().getUsername());
        if(Utils.loggedUserIsAdmin()){
            model.addObject("admin", true);
        }
        return model;
    }

    /**
     * Zobrazi formular pro registraci usera
     * @param model
     * @return
     */
    @RequestMapping(value = "/users/create", method = RequestMethod.GET)
    public ModelAndView showUserForm(ModelAndView model) {
        User user = new User();
        setPropertiesToUserCreateForm(model, user);
        return model;
    }


    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView addUser(@ModelAttribute("User") User user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<String> messages = new ArrayList<>();

        StatusMessages statusMessages = userService.createUser(user);

        if(statusMessages.hasErrors()) {
            request.setAttribute("errors", statusMessages.getErrors());
            ModelAndView model = new ModelAndView();
            setPropertiesToUserCreateForm(model, user);
            return model;
        }
        if(statusMessages.hasMessages()){
            redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/users/edit", method = RequestMethod.POST)
    public ModelAndView editUser(@ModelAttribute("User") User user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<String> messages = new ArrayList<>();

        StatusMessages statusMessages = userService.editUser(user);

        if(statusMessages.hasErrors()) {
            request.setAttribute("errors", statusMessages.getErrors());
            ModelAndView model = new ModelAndView();
            setPropertiesToUserCreateForm(model, user);
            return model;
        }

        if(statusMessages.hasMessages()){
            redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
        }
        return new ModelAndView("redirect:/users");
    }


    @RequestMapping(value = "/users/delete", method = RequestMethod.GET)
    public ModelAndView deleteUser(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String username = request.getParameter("username");
        StatusMessages statusMessages = userService.deleteUser(username);
        if(statusMessages.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
        }
        if(statusMessages.hasMessages()){
            redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
        }

        return new ModelAndView("redirect:/users");
    }

    @RequestMapping(value = "/users/edit", method = RequestMethod.GET)
    public ModelAndView showEditUserForm(HttpServletRequest request, ModelAndView model, RedirectAttributes redirectAttributes) {
        StatusMessages statusMessages = new StatusMessages();

        if(request.getParameter("username") == null){
            return new ModelAndView("redirect:/users");
        }
        User user = userService.getUser(request.getParameter("username"));


        //TODO predelat do service kdyz bude cas
        if(user == null){
            statusMessages.addError("User not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
            return new ModelAndView("redirect:/users");
        }
        if(!loggedUserHasPrivilageToEditUser(user)){
            statusMessages.addError("You can't edit other users.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
            return new ModelAndView("redirect:/users");
        }

        setPropertiesToUserEditForm(model, user);
        return model;
    }

    private void setPropertiesToUserEditForm(ModelAndView model, User user){
        model.addObject("user", user);
        model.addObject("formAction", "/users/edit");
        model.addObject("pkHidden", true);
        model.addObject("passwordFieldHidden", true);
        model.addObject("formName", "Edit user");
        model.setViewName("user_form");
    }

    private void setPropertiesToUserCreateForm(ModelAndView model, User user){
        model.addObject("user", user);
        model.addObject("formAction", "/users/create");
        model.addObject("pkHidden", false);
        model.addObject("passwordFieldHidden", false);
        model.addObject("formName", "Registration");
        model.setViewName("user_form");
    }



    private boolean loggedUserHasPrivilageToEditUser(User user){
        return Utils.getLoggedUser().getUsername().equals(user.getUsername()) || Utils.loggedUserIsAdmin();
    }


}

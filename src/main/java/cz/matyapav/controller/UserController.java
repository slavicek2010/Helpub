package cz.matyapav.controller;

import com.sun.javafx.sg.prism.NGShape;
import cz.matyapav.models.enums.Roles;
import cz.matyapav.models.User;
import cz.matyapav.models.UserRole;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.models.validators.UserValidator;
import cz.matyapav.models.validators.Validator;
import cz.matyapav.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    protected EntityManager entityManager;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView users(ModelAndView model, HttpServletRequest request) {
        if(request.getParameter("errors") != null){
            model.addObject("errors", request.getParameter("errors"));
        }
        if(request.getParameter("messages") != null){
            model.addObject("messages", request.getParameter("messages"));
        }

        List<User> userList = userDAO.list();
        model.addObject("listUsers", userList);
        model.setViewName("users");
        model.addObject("loggedUserUsername", Utils.getLoggedUser().getUsername());
        if(Utils.loggedUserIsAdmin()){
            model.addObject("admin", true);
        }
        return model;
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.GET)
    public ModelAndView user(ModelAndView model) {
        User user = new User();
        setPropertiesToUserCreateForm(model, user);
        return model;
    }


    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView addUser(@ModelAttribute("User") User user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<String> messages = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        //validation conditions
        Validator<User> validator = new UserValidator();
        boolean validationFails = false;
        if(!validator.validate(user)){
            errors.addAll(validator.getErrorMessages());
            validationFails = true;
        }
        if(userDAO.read(user.getUsername()) != null){
            //user already exits
            errors.add("Username already exits.");
            validationFails = true;
        }
        if(validationFails){
            request.setAttribute("errors", errors);
            ModelAndView model = new ModelAndView();
            setPropertiesToUserCreateForm(model, user);
            return model;
        }


        //encrypt created/edited password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setEnabled(1);

        //add role to user
        UserRole role = userRoleDAO.read(Roles.USER.getRoleName());
        //create role if it doesnt exists
        if(role == null){
            role = new UserRole();
            role.setRole(Roles.USER.getRoleName());
            userRoleDAO.create(role);
        }
        user.addRole(role);

        //update or create user
        userDAO.create(user);
        Utils.addMessage(messages, "Registration was successfull. You can now log yourself in.", redirectAttributes);
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/users/edit", method = RequestMethod.POST)
    public ModelAndView editUser(@ModelAttribute("User") User user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<String> messages = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        Validator<User> validator = new UserValidator();
        if(!validator.validate(user)){
            request.setAttribute("errors", validator.getErrorMessages());
            ModelAndView model = new ModelAndView();
            setPropertiesToUserEditForm(model, user);
            return model;
        }
        //if user is not admin and edits anorther user throw error
        if(!loggedUserHasPrivilageToEditUser(user)){
            ModelAndView model = new ModelAndView();
            setPropertiesToUserEditForm(model, user);
            Utils.addError(errors, "You can't edit other users.", redirectAttributes);
            return model;
        }
        User user1 = userDAO.read(user.getUsername());
        if(user1 != null) {
            user1.setFirstName(user.getFirstName());
            user1.setLastName(user.getLastName());
            userDAO.update(user1);
        }
        Utils.addMessage(messages, "User successfully edited.", redirectAttributes);
        return new ModelAndView("redirect:/users");
    }


    @RequestMapping(value = "/users/delete", method = RequestMethod.GET)
    public ModelAndView deleteUser(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<String> messages = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        if(!Utils.loggedUserIsAdmin()){
            Utils.addError(errors, "This action can be done only by admin.", redirectAttributes);
            return new ModelAndView("redirect:/users");
        }
        String username = request.getParameter("username");
        //TODO odstranit usera ze vsech ostatnich tabulek tj. bills (pokud bude creator tak to smazem?), spojovaci bills items (vymazat ho i jeho pridane itemy z billu, itemz vsak ne)
        if(!userDAO.delete(username)){
            Utils.addError(errors, "User with username "+username+" was not found.", redirectAttributes);
        }else {
            Utils.addMessage(messages, "User successfully deleted.", redirectAttributes);
        }
        return new ModelAndView("redirect:/users");
    }

    @RequestMapping(value = "/users/edit", method = RequestMethod.GET)
    public ModelAndView editUser(HttpServletRequest request, ModelAndView model, RedirectAttributes redirectAttributes) {
        List<String> errors = new ArrayList<>();

        if(request.getParameter("username") == null){
            return new ModelAndView("redirect:/users");
        }
        User user = userDAO.read(request.getParameter("username"));
        if(!loggedUserHasPrivilageToEditUser(user)){
            Utils.addError(errors, "You can't edit other users.", redirectAttributes);
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

package cz.matyapav.services;

import cz.matyapav.models.User;
import cz.matyapav.models.UserRole;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.models.enums.Roles;
import cz.matyapav.models.validators.UserValidator;
import cz.matyapav.models.validators.Validator;
import cz.matyapav.utils.StatusMessages;
import cz.matyapav.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Pavel on 02.01.2017.
 */
public class UserService {

    @Autowired
    GenericDao<User, String> userDAO;

    @Autowired
    UserRoleService userRoleService;

    public List<User> getAllUsers(){
        return userDAO.list();
    }

    public StatusMessages createUser(User user){
        //validation conditions
        StatusMessages statusMessages = new StatusMessages();
        Validator<User> validator = new UserValidator();
        boolean validationFails = false;
        if(!validator.validate(user)){
            statusMessages.addMultipleErrors(validator.getErrorMessages());
            validationFails = true;
        }
        if(userDAO.read(user.getUsername()) != null){
            //user already exits
            statusMessages.addError("Username already exits.");
            validationFails = true;
        }
        if(validationFails){
            return statusMessages;
        }

        //encrypt created/edited password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setEnabled(1);

        //add role to user
        UserRole role = userRoleService.getRole(Roles.USER.getRoleName());
        //create role if it doesnt exists
        if(role == null){
            role = new UserRole();
            role.setRole(Roles.USER.getRoleName());
            userRoleService.createRole(role);
        }
        user.addRole(role);

        //update or create user
        userDAO.create(user);

        statusMessages.addMessage("Registration was successfull. You can now log yourself in.");
        return statusMessages;
    }

    public StatusMessages editUser(User user){
        StatusMessages statusMessages = new StatusMessages();
        Validator<User> validator = new UserValidator();
        boolean validationFails = false;
        if(!validator.validate(user)){
            statusMessages.addMultipleErrors(validator.getErrorMessages());
            validationFails = true;
        }
        if(!loggedUserHasPrivilageToEditUser(user)){
            ModelAndView model = new ModelAndView();
            statusMessages.addError("You can't edit other users.");
            validationFails = true;
        }
        if(validationFails){
            return statusMessages;
        }

        User user1 = userDAO.read(user.getUsername());
        if(user1 != null) {
            user1.setFirstName(user.getFirstName());
            user1.setLastName(user.getLastName());
            userDAO.update(user1);
        }

        statusMessages.addMessage("User successfully edited.");
        return statusMessages;
    }

    public User getUser(String username){
        return userDAO.read(username);
    }

    public StatusMessages deleteUser(String username){
        StatusMessages statusMessages = new StatusMessages();
        //TODO odstranit usera ze vsech ostatnich tabulek tj. bills (pokud bude creator tak to smazem?), spojovaci bills items (vymazat ho i jeho pridane itemy z billu, itemz vsak ne)
        if(Utils.getLoggedUser().getUsername().equals(username)){
            statusMessages.addError("You cannot delete yourself.");
            return statusMessages;
        }
        if(!Utils.loggedUserIsAdmin()) {
            statusMessages.addError("This action can be done only by admin.");
            return statusMessages;
        }
        if(!userDAO.delete(username)){
            statusMessages.addError("User with username "+username+" was not found.");
        }else{
            statusMessages.addMessage("User successfully deleted.");
        }
        return statusMessages;
    }

    private boolean loggedUserHasPrivilageToEditUser(User user){
        return Utils.getLoggedUser().getUsername().equals(user.getUsername()) || Utils.loggedUserIsAdmin();
    }


}

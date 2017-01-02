package cz.matyapav.models.validators;

import cz.matyapav.models.User;
import cz.matyapav.models.dao.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 25.12.2016.
 */
public class UserValidator extends Validator<User> {

    @Autowired
    private GenericDao<User, String> userDAO;

    @Override
    public boolean validate(User user) {
        boolean withoutErrors = true;

        withoutErrors = validatePasswordMatch(user) &
                        validatePasswordLength(user) &
                        validateUsernameLength(user) &
                        validateFirstNameLength(user) &
                        validateLastNameLength(user);

        return withoutErrors;
    }

    public boolean validatePasswordMatch(User user){
        if(user.getPassword() != null && user.getPasswordRetype() != null &&!user.getPassword().equals(user.getPasswordRetype())){
            addErrorMessage("Passwords do not match!!");
            return false;
        }
        return true;
    }

    public boolean validatePasswordLength(User user){
        if(user.getPassword() != null && user.getPassword().length() < 8){
            addErrorMessage("Passwords must be at least 8 characters long!!");
            return false;
        }
        return true;
    }

    public boolean validateUsernameLength(User user){
        if(user.getUsername() != null && user.getUsername().length() > 40){
            addErrorMessage("Username must have 40 chars max!!");
            return false;
        }
        return true;
    }

    public boolean validateFirstNameLength(User user){
        if(user.getFirstName() != null && user.getFirstName().length() > 80){
            addErrorMessage("Firstname must have 80 chars max!!");
            return false;
        }
        return true;
    }

    public boolean validateLastNameLength(User user){
        if(user.getLastName() != null && user.getLastName().length() > 80){
            addErrorMessage("Lastname must have 80 chars max!!");
            return false;
        }
        return true;
    }


}

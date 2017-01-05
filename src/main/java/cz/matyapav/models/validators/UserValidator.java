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
                        validatePasswordTooLong(user) &
                        validatePasswordLength(user) &
                        validateUsernameLength(user) &
                        validateFirstNameLength(user) &
                        validateLastNameLength(user);

        return withoutErrors;
    }

    private boolean validatePasswordMatch(User user){
        if(user.getPassword() == null || user.getPasswordRetype() == null){
            addErrorMessage("Password or password retype is not set.");
            return false;
        }
        if(user.getPassword() != null && user.getPasswordRetype() != null &&!user.getPassword().equals(user.getPasswordRetype())){
            addErrorMessage("Passwords do not match!!");
            return false;
        }
        return true;
    }

    private boolean validatePasswordLength(User user){
        if(user.getPassword() == null){
            addErrorMessage("Password is not set.");
            return false;
        }
        if(user.getPassword().length() < 8){
            addErrorMessage("Passwords must be at least 8 characters long!!");
            return false;
        }
        return true;
    }

    private boolean validatePasswordTooLong(User user){
        if(user.getPassword() == null){
            addErrorMessage("Password is not set.");
            return false;
        }
        if(user.getPassword().length() > 60){
            addErrorMessage("Passwords can have no more than 60 characters long!!");
            return false;
        }
        return true;
    }

    private boolean validateUsernameLength(User user){
        if(user.getUsername() == null){
            addErrorMessage("Username is not set.");
            return false;
        }
        if(user.getUsername().length() > 40){
            addErrorMessage("Username must have 40 chars max!!");
            return false;
        }
        return true;
    }

    private boolean validateFirstNameLength(User user){
        if(user.getFirstName() == null){
            addErrorMessage("First name is not set.");
            return false;
        }
        if(user.getFirstName().length() > 80){
            addErrorMessage("First name must have 80 chars max!!");
            return false;
        }
        return true;
    }

    private boolean validateLastNameLength(User user){
        if(user.getLastName() == null){
            addErrorMessage("Last name is not set.");
            return false;
        }
        if(user.getLastName().length() > 80){
            addErrorMessage("Last name must have 80 chars max!!");
            return false;
        }
        return true;
    }


}

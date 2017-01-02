package cz.matyapav.models.validators;

import cz.matyapav.models.User;
import cz.matyapav.models.dao.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 25.12.2016.
 */
public abstract class Validator<T> {

    private List<String> errorMessages;

    public abstract boolean validate(T model);

    public List<String> getErrorMessages(){
        return errorMessages;
    }

    public void addErrorMessage(String errorMsg){
        if(errorMessages == null){
            errorMessages = new ArrayList<>();
        }
        if(errorMsg != null){
            errorMessages.add(errorMsg);
        }
    }
}

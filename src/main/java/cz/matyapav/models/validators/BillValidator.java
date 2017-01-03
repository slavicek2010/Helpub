package cz.matyapav.models.validators;

import cz.matyapav.models.Bill;
import cz.matyapav.models.User;

/**
 * Created by Pavel on 25.12.2016.
 */
public class BillValidator extends Validator<Bill> {
    @Override
    public boolean validate(Bill bill) {
        boolean withoutErrors = true;

        withoutErrors = validateBillNameLenght(bill) & validatePasswordMatch(bill) & validatePasswordLength(bill);

        return withoutErrors;
    }

    public boolean validatePasswordMatch(Bill bill){
        if(bill.isLocked() && bill.getPassword() != null && bill.getPasswordRetype() != null &&!bill.getPassword().equals(bill.getPasswordRetype())){
            addErrorMessage("Passwords do not match!!");
            return false;
        }
        return true;
    }

    public boolean validatePasswordLength(Bill bill){
        if(bill.isLocked() && bill.getPassword() != null && bill.getPassword().length() < 4){
            addErrorMessage("Passwords must be at least 4 characters long!!");
            return false;
        }
        return true;
    }

    public boolean validateBillNameLenght(Bill bill){
        if(bill.getName() != null && bill.getName().length() > 255){
            addErrorMessage("Bill name can contain no more than 255 characters!!");
            return false;
        }
        return true;
    }
}

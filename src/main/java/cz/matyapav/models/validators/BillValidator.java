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

        withoutErrors = validateBillNameLenght(bill) & validatePasswordMatch(bill) & validatePasswordTooLong(bill) &validatePasswordLength(bill);

        return withoutErrors;
    }

    private boolean validatePasswordMatch(Bill bill){
        if(bill.isLocked()) {
            if (bill.getPassword() == null || bill.getPasswordRetype() == null) {
                addErrorMessage("Password or password retype is not set!!");
                return false;
            }
            if (!bill.getPassword().equals(bill.getPasswordRetype())) {
                addErrorMessage("Passwords do not match!!");
                return false;
            }
        }
        return true;
    }

    private boolean validatePasswordLength(Bill bill) {
        if(bill.isLocked()) {
            if (bill.getPassword() == null) {
                addErrorMessage("Password is not set!!");
                return false;
            }
            if (bill.getPassword().length() < 4) {
                addErrorMessage("Passwords must be at least 4 characters long!!");
                return false;
            }
        }
        return true;
    }

    private boolean validatePasswordTooLong(Bill bill){
        if(bill.isLocked()) {
            if (bill.getPassword() == null) {
                addErrorMessage("Password is not set!!");
                return false;
            }
            if (bill.getPassword().length() > 60) {
                addErrorMessage("Passwords can contain no more than 255 characters long!!");
                return false;
            }
        }
        return true;
    }

    private boolean validateBillNameLenght(Bill bill){
        if(bill.getName() == null){
            addErrorMessage("Bill name is not set!!");
            return false;
        }
        if(bill.getName().length() > 255){
            addErrorMessage("Bill name can contain no more than 255 characters!!");
            return false;
        }
        return true;
    }
}

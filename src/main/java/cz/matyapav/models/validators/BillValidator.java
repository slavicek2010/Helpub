package cz.matyapav.models.validators;

import cz.matyapav.models.Bill;

/**
 * Created by Pavel on 25.12.2016.
 */
public class BillValidator extends Validator<Bill> {
    @Override
    public boolean validate(Bill bill) {
        boolean withoutErrors = true;

        withoutErrors = validateBillNameLenght(bill);

        return withoutErrors;
    }

    public boolean validateBillNameLenght(Bill bill){
        if(bill.getName() != null && bill.getName().length() > 255){
            addErrorMessage("Bill name can contain no more than 255 characters!!");
            return false;
        }
        return true;
    }
}

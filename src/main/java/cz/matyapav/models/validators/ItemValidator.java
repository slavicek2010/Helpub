package cz.matyapav.models.validators;

import cz.matyapav.models.Item;

/**
 * Created by Pavel on 25.12.2016.
 */
public class ItemValidator extends Validator<Item> {

    @Override
    public boolean validate(Item item) {
        boolean withoutErrors = true;

        withoutErrors = validateItemNameLength(item);

        return withoutErrors;
    }

    public boolean validateItemNameLength(Item item){
        if(item.getName() != null && item.getName().length() > 255){
            addErrorMessage("Item name can contain no more than 255 characters!!");
            return false;
        }
        return true;
    }
}

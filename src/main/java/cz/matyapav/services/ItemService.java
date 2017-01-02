package cz.matyapav.services;

import cz.matyapav.models.Item;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.models.enums.ItemTypes;
import cz.matyapav.models.validators.ItemValidator;
import cz.matyapav.models.validators.Validator;
import cz.matyapav.utils.StatusMessages;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Pavel on 02.01.2017.
 */
public class ItemService {

    @Autowired
    private GenericDao<Item, String> itemDao;

    public StatusMessages createItem(Item item){
        StatusMessages statusMessages = new StatusMessages();
        boolean validationFails = false;
        Validator<Item> validator = new ItemValidator();
        if(!validator.validate(item)){
            statusMessages.addMultipleErrors(validator.getErrorMessages());
            validationFails = true;
        }
        if(itemDao.read(item.getName()) != null){
            //user already exits
            statusMessages.addError("Item with that name already exits.");
            validationFails = true;
        }
        if(validationFails){
            return statusMessages;
        }
        itemDao.create(item);

        return statusMessages;
    }

    public Item getItem(String itemName){
        return itemDao.read(itemName);
    }

    public List<Item> getAllItems(){
        return itemDao.list();
    }
}

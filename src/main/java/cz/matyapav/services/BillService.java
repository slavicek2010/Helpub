package cz.matyapav.services;

import cz.matyapav.models.Bill;
import cz.matyapav.models.Item;
import cz.matyapav.models.ItemBill;
import cz.matyapav.models.User;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.models.validators.BillValidator;
import cz.matyapav.models.validators.Validator;
import cz.matyapav.utils.ItemBillId;
import cz.matyapav.utils.StatusMessages;
import cz.matyapav.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Pavel on 02.01.2017.
 */
public class BillService {

    @Autowired
    private GenericDao<Bill, Integer> billDao;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemBillService itemBillService;

    @Autowired
    UserService userService;

    public List<Bill> getAllBills(){
        return billDao.list();
    }

    public Bill getBill(int id){
        return billDao.read(id);
    }

    public StatusMessages createBill(Bill bill){
        //validation conditions
        StatusMessages statusMessages = new StatusMessages();
        Validator<Bill> validator = new BillValidator();
        if(!validator.validate(bill)){
            return statusMessages;
        }
        //create getBillForm
        bill.setOpened(true);
        User user = userService.getUser(bill.getCreatorUsername());
        bill.getUsers().add(user);

        billDao.create(bill);

        statusMessages.addMessage("Bill succesfully created.");
        return statusMessages;
    }

    public StatusMessages editBill(Bill bill) {
        StatusMessages statusMessages = new StatusMessages();
        Validator<Bill> validator = new BillValidator();
        if(!validator.validate(bill)){
            statusMessages.addMultipleErrors(validator.getErrorMessages());
            return statusMessages;
        }
        Bill bill1 = billDao.read(bill.getId());
        User loggedUser = userService.getUser(Utils.getLoggedUser().getUsername());
        if(!loggedUser.isInBill(bill1) && !Utils.loggedUserIsAdmin()){
            statusMessages.addError("You don't have permission to edit this bill.");
            return statusMessages;
        }
        if(bill1 != null) {
            bill1.setName(bill.getName());
            billDao.update(bill1);
        }
        statusMessages.addMessage("Bill successfully edited.");
        return statusMessages;
    }

    public StatusMessages addItem(Item item,int billId, double price){
        StatusMessages statusMessages = new StatusMessages();
        Bill billDB = billDao.read(billId);
        if(billDB == null){
            statusMessages.addError("Bill not found");
            return statusMessages;
        }

        //connect showItemForm and getBillForm
        ItemBill itemBill = new ItemBill();
        ItemBillId itemBillId = new ItemBillId();
        itemBillId.setItem(item);
        itemBillId.setBill(billDB);
        itemBillId.setAddedBy(Utils.getLoggedUser().getUsername());
        itemBill.setPrimaryKey(itemBillId);
        itemBill.setPrice(price);

        ItemBill itemBillDB = itemBillService.getItemBill(itemBillId);
        if(itemBillDB != null){
            statusMessages.addError("Item is already in the bill!");
        }else{
            itemBill.setQuantity(1);
            StatusMessages statusMessagesItemBill = itemBillService.createItemBill(itemBill);
            if(statusMessagesItemBill.hasErrors()){
                statusMessages.addMultipleErrors(statusMessagesItemBill.getErrors());
            }
            if(statusMessagesItemBill.hasMessages()){
                statusMessages.addMultipleMessages(statusMessagesItemBill.getMessages());
            }
        }
        return statusMessages;
    }

    public StatusMessages removeItem(String itemName, int billId, String user){
        StatusMessages statusMessages = new StatusMessages();
        Bill billDB = billDao.read(billId);
        if(billDB == null){
            statusMessages.addError("Bill not found.");
            return statusMessages;
        }

        Item itemDB = itemService.getItem(itemName);
        if(itemDB == null){
            statusMessages.addError("Item not found.");
            return statusMessages;
        }

        ItemBillId itemBillId = new ItemBillId();
        itemBillId.setItem(itemDB);
        itemBillId.setBill(billDB);
        itemBillId.setAddedBy(user);

        if(!Utils.loggedUserIsAdmin() && !user.equals(Utils.getLoggedUser().getUsername())){
            statusMessages.addError("You cannot delete someones items");
            return statusMessages;
        }

        StatusMessages statusMessagesItemBill = itemBillService.deleteItemBill(itemBillId);
        if(statusMessagesItemBill.hasErrors()){
            statusMessages.addMultipleErrors(statusMessagesItemBill.getErrors());
        }
        if(statusMessagesItemBill.hasMessages()){
            statusMessages.addMultipleMessages(statusMessagesItemBill.getMessages());
        }

        return statusMessages;
    }

    public StatusMessages increaseItemQuantity(int billId, String itemName, String user, int howMuch){
        StatusMessages statusMessages = new StatusMessages();
        Bill billDB = billDao.read(billId);
        if (billDB == null) {
            statusMessages.addError("Bill not found.");
            return statusMessages;
        }

        Item itemDB = itemService.getItem(itemName);
        if(itemDB == null){
            statusMessages.addError("Item not found.");
            return statusMessages;
        }

        ItemBillId itemBillId = new ItemBillId();
        itemBillId.setItem(itemDB);
        itemBillId.setBill(billDB);
        itemBillId.setAddedBy(user);

        ItemBill itemBillDB = itemBillService.getItemBill(itemBillId);
        if(itemBillDB != null){
            if(!Utils.loggedUserIsAdmin() && !itemBillDB.getAddedBy().equals(Utils.getLoggedUser().getUsername())){
                statusMessages.addError("You cannot edit someones items");
                return statusMessages;
            }
            int actualQuantity = itemBillDB.getQuantity();
            if(howMuch + actualQuantity <= 0){
                //remove item
                return removeItem(itemName, billId, user);
            }
            itemBillDB.setQuantity(actualQuantity + howMuch);
            itemBillService.updateItemBill(itemBillDB);
        }else{
            statusMessages.addError("Item connection to specified bill was not found.");
        }
        return statusMessages;
    }

    public StatusMessages deleteBill(int billId){
        StatusMessages statusMessages = new StatusMessages();
        Bill bill = getBill(billId);
        if(bill != null){
            for(User user : bill.getUsers()){
                user.removeBill(bill);
                userService.editUser(user);
            }
            itemBillService.deleteItemBillsByBillId(billId);
            billDao.delete(billId);
            statusMessages.addMessage("Bill successfully deleted");
        }else{
            statusMessages.addError("Bill not found.");
        }
        return statusMessages;
    }

    public StatusMessages changeItemsPrice(int billId, String itemName, String user, double newPrice) {
        StatusMessages statusMessages = new StatusMessages();
        Bill billDB = billDao.read(billId);
        if (billDB == null) {
            statusMessages.addError("Bill not found.");
            return statusMessages;
        }

        Item itemDB = itemService.getItem(itemName);
        if(itemDB == null){
            statusMessages.addError("Item not found.");
            return statusMessages;
        }

        ItemBillId itemBillId = new ItemBillId();
        itemBillId.setItem(itemDB);
        itemBillId.setBill(billDB);
        itemBillId.setAddedBy(user);

        ItemBill itemBillDB = itemBillService.getItemBill(itemBillId);
        if(itemBillDB != null){
            if(!Utils.loggedUserIsAdmin() && !itemBillDB.getAddedBy().equals(Utils.getLoggedUser().getUsername())) {
                statusMessages.addError("You cannot edit someones items");
                return statusMessages;
            }
            itemBillDB.setPrice(newPrice);
            itemBillService.updateItemBill(itemBillDB);
        }else{
            statusMessages.addError("Item connection to specified bill was not found.");
        }
        return statusMessages;
    }
}

package cz.matyapav.services;

import cz.matyapav.models.Item;
import cz.matyapav.models.ItemBill;
import cz.matyapav.models.dao.ItemBillDao;
import cz.matyapav.models.dao.ItemBillDaoImpl;
import cz.matyapav.utils.ItemBillId;
import cz.matyapav.utils.StatusMessages;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pavel on 02.01.2017.
 */
public class ItemBillService {

    @Autowired
    private ItemBillDao itemBillDao;

    public ItemBill getItemBill(ItemBillId id){
        return itemBillDao.read(id);
    }

    public StatusMessages updateItemBill(ItemBill itemBill){
        StatusMessages statusMessages = new StatusMessages();
        itemBillDao.update(itemBill);
        statusMessages.addMessage("Item bill connection successfully updated");
        return statusMessages;
    }

    public StatusMessages createItemBill(ItemBill itemBill){
        StatusMessages statusMessages = new StatusMessages();
        itemBillDao.create(itemBill);
        statusMessages.addMessage("Item bill connection successfully updated");
        return statusMessages;
    }

    public StatusMessages deleteItemBill(ItemBillId id){
        StatusMessages statusMessages = new StatusMessages();
        itemBillDao.delete(id);
        statusMessages.addMessage("Item bill connection succesfully delted.");
        return statusMessages;
    }

    public List<ItemBill> getItemBillsByBillId(int billId){
        return itemBillDao.getBillItemsByBillId(billId);
    }

    public StatusMessages deleteItemBillsByBillId(int billId){
        StatusMessages statusMessages = new StatusMessages();
        itemBillDao.deleteBillItemsByBillId(billId);
        statusMessages.addMessage("Bill item connection successfully deleted.");
        return statusMessages;
    }

    public double getBillTotalPrice(int billID){
        return itemBillDao.getBillTotalPrice(billID);
    }


    public HashMap<String, List<ItemBill>> getBillItemsGroupedByUsernames(int billId){
        List<ItemBill> itemBills = getItemBillsByBillId(billId);
        HashMap<String, List<ItemBill>> result = new HashMap<>();
        for (ItemBill ib : itemBills){
            String username = ib.getAddedBy();
            if(!result.containsKey(username)){
                result.put(username, new ArrayList<>());
            }
            result.get(username).add(ib);
        }
        return result;
    }

    public HashMap<String, Double> getParticularPriceSumsGroupedByUsernames(int billId){
        HashMap<String, Double> result = new HashMap<>();
        HashMap<String, List<ItemBill>> groupedBillItems = getBillItemsGroupedByUsernames(billId);
        for(Map.Entry<String, List<ItemBill>> groupedBillItemsRecord : groupedBillItems.entrySet()){
            String username = groupedBillItemsRecord.getKey();
            double sum = 0;
            for(ItemBill itemBill : groupedBillItemsRecord.getValue()){
                sum += itemBill.getPrice()*itemBill.getQuantity();
            }
            result.put(username, sum);
        }
        return result;
    }
}

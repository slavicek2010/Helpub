package cz.matyapav.models.dao;

import cz.matyapav.models.ItemBill;
import cz.matyapav.utils.ItemBillId;

import java.util.List;

/**
 * Created by Pavel on 02.01.2017.
 */
public interface ItemBillDao extends GenericDao<ItemBill,ItemBillId> {

    List<ItemBill> getBillItemsByBillId(int billId);

    void deleteBillItemsByBillId(int billId);

    double getBillTotalPrice(int billId);

}

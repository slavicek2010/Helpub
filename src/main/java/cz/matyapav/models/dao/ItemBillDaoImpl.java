package cz.matyapav.models.dao;

import cz.matyapav.models.Item;
import cz.matyapav.models.ItemBill;
import cz.matyapav.utils.ItemBillId;
import cz.matyapav.utils.StatusMessages;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Pavel on 02.01.2017.
 */
public class ItemBillDaoImpl extends GenericDaoImpl<ItemBill, ItemBillId> implements ItemBillDao{

    public ItemBillDaoImpl() {
        super(ItemBill.class);
    }

    @Override
    public List<ItemBill> getBillItemsByBillId(int billId){
        String hql = "select ib from ItemBill ib where ib.id.bill.id = (:billId)";
        Query query = entityManager.createQuery(hql);
        query.setParameter("billId", billId);
        List<ItemBill> billItems = query.getResultList();
        return billItems;
    }

    @Override
    public void deleteBillItemsByBillId(int billId){
        entityManager.createQuery("DELETE from ItemBill ib where ib.id.bill.id = (:bill_id)").setParameter("bill_id", billId).executeUpdate();
    }

    @Override
    public double getBillTotalPrice(int billId) {
        String hql = "SELECT sum(ib.price*ib.quantity) FROM ItemBill ib WHERE ib.id.bill.id = (:bill_id)";
        TypedQuery<Double> query = entityManager.createQuery(hql, Double.class);
        query.setParameter("bill_id", billId);
        try{
            return query.getSingleResult();
        }catch (Exception e){
            return 0;
        }
    }


}

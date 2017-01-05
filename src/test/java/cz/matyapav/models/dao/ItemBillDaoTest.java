package cz.matyapav.models.dao;

import cz.matyapav.config.AppConfig;
import cz.matyapav.models.Bill;
import cz.matyapav.models.Item;
import cz.matyapav.models.ItemBill;
import cz.matyapav.models.enums.ItemTypes;
import cz.matyapav.utils.ItemBillId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pavel on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
@WebAppConfiguration
@Transactional
public class ItemBillDaoTest {

    @Autowired
    ItemBillDao itemBillDao;

    @Autowired
    GenericDao<Bill, Integer> billDao;

    @Autowired
    GenericDao<Item, String> itemDao;

    @Test
    public void testCreateAndReadPositive() throws Exception {
        Bill bill = createBill();
        Item item = createItem();

        ItemBill itemBill = new ItemBill();
        itemBill.setBill(bill);
        itemBill.setItem(item);
        itemBill.setAddedBy("matyapav");

        itemBill.setPrice(20);
        itemBill.setQuantity(1);

        itemBillDao.create(itemBill);
        Assert.assertNotNull(itemBillDao.read(itemBill.getPrimaryKey()));
    }

    @Test(expected = JpaSystemException.class)
    public void testCreateNegativeAddedByIsNull() throws Exception{
        Bill bill = createBill();
        Item item = createItem();

        ItemBill itemBill = new ItemBill();
        itemBill.setBill(bill);
        itemBill.setItem(item);
        itemBill.setAddedBy(null);

        itemBill.setPrice(20);
        itemBill.setQuantity(40);

        itemBillDao.create(itemBill);

        TestTransaction.flagForCommit();
        TestTransaction.end();

    }

    @Test
    public void testUpdatePositive(){
        //create bill
        Bill bill = createBill();
        Item item = createItem();

        ItemBill itemBill = new ItemBill();
        itemBill.setBill(bill);
        itemBill.setItem(item);
        itemBill.setAddedBy("matyapav");
        itemBill.setPrice(20);
        itemBill.setQuantity(20);

        itemBillDao.create(itemBill);

        itemBill.setPrice(500);
        itemBill.setQuantity(22000);
        itemBillDao.update(itemBill);
        //read updated bill
        ItemBill itemBillUpdated = itemBillDao.read(itemBill.getPrimaryKey());
        //compare
        Assert.assertEquals(itemBillUpdated, itemBill);
    }

    @Test
    public void testRemovePositive(){
        //create itembill
        Bill bill = createBill();
        Item item = createItem();

        ItemBill itemBill = new ItemBill();
        itemBill.setBill(bill);
        itemBill.setItem(item);
        itemBill.setAddedBy("matyapav");
        itemBill.setPrice(20);
        itemBill.setQuantity(20);

        itemBillDao.create(itemBill);

        Assert.assertTrue(itemBillDao.delete(itemBill.getPrimaryKey()));
        Assert.assertNull(itemBillDao.read(itemBill.getPrimaryKey()));
    }

    @Test
    public void testRemoveNegativeNonExistingId(){
        Assert.assertFalse(itemBillDao.delete(new ItemBillId()));
    }

    @Test
    public void testUpdateNegativeItemBillIsNull(){
        ItemBill itemBill = null;
        itemBillDao.update(itemBill);
    }

    @Test
    public void testCreateNegativeItemBillIsNull(){
        ItemBill itemBill = null;
        itemBillDao.create(itemBill);
    }

    @Test
    public void testReadNegativeItemBillIdIsNull(){
        ItemBillId i = null;
        itemBillDao.read(i);
    }

    @Test
    public void testDeleteNegativeItemBillIdIsNull(){
        ItemBillId i = null;
        itemBillDao.delete(i);
    }

    @Test
    public void getBillItemsByBillId() throws Exception {
        //create itembill
        Bill bill = createBill();
        Item item = createItem();

        ItemBill itemBill = new ItemBill();
        itemBill.setBill(bill);
        itemBill.setItem(item);
        itemBill.setAddedBy("matyapav");
        itemBill.setPrice(20);
        itemBill.setQuantity(20);
        itemBillDao.create(itemBill);

        List<ItemBill> items = itemBillDao.getBillItemsByBillId(bill.getId());
        assertTrue(items.contains(itemBill));
    }

    @Test
    public void getBillItemsByBillIdNegativeBillNotFound() throws Exception {
        List<ItemBill> items = itemBillDao.getBillItemsByBillId(0);
        assertEquals(items.size(), 0);
    }

    @Test
    public void getBillItemsByBillIdNegativeBillIdNull() throws Exception {
        Integer i = null;
        itemBillDao.getBillItemsByBillId(i);
    }

    @Test
    public void deleteBillItemsByBillId() throws Exception {
        //create itembill
        Bill bill = createBill();
        Item item = createItem();

        ItemBill itemBill = new ItemBill();
        itemBill.setBill(bill);
        itemBill.setItem(item);
        itemBill.setAddedBy("matyapav");
        itemBill.setPrice(20);
        itemBill.setQuantity(20);
        itemBillDao.create(itemBill);

        List<ItemBill> itemsBills = itemBillDao.getBillItemsByBillId(bill.getId());
        assertEquals(1, itemsBills.size());
        itemBillDao.deleteBillItemsByBillId(bill.getId());
        List<ItemBill> items = itemBillDao.getBillItemsByBillId(bill.getId());
        assertTrue(items.isEmpty());
    }

    @Test
    public void getBillTotalPrice() throws Exception {
        //create itembill
        Bill bill = createBill();
        Item item = createItem();

        ItemBill itemBill = new ItemBill();
        itemBill.setBill(bill);
        itemBill.setItem(item);
        itemBill.setAddedBy("matyapav");
        itemBill.setPrice(20);
        itemBill.setQuantity(20);
        itemBillDao.create(itemBill);

        double price = itemBillDao.getBillTotalPrice(bill.getId());
        assertEquals(price, 20, 1e-15);
    }

    @Test
    public void getBillTotalPriceNegativeBillIdNull() throws Exception {
        Integer i = null;
        itemBillDao.getBillItemsByBillId(i);
    }

    private Bill createBill(){
        Bill bill = new Bill();
        bill.setName("testBill");
        bill.setOpened(true);
        bill.setCreatorUsername("testUser");
        bill.setLocked(true);
        bill.setPassword("password");
        billDao.create(bill);
        return bill;
    }

    private Item createItem(){
        Item item = new Item();
        item.setName("item");
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);
        return item;
    }

}
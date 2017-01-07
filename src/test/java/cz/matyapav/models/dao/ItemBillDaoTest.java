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
 * This class tests {@link ItemBillDao}
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

    /**
     * Tests positive create and read - everything goes well
     * ItemBill is stored in database and can be read
     * @throws Exception
     */
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

    /**
     * Test negative create - part of primaryKey (added_by) is null
     * ItemBill is no stored in database and {@link JpaSystemException} is expected
     * @throws Exception
     */
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

    /**
     * Tests positive update - everything goes well
     * ItemBill is successfully updated and next read returns updated values
     * @throws Exception
     */
    @Test
    public void testUpdatePositive() throws Exception{
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

    /**
     * Tests positive remove - everything goes well
     * ItemBill is removed, remove returns true and next read returns null
     * @throws Exception
     */
    @Test
    public void testRemovePositive() throws Exception{
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

    /**
     * Tests negative remove - itembill with desired id does not exist in db
     * Nothing is deleted and remove operation returns false
     * @throws Exception
     */
    @Test
    public void testRemoveNegativeNonExistingId() throws Exception {
        Assert.assertFalse(itemBillDao.delete(new ItemBillId()));
    }

    /**
     * Tests negative update - itembill is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testUpdateNegativeItemBillIsNull() throws Exception{
        ItemBill itemBill = null;
        itemBillDao.update(itemBill);
    }

    /**
     * Tests negative create - itembill is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testCreateNegativeItemBillIsNull() throws Exception {
        ItemBill itemBill = null;
        itemBillDao.create(itemBill);
    }

    /**
     * Tests negative read - itembill id is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testReadNegativeItemBillIdIsNull() throws Exception {
        ItemBillId i = null;
        itemBillDao.read(i);
    }

    /**
     * Tests negative delete - itembill id is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testDeleteNegativeItemBillIdIsNull() throws Exception{
        ItemBillId i = null;
        itemBillDao.delete(i);
    }

    /**
     * Tests positive get bill items by bill id - everything goes well
     * ItemBills connected with specified bill are returned
     * @throws Exception
     */
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

    /**
     * Tests negative get bill items by bill id - bill with specified id not found
     * Should return empty collection
     * @throws Exception
     */
    @Test
    public void getBillItemsByBillIdNegativeBillNotFound() throws Exception {
        List<ItemBill> items = itemBillDao.getBillItemsByBillId(0);
        assertEquals(items.size(), 0);
    }

    /**
     * Tests negative get bill items by bill id - bill id is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void getBillItemsByBillIdNegativeBillIdNull() throws Exception {
        Integer i = null;
        itemBillDao.getBillItemsByBillId(i);
    }

    /**
     * Tests positive delete Item bills by bill id - everything goes well
     * All ItemBills with specified bill id are removed
     * @throws Exception
     */
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

    /**
     * Tests negative delete item bills by specified bill id - bill id is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void deleteBillItemsByBillIdBillIdNull() throws Exception {
        Integer i = null;
        itemBillDao.deleteBillItemsByBillId(i);
    }

    /**
     * Test positive get bill total price - everything goes well
     * Total bill price should be returned
     * @throws Exception
     */
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
        assertEquals(price, 400, 1e-15);
    }

    /**
     * Test negative get total bill price - bill id is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void getBillTotalPriceNegativeBillIdNull() throws Exception {
        Integer i = null;
        itemBillDao.getBillItemsByBillId(i);
    }

    /**
     * Creates bill in db
     * @return created bill
     */
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

    /**
     * Creates item in db
     * @return created item
     */
    private Item createItem(){
        Item item = new Item();
        item.setName("item");
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);
        return item;
    }

}
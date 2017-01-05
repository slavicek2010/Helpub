package cz.matyapav.models.dao;

import cz.matyapav.config.AppConfig;
import cz.matyapav.models.Bill;
import cz.matyapav.models.Item;
import cz.matyapav.models.enums.ItemTypes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;

/**
 * Created by Pavel on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
@WebAppConfiguration
@Transactional
public class ItemDaoTest {

    @Autowired
    GenericDao<Item, String> itemDao;

    @Test
    public void testCreateAndReadPositive() throws Exception{
        Item item = new Item();
        item.setName("item");
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);
        Assert.assertNotNull(itemDao.read(item.getName()));
    }

    @Test(expected = PersistenceException.class)
    public void testCreateNegativeNameIsNull() throws Exception{
        Item item = new Item();
        item.setName(null);
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test(expected = EntityExistsException.class)
    public void testCreateNegativeItemNameExists() throws Exception{
        Item item = new Item();
        item.setName("item");
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);

        Item item2 = new Item();
        item2.setName("item");
        item2.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item2);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    public void testUpdatePositive(){
        Item item = new Item();
        item.setName("item");
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);

        item.setType(ItemTypes.NON_ALCOHOLIC_BEVERAGE);
        itemDao.update(item);

        Item itemDB = itemDao.read(item.getName());
        Assert.assertEquals(itemDB, item);
    }

    @Test
    public void testRemovePositive(){
        Item item = new Item();
        item.setName("item");
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);

        Assert.assertTrue(itemDao.delete(item.getName()));
        Assert.assertNull(itemDao.read(item.getName()));
    }

    @Test
    public void testRemoveNegativeNonExistingId(){
        Assert.assertFalse(itemDao.delete("non existing item"));
    }

    @Test
    public void testUpdateNegativeItemIsNull(){
        Item item = null;
        itemDao.update(item);
    }

    @Test
    public void testCreateNegativeItemIsNull(){
        Item item = null;
        itemDao.create(item);
    }

    @Test
    public void testReadNegativeItemIsNull(){
        String name = null;
        itemDao.read(name);
    }

    @Test
    public void testDeleteNegativeBillIsNull(){
        String name = null;
        itemDao.delete(name);
    }

}

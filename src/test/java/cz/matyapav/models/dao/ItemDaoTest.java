package cz.matyapav.models.dao;

import cz.matyapav.config.AppConfig;
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
 * This class tests ItemDao
 * Created by Pavel on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
@WebAppConfiguration
@Transactional
public class ItemDaoTest {

    @Autowired
    GenericDao<Item, String> itemDao;

    /**
     * Tests positive create and read - everything goes well
     * Item is stored in db and can be read
     * @throws Exception
     */
    @Test
    public void testCreateAndReadPositive() throws Exception{
        Item item = new Item();
        item.setName("item");
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);
        Assert.assertNotNull(itemDao.read(item.getName()));
    }

    /**
     * Tests negative create - name is null
     * Item is not stored in db and {@link PersistenceException} is expected
     * @throws Exception
     */
    @Test(expected = PersistenceException.class)
    public void testCreateNegativeNameIsNull() throws Exception{
        Item item = new Item();
        item.setName(null);
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    /**
     * Tests uniqueness of item name in db
     * Item is not stored nad {@link EntityExistsException} should be expected
     * @throws Exception
     */
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

    /**
     * Tests positive update - everything goes well
     * Item is successfully updated and next read returns updated values
     * @throws Exception
     */
    @Test
    public void testUpdatePositive() throws Exception{
        Item item = new Item();
        item.setName("item");
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);

        item.setType(ItemTypes.NON_ALCOHOLIC_BEVERAGE);
        itemDao.update(item);

        Item itemDB = itemDao.read(item.getName());
        Assert.assertEquals(itemDB, item);
    }

    /**
     * Tests positive remove - everything goes well
     * Item is removed from db, remove returns true and next read returns null
     * @throws Exception
     */
    @Test
    public void testRemovePositive() throws Exception{
        Item item = new Item();
        item.setName("item");
        item.setType(ItemTypes.ALCOHOLIC_BEVERAGE);
        itemDao.create(item);

        Assert.assertTrue(itemDao.delete(item.getName()));
        Assert.assertNull(itemDao.read(item.getName()));
    }

    /**
     * Tests negative remove - item with specified id is not in fb
     * nothing is removed amd remove returns false
     * @throws Exception
     */
    @Test
    public void testRemoveNegativeNonExistingId() throws Exception{
        Assert.assertFalse(itemDao.delete("non existing item"));
    }

    /**
     * Tests negative update - item is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testUpdateNegativeItemIsNull() throws Exception{
        Item item = null;
        itemDao.update(item);
    }


    /**
     * Tests negative create - item is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testCreateNegativeItemIsNull() throws Exception{
        Item item = null;
        itemDao.create(item);
    }


    /**
     * Tests negative read - item name is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testReadNegativeItemIsNull() throws Exception{
        String name = null;
        itemDao.read(name);
    }


    /**
     * Tests negative delete - tem name is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testDeleteNegativeBillIsNull() throws Exception{
        String name = null;
        itemDao.delete(name);
    }

}

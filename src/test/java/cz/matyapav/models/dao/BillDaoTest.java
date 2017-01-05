package cz.matyapav.models.dao;

import cz.matyapav.config.AppConfig;
import cz.matyapav.models.Bill;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
/**
 * This class tests BillDao
 * Created by Pavel on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
@WebAppConfiguration
@Transactional
public class BillDaoTest {

    @Autowired
    GenericDao<Bill, Integer> billDao;

    /**
     * Tests positive create and read- everything goes well
     * Bill is stored in database and can be read
     * @throws Exception
     */
    @Test
    public void testCreateAndReadPositive() throws Exception{
        Bill bill = new Bill();
        bill.setName("testBill");
        bill.setOpened(true);
        bill.setCreatorUsername("testUser");
        bill.setLocked(true);
        bill.setPassword("password");
        Bill billdb = billDao.create(bill);
        Assert.assertNotNull(billDao.read(billdb.getId()));
    }

    /**
     * Tests negative create - billname is null
     * Bill is not stored in database and {@link PersistenceException} is expected
     * @throws Exception
     */
    @Test(expected = PersistenceException.class)
    public void testCreateNegativeNameIsNull() throws Exception{
        Bill bill = new Bill();
        bill.setName(null);
        bill.setOpened(true);
        bill.setCreatorUsername("testUser");
        bill.setLocked(true);
        bill.setPassword("p");
        billDao.create(bill);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    /**
     * Tests negative create - creator username is null
     * Bill is not stored in database and {@link PersistenceException } is expected
     * @throws Exception
     */
    @Test(expected = PersistenceException.class)
    public void testCreateNegativeCreatorUsernameIsNull() throws Exception{
        Bill bill = new Bill();
        bill.setName("bill");
        bill.setCreatorUsername(null);
        bill.setLocked(true);
        bill.setPassword("p");
        billDao.create(bill);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    /**
     * Tests positive update - everything goes well
     * Bill is successfully updated and next read reads updated values
     * @throws Exception
     */
    @Test
    public void testUpdatePositive() throws Exception{
        //create bill
        Bill bill = new Bill();
        bill.setName("testBill");
        bill.setOpened(true);
        bill.setCreatorUsername("testUser");
        bill.setLocked(true);
        bill.setPassword("password");
        Bill billdb = billDao.create(bill);

        //update bill
        billdb.setName("testBillUpdated");
        billdb.setOpened(false);
        billdb.setLocked(false);
        billdb.setCreatorUsername("testUserUpdated");
        billdb.setPassword("passwordUpdated");
        billDao.update(billdb);
        //read updated bill
        Bill billDbUpdated = billDao.read(billdb.getId());
        //compare
        Assert.assertEquals(billDbUpdated, billdb);
    }

    /**
     * Tests positive remove - everything goes well
     * Bill is removed, remove returns true and next read returns null
     * @throws Exception
     */
    @Test
    public void testRemovePositive() throws Exception{
        //create bill
        Bill bill = new Bill();
        bill.setName("testBill");
        bill.setOpened(true);
        bill.setCreatorUsername("testUser");
        bill.setLocked(true);
        bill.setPassword("password");
        Bill billdb = billDao.create(bill);

        Assert.assertTrue(billDao.delete(billdb.getId()));
        Assert.assertNull(billDao.read(billdb.getId()));
    }

    /**
     * Tests negative remove - desired bill is not in db
     * Nothing is removed and remove returns false
     * @throws Exception
     */
    @Test
    public void testRemoveNegativeNonExistingId() throws Exception{
        Assert.assertFalse(billDao.delete(0));
    }

    /**
     * Tests negative update - bill is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testUpdateNegativeBillIsNull() throws Exception{
        Bill bill = null;
        billDao.update(bill);
    }

    /**
     * Tests negative create - bill is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testCreateNegativeBillIsNull() throws Exception{
        Bill bill = null;
        billDao.create(bill);
    }

    /**
     * Tests negative read - bill id is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testReadNegativeBillIdIsNull() throws Exception{
        Integer i = null;
        billDao.read(i);
    }

    /**
     * Tests negative delete - bill id is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testDeleteNegativeBillIdIsNull() throws Exception{
        Integer i = null;
        billDao.delete(i);
    }
}

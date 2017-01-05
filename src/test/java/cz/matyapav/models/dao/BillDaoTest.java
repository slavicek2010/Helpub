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
 * Created by Pavel on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
@WebAppConfiguration
@Transactional
public class BillDaoTest {

    @Autowired
    GenericDao<Bill, Integer> billDao;

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

    @Test
    public void testUpdatePositive(){
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

    @Test
    public void testRemovePositive(){
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

    @Test
    public void testRemoveNegativeNonExistingId(){
        Assert.assertFalse(billDao.delete(0));
    }

    @Test
    public void testUpdateNegativeBillIsNull(){
        Bill bill = null;
        billDao.update(bill);
    }

    @Test
    public void testCreateNegativeBillIsNull(){
        Bill bill = null;
        billDao.create(bill);
    }

    @Test
    public void testReadNegativeBillIdIsNull(){
        Integer i = null;
        billDao.read(i);
    }

    @Test
    public void testDeleteNegativeBillIdIsNull(){
        Integer i = null;
        billDao.delete(i);
    }
}

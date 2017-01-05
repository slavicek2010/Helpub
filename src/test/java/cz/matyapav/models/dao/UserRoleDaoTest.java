package cz.matyapav.models.dao;

import cz.matyapav.config.AppConfig;
import cz.matyapav.models.Item;
import cz.matyapav.models.User;
import cz.matyapav.models.UserRole;
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
public class UserRoleDaoTest {

    @Autowired
    GenericDao<UserRole, String> userRoleDao;

    @Test
    public void testCreateAndReadPositive() throws Exception{
        UserRole role = new UserRole();
        role.setRole("ROLE_USER");
        userRoleDao.create(role);
        Assert.assertNotNull(userRoleDao.read(role.getRole()));
    }

    @Test(expected = PersistenceException.class)
    public void testCreateNegativeRoleNameIsNull() throws Exception{
        UserRole role = new UserRole();
        role.setRole(null);
        userRoleDao.create(role);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test(expected = EntityExistsException.class)
    public void testCreateNegativeRoleNameExists() throws Exception{
        UserRole role = new UserRole();
        role.setRole("ROLE_USER");
        userRoleDao.create(role);

        UserRole role2 = new UserRole();
        role2.setRole("ROLE_USER");
        userRoleDao.create(role2);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    public void testUpdatePositive(){
        UserRole role = new UserRole();
        role.setRole("ROLE_USER");
        userRoleDao.create(role);

        role.addUser(new User());

        userRoleDao.update(role);

        UserRole roleDB = userRoleDao.read(role.getRole());
        Assert.assertEquals(roleDB, role);
    }

    @Test
    public void testRemovePositive(){
        UserRole role = new UserRole();
        role.setRole("ROLE_USER");
        userRoleDao.create(role);

        Assert.assertTrue(userRoleDao.delete(role.getRole()));
        Assert.assertNull(userRoleDao.read(role.getRole()));
    }

    @Test
    public void testRemoveNegativeNonExistingId(){
        Assert.assertFalse(userRoleDao.delete("non existing role"));
    }

    @Test
    public void testUpdateNegativeRoleIsNull(){
        UserRole role = null;
        userRoleDao.update(role);
    }

    @Test
    public void testCreateNegativeRoleIsNull(){
        UserRole role = null;
        userRoleDao.create(role);
    }

    @Test
    public void testReadNegativeItemIsNull(){
        String name = null;
        userRoleDao.read(name);
    }

    @Test
    public void testDeleteNegativeBillIsNull(){
        String name = null;
        userRoleDao.delete(name);
    }

}

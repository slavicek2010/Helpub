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
 * This class tests UserRoleDao
 * Created by Pavel on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
@WebAppConfiguration
@Transactional
public class UserRoleDaoTest {

    @Autowired
    GenericDao<UserRole, String> userRoleDao;

    /**
     * Tests positive create and read - everything goes well
     * UserRole is stored in db and can be read
     * @throws Exception
     */
    @Test
    public void testCreateAndReadPositive() throws Exception{
        UserRole role = new UserRole();
        role.setRole("ROLE_USER");
        userRoleDao.create(role);
        Assert.assertNotNull(userRoleDao.read(role.getRole()));
    }

    /**
     * Tests negative create - role name is null
     * UserRole is not stored in db and {@link PersistenceException} is expected
     * @throws Exception
     */
    @Test(expected = PersistenceException.class)
    public void testCreateNegativeRoleNameIsNull() throws Exception{
        UserRole role = new UserRole();
        role.setRole(null);
        userRoleDao.create(role);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    /**
     * Tests uniqueness of role name - role name already exists in db
     * UserRole is not stored in gb and {@link EntityExistsException} is expected
     * @throws Exception
     */
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

    /**
     * Tests positive update - everything goes well
     * UserRole is successfully updated and nex read returns updated values
     * @throws Exception
     */
    @Test
    public void testUpdatePositive() throws Exception {
        UserRole role = new UserRole();
        role.setRole("ROLE_USER");
        userRoleDao.create(role);

        role.addUser(new User());

        userRoleDao.update(role);

        UserRole roleDB = userRoleDao.read(role.getRole());
        Assert.assertEquals(roleDB, role);
    }

    /**
     * Tests positive remove - everything goes well
     * UserRole is removed, remove returns true and next read returns null
     * @throws Exception
     */
    @Test
    public void testRemovePositive() throws Exception {
        UserRole role = new UserRole();
        role.setRole("ROLE_USER");
        userRoleDao.create(role);

        Assert.assertTrue(userRoleDao.delete(role.getRole()));
        Assert.assertNull(userRoleDao.read(role.getRole()));
    }

    /**
     * Tests negative remove - role with specified name does not exist in db
     * Nothing is removed and remove returns false
     * @throws Exception
     */
    @Test
    public void testRemoveNegativeNonExistingId() throws Exception {
        Assert.assertFalse(userRoleDao.delete("non existing role"));
    }

    /**
     * Tests negative update - user role is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testUpdateNegativeRoleIsNull() throws Exception{
        UserRole role = null;
        userRoleDao.update(role);
    }

    /**
     * Tests negative create - user role is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testCreateNegativeRoleIsNull() throws Exception {
        UserRole role = null;
        userRoleDao.create(role);
    }

    /**
     * Tests negative read - user role name is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testReadNegativeItemIsNull() throws Exception {
        String name = null;
        userRoleDao.read(name);
    }

    /**
     * Tests negative delete - user role name is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testDeleteNegativeBillIsNull() throws Exception {
        String name = null;
        userRoleDao.delete(name);
    }

}

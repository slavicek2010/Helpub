package cz.matyapav.models.dao;

import cz.matyapav.config.AppConfig;
import cz.matyapav.models.User;
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

import javax.persistence.EntityExistsException;

/**
 * This class tests UserDao
 * Created by Pavel on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
@WebAppConfiguration
@Transactional
public class UserDaoTest {

    @Autowired
    GenericDao<User, String> userDao;

    /**
     * Tests positive create and read - everything goes well
     * USer is stored in db and can be read
     * @throws Exception
     */
    @Test
    public void testCreateAndReadPositive() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        user.setPassword("password");
        user.setEnabled(1);
        userDao.create(user);
        Assert.assertNotNull(userDao.read(user.getUsername()));
    }

    /**
     * Tests negative create - username already exists in db
     * User is not stored and {@link EntityExistsException} is expected
     * @throws Exception
     */
    @Test(expected = EntityExistsException.class)
    public void testCreateNegativeUsernameExists() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        user.setPassword("password");
        user.setEnabled(1);
        userDao.create(user);

        User user2 = new User();
        user2.setUsername("matyapav");
        user2.setFirstName("Jiny");
        user2.setLastName("Clovek");
        user2.setPassword("se stejnou prezdivkou");
        user2.setEnabled(1);
        userDao.create(user2);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    /**
     * Tests negative create - password is too long (over 60 chars)
     * User in not stored in db and {@link JpaSystemException} is expected
     * @throws Exception
     */
    @Test(expected = JpaSystemException.class)
    public void testCreateNegativePasswordTooLong() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        user.setPassword("atyasijiqjioehwqoiejiowqhjewqhfhjikwebfjwebfvjiedqbvjqedbvujqeobvqejvbjqebfjieqbvjieqbhkjqebvhkjeqbvhjkbeqkhvbqehkvbqekfbqeiodhoiejdioeqhfuioeqhdoiqhjdouiwqhfuoqehfiouweqbf");
        user.setEnabled(1);
        userDao.create(user);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    /**
     * Tests negative create - username is too long (over 40 chars)
     * User is not stored in db and {@link JpaSystemException} is expected
     * @throws Exception
     */
    @Test(expected = JpaSystemException.class)
    public void testCreateNegativeUsernameTooLong() throws Exception{
        User user = new User();
        user.setUsername("atyasijiqjioehwqoiejiowqhjewqhfhjikwebfjwebfvjiedqbvjqedbvujqeobvqejvbjqebfjieqbvjieqbhkjqebvhkjeqbvhjkbeqkhvbqehkvbqekfbqeiodhoiejdioeqhfuioeqhdoiqhjdouiwqhfuoqehfiouweqbf");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        user.setPassword("password");
        user.setEnabled(1);
        userDao.create(user);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    /**
     * Tests negative create - firstname is too long (over 80 chars)
     * User is not stored in db and {@link JpaSystemException} is expected
     * @throws Exception
     */
    @Test(expected = JpaSystemException.class)
    public void testCreateNegativeFirstNameTooLong() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("atyasijiqjioehwqoiejiowqhjewqhfhjikwebfjwebfvjiedqbvjqedbvujqeobvqejvbjqebfjieqbvjieqbhkjqebvhkjeqbvhjkbeqkhvbqehkvbqekfbqeiodhoiejdioeqhfuioeqhdoiqhjdouiwqhfuoqehfiouweqbf");
        user.setLastName("Matyas");
        user.setPassword("password");
        user.setEnabled(1);
        userDao.create(user);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    /**
     * Tests negative create - lastname is too long (over 80 chars)
     * User is not stored in db and {@link JpaSystemException} is expected
     * @throws Exception
     */
    @Test(expected = JpaSystemException.class)
    public void testCreateNegativeLastNameTooLong() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyasijiqjioehwqoiejiowqhjewqhfhjikwebfjwebfvjiedqbvjqedbvujqeobvqejvbjqebfjieqbvjieqbhkjqebvhkjeqbvhjkbeqkhvbqehkvbqekfbqeiodhoiejdioeqhfuioeqhdoiqhjdouiwqhfuoqehfiouweqbf");
        user.setPassword("password");
        user.setEnabled(1);
        userDao.create(user);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    /**
     * Tests positive update - everything goes well
     * User is successfully updated and next read return updated  values
     * @throws Exception
     */
    @Test
    public void testUpdatePositive() throws Exception {
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        user.setPassword("password");
        user.setEnabled(1);
        userDao.create(user);

        user.setFirstName("Jirka");
        user.setLastName("Matyasu");
        user.setPassword("newpassword");
        user.setEnabled(0);
        userDao.update(user);

        User userDB = userDao.read(user.getUsername());
        Assert.assertEquals(userDB, user);
    }

    /**
     * Tests positive remove - everything goes well
     * User is removed, remove returns true and next read returns null
     * @throws Exception
     */
    @Test
    public void testRemovePositive() throws Exception {
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        user.setPassword("password");
        user.setEnabled(1);
        userDao.create(user);

        Assert.assertTrue(userDao.delete(user.getUsername()));
        Assert.assertNull(userDao.read(user.getUsername()));
    }

    /**
     * Tests negative remove - user with specified username does not exist in db
     * Nothing is removed and remove operation returns false
     * @throws Exception
     */
    @Test
    public void testRemoveNegativeNonExistingId() throws Exception {
        Assert.assertFalse(userDao.delete("non existing user"));
    }

    /**
     * Tests negative update - user is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testUpdateNegativeUserIsNull() throws Exception {
        User user = null;
        userDao.update(user);
    }

    /**
     * Tests negative create - user is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testCreateNegativeUserIsNull() throws Exception {
        User user = null;
        userDao.create(user);
    }

    /**
     * Tests negative read - username is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testReadNegativeUserIdIsNull() throws Exception {
        String name = null;
        userDao.read(name);
    }

    /**
     * Tests negative delete - username is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testDeleteNegativeUserIdIsNull() throws Exception {
        String name = null;
        userDao.delete(name);
    }

}

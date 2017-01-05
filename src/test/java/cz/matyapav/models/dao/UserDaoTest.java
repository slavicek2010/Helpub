package cz.matyapav.models.dao;

import cz.matyapav.config.AppConfig;
import cz.matyapav.models.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Pavel on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
@WebAppConfiguration
@Transactional
public class UserDaoTest {



    @Autowired
    GenericDao<User, String> userDao;

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

    @Test
    public void testUpdatePositive(){
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

    @Test
    public void testRemovePositive(){
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

    @Test
    public void testRemoveNegativeNonExistingId(){
        Assert.assertFalse(userDao.delete("non existing user"));
    }

    @Test
    public void testUpdateNegativeUserIsNull(){
        User user = null;
        userDao.update(user);
    }

    @Test
    public void testCreateNegativeUserIsNull(){
        User user = null;
        userDao.create(user);
    }

    @Test
    public void testReadNegativeUserIdIsNull(){
        String name = null;
        userDao.read(name);
    }

    @Test
    public void testDeleteNegativeUserIdIsNull(){
        String name = null;
        userDao.delete(name);
    }

}

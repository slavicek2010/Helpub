package cz.matyapav.models.dao;

import cz.matyapav.config.AppConfig;
import cz.matyapav.models.User;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Pavel on 05.01.2017.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(classes={AppConfig.class})
@WebAppConfiguration
@Transactional
public class UserDaoTestNullValuesParametrized {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    GenericDao<User, String> userDao;

    @Parameterized.Parameter(0)
    public String username;
    @Parameterized.Parameter(1)
    public String firstName;
    @Parameterized.Parameter(2)
    public String lastName;
    @Parameterized.Parameter(3)
    public String password;
    @Parameterized.Parameter(4)
    public int enabled;

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][] {
                { null, "Pavel", "Matyas", "password", 1 },
                { "Pavel", null, "Matyas", "password", 1 },
                { "matyapav", "Pavel", null, "password", 1  },
                { "matyapav", "Pavel", "Matyas", null, 1  },
        });
    }

    @Test(expected = PersistenceException.class)
    public void testCreateNegativeWithNullValues() throws Exception{
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setEnabled(enabled);
        userDao.create(user);

        TestTransaction.flagForCommit();
        TestTransaction.end();
    }
}

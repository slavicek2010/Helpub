package cz.matyapav.config;

import cz.matyapav.models.*;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.models.dao.GenericDaoImpl;
import cz.matyapav.models.dao.ItemBillDaoImpl;
import cz.matyapav.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by Pavel on 21.12.2016.
 */
@Configuration
@EnableTransactionManagement
public class JPAConfig {

    @Bean(name="dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/helpub");
        dataSource.setUsername("root");
        dataSource.setPassword("Sk4t3b04rd1ng");

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setDataSource(getDataSource());
        lcemfb.setPersistenceUnitName("localContainerEntity");
        LoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        lcemfb.setLoadTimeWeaver(loadTimeWeaver);
        return lcemfb;
    }

    @Bean
    public JpaTransactionManager jpaTransMan() {
        JpaTransactionManager jtManager = new JpaTransactionManager(
                getEntityManagerFactoryBean().getObject());
        return jtManager;
    }

    @Bean(name = "userDao")
    public GenericDaoImpl<User, String> getUserDao() {
        return new GenericDaoImpl<>(User.class);
    }

    @Bean(name = "userService")
    public UserService getUserService(){
        return new UserService();
    }

    @Bean(name = "userRoleDao")
    public GenericDaoImpl<UserRole, String> getUserRoleDao() {
        return new GenericDaoImpl<>(UserRole.class);
    }

    @Bean(name = "userRoleService")
    public UserRoleService getUserRoleService(){
        return new UserRoleService();
    }

    @Bean(name = "billDao")
    public GenericDao<Bill, Integer> getBillDao() {
        return new GenericDaoImpl<>(Bill.class);
    }

    @Bean(name = "billService")
    public BillService getBillService(){
        return new BillService();
    }

    @Bean(name = "itemDao")
    public GenericDaoImpl<Item, String> getItemDao() {
        return new GenericDaoImpl<>(Item.class);
    }

    @Bean(name = "itemService")
    public ItemService getItemService(){
        return new ItemService();
    }

    @Bean(name = "itemBillDao")
    public ItemBillDaoImpl getItemBillDao(){
        return new ItemBillDaoImpl();
    }

    @Bean(name = "itemBillService")
    public ItemBillService getItemBillService(){
        return new ItemBillService();
    }
}

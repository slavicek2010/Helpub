package cz.matyapav.services;

import cz.matyapav.models.User;
import cz.matyapav.models.UserRole;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.models.enums.Roles;
import cz.matyapav.utils.StatusMessages;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Pavel on 02.01.2017.
 */
public class UserRoleService {

    @Autowired
    GenericDao<UserRole, String> userRoleDao;

    public UserRole getRole(String roleName){
        return userRoleDao.read(roleName);
    }

    public StatusMessages createRole(UserRole role){
        StatusMessages statusMessages = new StatusMessages();
        userRoleDao.create(role);
        statusMessages.addMessage("Role successfully created");
        return statusMessages;
    }
}

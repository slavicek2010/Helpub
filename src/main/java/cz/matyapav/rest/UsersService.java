package cz.matyapav.rest;

import cz.matyapav.models.User;
import cz.matyapav.models.dao.GenericDao;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created by Pavel on 14.12.2016.
 */
@Path("/users")
public class UsersService {

    GenericDao<User, Integer> userDAO;

    @GET
    @Produces("application/json;charset=UTF-8")
    @Path("/all")
    public Response printAllUsersNames(@Context ServletContext servletContext) {
        ApplicationContext ctx =
                WebApplicationContextUtils.getWebApplicationContext(servletContext);

        userDAO = (GenericDao<User, Integer>) ctx.getBean("userDao");
        JSONArray jsonArray = new JSONArray();
        for (User user : userDAO.list()) {
            JSONObject userObj = new JSONObject();
            userObj.put("username", user.getUsername());
            userObj.put("firstName", user.getFirstName());
            userObj.put("lastName", user.getLastName());
            jsonArray.put(userObj);
        }

        String result = jsonArray.toString();

        return Response.status(200).entity(result).build();
    }


    @GET
    @Produces("application/json;charset=UTF-8")
    @Path("/find/{name}")
    public Response findUser(@PathParam(value="name") String name, @Context ServletContext servletContext){
        ApplicationContext ctx =
                WebApplicationContextUtils.getWebApplicationContext(servletContext);
        userDAO = (GenericDao<User, Integer>) ctx.getBean("userDao");
        JSONObject userObj = new JSONObject();
        for (User user : userDAO.list()) {
            if(user.getUsername().equals(name)){
                userObj.put("username", user.getUsername());
                userObj.put("firstName", user.getFirstName());
                userObj.put("lastName", user.getLastName());
                break;
            }
        }
        String result = userObj.toString();
        return Response.status(200).entity(result).build();
    }
}

package cz.matyapav.utils;

import cz.matyapav.models.enums.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Created by Pavel on 25.12.2016.
 */
public class Utils {

    public static User getLoggedUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean loggedUserIsAdmin(){
        org.springframework.security.core.userdetails.User loggedUser =
                (org.springframework.security.core.userdetails.User)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = false;
        for(GrantedAuthority authority : loggedUser.getAuthorities()){
            if(authority.getAuthority().equals(Roles.ADMIN.getRoleName())){
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }

    public static void addError(List<String> errors, String error, RedirectAttributes redirectAttributes){
        errors.add(error);
        redirectAttributes.addFlashAttribute("errors", errors);
    }

    public static void addMessage(List<String> messages, String message, RedirectAttributes redirectAttributes){
        messages.add(message);
        redirectAttributes.addFlashAttribute("messages", messages);
    }


}

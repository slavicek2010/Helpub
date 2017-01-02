package cz.matyapav.controller;

import cz.matyapav.models.Item;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.models.enums.ItemTypes;
import cz.matyapav.models.validators.ItemValidator;
import cz.matyapav.models.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 01.01.2017.
 */

@Controller
public class ItemController {

    @Autowired
    private GenericDao<Item, String> itemDao;

    @RequestMapping(value = "/items/create", method = RequestMethod.GET)
    public ModelAndView item(){
        Item item = new Item();
        ModelAndView model = new ModelAndView("item_form");
        model.addObject("enumValues", ItemTypes.values());
        model.addObject("item", item);
        //TODO poslat do formulare bill id
        return model;
    }

    @RequestMapping(value = "/items/create", method = RequestMethod.POST)
    public ModelAndView createItem(@ModelAttribute("Item") Item item, HttpServletRequest request){
        ModelAndView model = new ModelAndView("redirect:/bills");

        List<String> errors = new ArrayList<>();
        boolean validationFails = false;
        Validator<Item> validator = new ItemValidator();
        if(!validator.validate(item)){
            errors.addAll(validator.getErrorMessages());
            validationFails = true;
        }
        if(itemDao.read(item.getName()) != null){
            //user already exits
            errors.add("Item with that name already exits.");
            validationFails = true;
        }
        if(validationFails){
            request.setAttribute("errors", errors);
            model.addObject("enumValues", ItemTypes.values());
            model.addObject("item", item);
            model.setViewName("item_form");
            return model;
        }
        itemDao.create(item);

        return model;
    }
}

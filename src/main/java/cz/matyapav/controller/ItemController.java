package cz.matyapav.controller;

import cz.matyapav.models.Item;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.models.enums.ItemTypes;
import cz.matyapav.models.validators.ItemValidator;
import cz.matyapav.models.validators.Validator;
import cz.matyapav.services.ItemService;
import cz.matyapav.utils.StatusMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    private ItemService itemService;

    @RequestMapping(value = "/items/create", method = RequestMethod.GET)
    public ModelAndView showItemForm(@RequestParam(name = "billId", required = false) String billId){
        Item item = new Item();
        ModelAndView model = new ModelAndView("item_form");
        model.addObject("enumValues", ItemTypes.values());
        model.addObject("item", item);
        model.addObject("billId", billId);
        return model;
    }

    @RequestMapping(value = "/items/create", method = RequestMethod.POST)
    public ModelAndView createItem(@ModelAttribute("Item") Item item, HttpServletRequest request){
        ModelAndView model = new ModelAndView("redirect:/bills/show?id="+request.getParameter("billId"));
        StatusMessages statusMessages = itemService.createItem(item);
        if(statusMessages.hasErrors()){
            request.setAttribute("errors", statusMessages.getErrors());
            model.addObject("enumValues", ItemTypes.values());
            model.addObject("item", item);
            model.setViewName("item_form");
            return model;
        }

        return model;
    }
}

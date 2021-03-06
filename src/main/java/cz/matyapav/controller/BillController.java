package cz.matyapav.controller;

import cz.matyapav.models.Bill;
import cz.matyapav.models.Item;
import cz.matyapav.models.ItemBill;
import cz.matyapav.models.User;
import cz.matyapav.services.BillService;
import cz.matyapav.services.ItemBillService;
import cz.matyapav.services.ItemService;
import cz.matyapav.services.UserService;
import cz.matyapav.utils.StatusMessages;
import cz.matyapav.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pavel on 25.12.2016.
 */

@Controller
public class BillController {


    @Autowired
    private BillService billService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemBillService itemBillService;

    ////

    @RequestMapping(value = "/bills", method = RequestMethod.GET)
    public ModelAndView bills(ModelAndView model, HttpServletRequest request, @RequestParam(name="username", required = false) String username){
        if(request.getParameter("errors") != null){
            model.addObject("errors", request.getParameter("errors"));
        }

        List<Bill> billsList;
        if(username != null){
            User user = userService.getUser(username);
            if(user != null){
                billsList = billService.getBillsInWhichIsUserPartOf(user);
                model.addObject("showBillsChecked", true);
            }else{
                //if user not found get all bills
                model.addObject("showBillsChecked", false);
                billsList = billService.getAllBills();
            }
        }else{
            model.addObject("showBillsChecked", false);
            billsList = billService.getAllBills();
        }
        model.addObject("listBills", billsList);
        model.setViewName("bills");
        if(Utils.loggedUserIsAdmin()){
            model.addObject("admin", true);
        }
        model.addObject("loggedInUsername", Utils.getLoggedUser().getUsername());
        return model;
    }

    @RequestMapping(value = "/bills/edit", method = RequestMethod.GET)
    public ModelAndView getBillForm(HttpServletRequest request, ModelAndView model, RedirectAttributes redirectAttributes) {
        StatusMessages statusMessages = new StatusMessages();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Bill bill = billService.getBill(id);
            if(bill == null){
                model.setViewName("redirect:/bills");
                statusMessages.addError("Bill not found");
                redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
                return model;
            }
            User loggedUser = userService.getUser(Utils.getLoggedUser().getUsername());
            if(!loggedUser.isInBill(bill) && !Utils.loggedUserIsAdmin()){
                model.setViewName("redirect:/bills");
                statusMessages.addError("You don't have permission to edit this bill.");
                redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
                return model;
            }

            setPropertiesToEditBillForm(model,bill);
            return model;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            model.setViewName("redirect:/bills");
            statusMessages.addError("Bill not found");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
            return model;
        }
    }

    @RequestMapping(value = "/bills/edit", method = RequestMethod.POST)
    public ModelAndView editBill(@ModelAttribute("Bill") Bill bill, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView();
        //validation conditions
        StatusMessages statusMessages = billService.editBill(bill);
        if(statusMessages.hasErrors()){
            request.setAttribute("errors", statusMessages.getErrors());
            setPropertiesToEditBillForm(model, bill);
            return model;
        }
        if(statusMessages.hasMessages()){
            redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
        }

        return new ModelAndView("redirect:/bills/show?id="+bill.getId());
    }

    @RequestMapping(value = "/bills/addItem", method = RequestMethod.POST)
    public ModelAndView addItem(@ModelAttribute("Item") Item item, HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView("redirect:/bills");

        try {
            int billId = Integer.parseInt(request.getParameter("id"));
            double price = Double.parseDouble(request.getParameter("price"));
            StatusMessages statusMessages = billService.addItem(item, billId, price);
            model.setViewName("redirect:/bills/show?id="+billId);
            if(statusMessages.hasErrors()){
                redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
                return model;
            }
            if(statusMessages.hasMessages()){
                redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
            }
        }catch (NumberFormatException e){
            StatusMessages statusMessages = new StatusMessages();
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
        }
        return model;
    }

    @RequestMapping(value = "/bills/removeItem", method = RequestMethod.GET)
    public ModelAndView removeItem(HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView("redirect:/bills");

        try {
            int billId = Integer.parseInt(request.getParameter("id"));
            String itemName = request.getParameter("item");
            String user = request.getParameter("user");
            StatusMessages statusMessages = billService.removeItem(itemName, billId, user);
            model.setViewName("redirect:/bills/show?id="+billId);
            if(statusMessages.hasErrors()){
                redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
                return model;
            }
            if(statusMessages.hasMessages()){
                redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
            }
        }catch (NumberFormatException e){
            StatusMessages statusMessages = new StatusMessages();
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
        }

        return model;

    }

    @RequestMapping(value = "/bills/increaseQuantity", method = RequestMethod.GET)
    public ModelAndView increaseQuantity(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView("redirect:/bills");

        try{
            int billId = Integer.parseInt(request.getParameter("id"));
            String itemName = request.getParameter("item");
            int howMuch = Integer.parseInt(request.getParameter("howMuch"));
            String user = request.getParameter("user");
            StatusMessages statusMessages = billService.increaseItemQuantity(billId, itemName, user, howMuch);
            model.setViewName("redirect:/bills/show?id="+billId);
            if(statusMessages.hasErrors()){
                redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
                return model;
            }
            if(statusMessages.hasMessages()){
                redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
            }

        }catch (NumberFormatException e){
            StatusMessages statusMessages = new StatusMessages();
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
        }
        return model;
    }

    @RequestMapping(value = "/bills/create", method = RequestMethod.GET)
    public ModelAndView showCreateBillForm(ModelAndView model) {
        Bill bill = new Bill();
        setPropertiesToCreateBillForm(model, bill);
        return model;
    }

    @RequestMapping(value = "/bills/create", method = RequestMethod.POST)
    public ModelAndView createBill(@ModelAttribute("Bill") Bill bill, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        //validation conditions
        StatusMessages statusMessages = billService.createBill(bill);
        if(statusMessages.hasErrors()){
            request.setAttribute("errors", statusMessages.getErrors());
            ModelAndView model = new ModelAndView();
            setPropertiesToCreateBillForm(model, bill);
            return model;
        }
        if(statusMessages.hasMessages()){
            redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
        }
        return new ModelAndView("redirect:/bills/show?id="+bill.getId());
    }

    @RequestMapping(value = "/bills/show", method = RequestMethod.GET)
    public ModelAndView showBill(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        StatusMessages statusMessages = new StatusMessages();
        ModelAndView model = new ModelAndView();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Bill bill = billService.getBill(id);
            if(bill == null){
                model.setViewName("redirect:/bills");
                statusMessages.addError("Bill not found.");
                redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
                return model;
            }

            User loggedUser = userService.getUser(Utils.getLoggedUser().getUsername());
            if(!Utils.loggedUserIsAdmin() && !loggedUser.isInBill(bill) && bill.isLocked()){
                model.addObject(new Bill());
                model.addObject("billId", bill.getId());
                model.setViewName("bill_password");
                return model;
            }
            StatusMessages statusMessages1 = billService.addLoggedUserToBill(bill);
            if(statusMessages1.hasErrors()){
                statusMessages.addMultipleErrors(statusMessages1.getErrors());
                model.addObject("errors", statusMessages.getErrors());
                return model;
            }
            if(statusMessages1.hasMessages()){
                statusMessages.addMultipleMessages(statusMessages1.getMessages());
                model.addObject("messages", statusMessages.getMessages());
            }
            bill = billService.getBill(bill.getId()); //TODO neupdatujou se hned po pridani useri .. fetchnu znovu z db

            model.setViewName("bill_show");
            model.addObject("bill", bill);

            HashMap<String, List<ItemBill>> billItems = itemBillService.getBillItemsGroupedByUsernames(bill.getId());
            HashMap<String, Double> particularPricesForUsers = itemBillService.getParticularPriceSumsGroupedByUsernames(bill.getId());

            model.addObject("admin", Utils.loggedUserIsAdmin());
            model.addObject("billItems", billItems);
            model.addObject("particularPrices", particularPricesForUsers);
            model.addObject("loggedUser", loggedUser);
            model.addObject("billUsers", bill.getUsers());
            model.addObject("item", new Item());
            model.addObject("items", itemService.getAllItems());
            model.addObject("total", itemBillService.getBillTotalPrice(bill.getId()));
            return model;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            model.setViewName("redirect:/bills");
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
            return model;
        }
    }

    @RequestMapping(value = "/bills/addUser", method = RequestMethod.POST)
    public ModelAndView addUserToBill(@ModelAttribute("Bill") Bill bill, HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView();
        try {
            StatusMessages statusMessages = billService.addLoggedUserToBill(bill);
            if(statusMessages.hasErrors()){
                model.addObject(bill);
                model.addObject("billId", bill.getId());
                model.setViewName("bill_password");
                request.setAttribute("errors", statusMessages.getErrors());
                return model;
            }
            if(statusMessages.hasMessages()){
                redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
            }
            model.setViewName("redirect:/bills/show?id="+bill.getId());
            return model;
        }catch (NumberFormatException e) {
            StatusMessages statusMessages = new StatusMessages();
            model.setViewName("redirect:/bills");
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
            return model;
        }
    }

    @RequestMapping(value = "/bills/removeUser", method = RequestMethod.GET)
    public ModelAndView removeUserFromBill(HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Bill bill = billService.getBill(id);
            StatusMessages statusMessages = billService.removeLoggedUserFromBill(bill);
            if(statusMessages.hasErrors()){
                redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
            }
            if(statusMessages.hasMessages()){
                redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
            }
            model.setViewName("redirect:/bills");
            return model;
        }catch (NumberFormatException e) {
            StatusMessages statusMessages = new StatusMessages();
            model.setViewName("redirect:/bills");
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
            return model;
        }
    }
    @RequestMapping(value = "/bills/delete", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteBill(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView("redirect:/bills");
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            StatusMessages statusMessages = billService.deleteBill(id);
            if(statusMessages.hasErrors()){
                redirectAttributes.addFlashAttribute("errors",statusMessages.getErrors());
                return model;
            }
            if(statusMessages.hasMessages()){
                redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            StatusMessages statusMessages = new StatusMessages();
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
        }
        return model;
    }

    @RequestMapping(value = "/bills/changeItemPrice", method = RequestMethod.GET)
    public ModelAndView changeItemsPrice(HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView("redirect:/bills");

        try{
            int billId = Integer.parseInt(request.getParameter("id"));
            String itemName = request.getParameter("item");
            double newPrice = Double.parseDouble(request.getParameter("newPrice"));
            String user = request.getParameter("user");
            StatusMessages statusMessages = billService.changeItemsPrice(billId, itemName, user, newPrice);
            model.setViewName("redirect:/bills/show?id="+billId);
            if(statusMessages.hasErrors()){
                redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
                return model;
            }
            if(statusMessages.hasMessages()){
                redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
            }

        }catch (NumberFormatException e){
            StatusMessages statusMessages = new StatusMessages();
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
        }
        return model;
    }

    @RequestMapping(value = "/bills/closeBill", method = RequestMethod.GET)
    public ModelAndView closeBill(HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView("redirect:/bills");
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            StatusMessages statusMessages = billService.closeBill(id);
            if(statusMessages.hasErrors()){
                redirectAttributes.addFlashAttribute("errors",statusMessages.getErrors());
                return model;
            }
            if(statusMessages.hasMessages()){
                redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            StatusMessages statusMessages = new StatusMessages();
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
        }
        return model;
    }

    @RequestMapping(value = "/bills/reopenBill", method = RequestMethod.GET)
    public ModelAndView reopenBill(HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView("redirect:/bills");
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            StatusMessages statusMessages = billService.reopenBill(id);
            if(statusMessages.hasErrors()){
                redirectAttributes.addFlashAttribute("errors",statusMessages.getErrors());
                return model;
            }
            if(statusMessages.hasMessages()){
                redirectAttributes.addFlashAttribute("messages", statusMessages.getMessages());
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            StatusMessages statusMessages = new StatusMessages();
            statusMessages.addError("Bill not found.");
            redirectAttributes.addFlashAttribute("errors", statusMessages.getErrors());
        }
        return model;
    }


    private void setPropertiesToCreateBillForm(ModelAndView model, Bill bill){
        model.addObject("bill", bill);
        model.addObject("formName", "New bill");
        model.addObject("formAction", "/bills/create");
        model.addObject("loggedInUsername", Utils.getLoggedUser().getUsername());
        model.addObject("editing", false);
        model.setViewName("bill_form");
    }

    private void setPropertiesToEditBillForm(ModelAndView model, Bill bill){
        model.addObject("bill", bill);
        model.addObject("formName", "Edit bill");
        model.addObject("formAction", "/bills/edit");
        model.addObject("includeId", true);
        model.addObject("loggedInUsername", Utils.getLoggedUser().getUsername());
        model.addObject("editing", true);
        model.setViewName("bill_form");
    }

}

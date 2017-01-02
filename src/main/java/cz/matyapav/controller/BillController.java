package cz.matyapav.controller;

import cz.matyapav.models.Bill;
import cz.matyapav.models.Item;
import cz.matyapav.models.ItemBill;
import cz.matyapav.models.User;
import cz.matyapav.models.dao.GenericDao;
import cz.matyapav.models.validators.BillValidator;
import cz.matyapav.models.validators.Validator;
import cz.matyapav.utils.ItemBillId;
import cz.matyapav.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 25.12.2016.
 */

@Controller
public class BillController {

    @Autowired
    private GenericDao<Bill, Integer> billDao;

    @Autowired
    private GenericDao<Item, String> itemDao;

    @Autowired
    private GenericDao<ItemBill, ItemBillId> itemBillDao;

    @Autowired
    GenericDao<User, String> userDAO;

    @PersistenceContext
    protected EntityManager entityManager;

    ////

    @RequestMapping(value = "/bills", method = RequestMethod.GET)
    public ModelAndView bills(ModelAndView model, HttpServletRequest request){
        if(request.getParameter("errors") != null){
            model.addObject("errors", request.getParameter("errors"));
        }
        List<Bill> billsList = billDao.list();
        model.addObject("listBills", billsList);
        model.setViewName("bills");
        if(Utils.loggedUserIsAdmin()){
            model.addObject("admin", true);
        }
        model.addObject("loggedInUsername", Utils.getLoggedUser().getUsername());
        return model;
    }

    @RequestMapping(value = "/bills/edit", method = RequestMethod.GET)
    public ModelAndView bill(HttpServletRequest request, ModelAndView model, RedirectAttributes redirectAttributes) {
        List<String> errors = new ArrayList<>();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Bill bill = billDao.read(id);
            if(bill == null){
                model.setViewName("redirect:/bills");
                Utils.addError(errors, "Bill not found.", redirectAttributes);
                return model;
            }
            User loggedUser = userDAO.read(Utils.getLoggedUser().getUsername());
            if(!loggedUser.isInBill(bill) && !Utils.loggedUserIsAdmin()){
                model.setViewName("redirect:/bills");
                Utils.addError(errors, "You don't have permission to edit this bill.", redirectAttributes);
                return model;
            }

            setPropertiesToEditBillForm(model,bill);
            return model;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            model.setViewName("redirect:/bills");
            Utils.addError(errors, "Bill not found.", redirectAttributes);
            return model;
        }
    }

    @RequestMapping(value = "/bills/edit", method = RequestMethod.POST)
    public ModelAndView editBill(@ModelAttribute("Bill") Bill bill, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<String> messages = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        ModelAndView model = new ModelAndView();
        //validation conditions
        Validator<Bill> validator = new BillValidator();
        if(!validator.validate(bill)){
            request.setAttribute("errors", validator.getErrorMessages());
            setPropertiesToEditBillForm(model, bill);
            return model;
        }
        Bill bill1 = billDao.read(bill.getId());
        User loggedUser = userDAO.read(Utils.getLoggedUser().getUsername());
        if(!loggedUser.isInBill(bill1) && !Utils.loggedUserIsAdmin()){
            model.setViewName("redirect:/bills");
            Utils.addError(errors, "You don't have permission to edit this bill.", redirectAttributes);
            return model;
        }
        if(bill1 != null) {
            bill1.setName(bill.getName());
            billDao.update(bill1);
        }
        Utils.addMessage(messages, "Bill successfully edited.", redirectAttributes);
        return new ModelAndView("redirect:/bills/show?id="+bill1.getId());
    }

    @RequestMapping(value = "/bills/addItem", method = RequestMethod.POST)
    public ModelAndView addItem(@ModelAttribute("Item") Item item, HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView("redirect:/bills");

        List<String> errors = new ArrayList<>();
        Bill billDB = billDao.read(Integer.parseInt(request.getParameter("id")));
        if(billDB == null){
            Utils.addError(errors, "Bill not found", redirectAttributes);
            return model;
        }
        //connect item and bill
        ItemBill itemBill = new ItemBill();
        ItemBillId itemBillId = new ItemBillId();
        itemBillId.setItem(item);
        itemBillId.setBill(billDB);
        itemBillId.setAddedBy(Utils.getLoggedUser().getUsername());
        itemBill.setPrimaryKey(itemBillId);
        itemBill.setPrice(Double.parseDouble(request.getParameter("price")));

        ItemBill itemBillDB = itemBillDao.read(itemBillId);
        if(itemBillDB != null){
            int actualQuantity = itemBillDB.getQuantity();
            itemBill.setQuantity(actualQuantity + 1);
            itemBillDao.update(itemBill);
        }else{
            itemBill.setQuantity(1);
            itemBillDao.create(itemBill);
        }
        model.setViewName("redirect:/bills/show?id="+billDB.getId());
        return model;

    }

    @RequestMapping(value = "/bills/removeItem", method = RequestMethod.GET)
    public ModelAndView removeItem(HttpServletRequest request, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView("redirect:/bills");

        List<String> errors = new ArrayList<>();
        Bill billDB = billDao.read(Integer.parseInt(request.getParameter("id")));
        if(billDB == null){
            Utils.addError(errors, "Bill not found", redirectAttributes);
            return model;
        }
        model.setViewName("redirect:/bills/show?id="+billDB.getId());

        Item itemDB = itemDao.read(request.getParameter("item"));
        if(itemDB == null){
            Utils.addError(errors, "Item not found", redirectAttributes);
            return model;
        }

        //connect item and bill
        ItemBillId itemBillId = new ItemBillId();
        itemBillId.setItem(itemDB);
        itemBillId.setBill(billDB);
        itemBillId.setAddedBy(Utils.getLoggedUser().getUsername());

        itemBillDao.delete(itemBillId);

        model.setViewName("redirect:/bills/show?id="+billDB.getId());
        return model;

    }

    @RequestMapping(value = "/bills/increaseQuantity", method = RequestMethod.GET)
    public ModelAndView increaseQuantity(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView("redirect:/bills");

        List<String> errors = new ArrayList<>();
        Bill billDB = billDao.read(Integer.parseInt(request.getParameter("id")));
        if (billDB == null) {
            Utils.addError(errors, "Bill not found", redirectAttributes);
            return model;
        }
        model.setViewName("redirect:/bills/show?id="+billDB.getId());

        Item itemDB = itemDao.read(request.getParameter("item"));
        if(itemDB == null){
            Utils.addError(errors, "Item not found", redirectAttributes);
            return model;
        }

        ItemBillId itemBillId = new ItemBillId();
        itemBillId.setItem(itemDB);
        itemBillId.setBill(billDB);
        itemBillId.setAddedBy(Utils.getLoggedUser().getUsername());

        ItemBill itemBillDB = itemBillDao.read(itemBillId);
        if(itemBillDB != null){
            int actualQuantity = itemBillDB.getQuantity();
            int howMuch = Integer.parseInt(request.getParameter("howMuch"));
            if(howMuch + actualQuantity <= 0){
                //remove item
                return removeItem(request, redirectAttributes);
            }
            itemBillDB.setQuantity(actualQuantity + howMuch);
            itemBillDao.update(itemBillDB);
        }else{
            Utils.addError(errors, "Item connection to specified bill was not found", redirectAttributes);
        }
        return model;
    }

    @RequestMapping(value = "/bills/create", method = RequestMethod.GET)
    public ModelAndView bill(ModelAndView model) {
        Bill bill = new Bill();
        setPropertiesToCreateBillForm(model, bill);
        return model;
    }

    @RequestMapping(value = "/bills/create", method = RequestMethod.POST)
    public ModelAndView addUser(@ModelAttribute("Bill") Bill bill, HttpServletRequest request) {
        //validation conditions
        Validator<Bill> validator = new BillValidator();
        if(!validator.validate(bill)){
            request.setAttribute("errors", validator.getErrorMessages());
            ModelAndView model = new ModelAndView();
            setPropertiesToCreateBillForm(model, bill);
            return model;
        }
        //create bill
        bill.setOpened(true);
        User user = userDAO.read(bill.getCreatorUsername());
        bill.getUsers().add(user);

        billDao.create(bill);
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:/bills/show?id="+bill.getId());
        return model;
    }

    @RequestMapping(value = "/bills/show", method = RequestMethod.GET)
    public ModelAndView showBill(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<String> errors = new ArrayList<>();
        ModelAndView model = new ModelAndView();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Bill bill = billDao.read(id);
            if(bill == null){
                model.setViewName("redirect:/bills");
                Utils.addError(errors, "Bill not found.", redirectAttributes);
                return model;
            }
            User loggedUser = userDAO.read(Utils.getLoggedUser().getUsername());
            if(!loggedUser.isInBill(bill)){
                model.setViewName("redirect:/bills");
                Utils.addError(errors, "You don't have permission to see this bill.", redirectAttributes);
                return model;
            }
            model.setViewName("bill_show");
            model.addObject("bill", bill);

            String hql = "select ib from ItemBill ib where ib.id.bill.id = (:billId)";
            Query query = entityManager.createQuery(hql);
            query.setParameter("billId", bill.getId());
            List<ItemBill> billItems = query.getResultList();

            model.addObject("billItems", billItems);
            model.addObject("loggedUser", loggedUser);
            model.addObject("item", new Item());
            model.addObject("items", itemDao.list());
            return model;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            model.setViewName("redirect:/bills");
            Utils.addError(errors, "Bill not found.", redirectAttributes);
            return model;
        }
    }

    @RequestMapping(value = "/bills/delete", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteUser(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<String> errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        ModelAndView model = new ModelAndView("redirect:/bills");
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Bill bill = billDao.read(id);
            if(bill != null){
                for(User user : bill.getUsers()){
                    user.removeBill(bill);
                    userDAO.update(user);
                }

                entityManager.createQuery("DELETE from ItemBill ib where ib.id.bill.id = (:bill_id)").setParameter("bill_id", bill.getId()).executeUpdate();

                billDao.delete(id);
                Utils.addMessage(messages, "Bill successfully deleted", redirectAttributes);
            }else{
                Utils.addError(errors, "Bill not found.", redirectAttributes);
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            Utils.addError(errors, "Bill not found.", redirectAttributes);
        }
        return model;
    }

    private void setPropertiesToCreateBillForm(ModelAndView model, Bill bill){
        model.addObject("bill", bill);
        model.addObject("formName", "New bill");
        model.addObject("formAction", "/bills/create");
        model.addObject("loggedInUsername", Utils.getLoggedUser().getUsername());
        model.setViewName("bill_form");
    }

    private void setPropertiesToEditBillForm(ModelAndView model, Bill bill){
        model.addObject("bill", bill);
        model.addObject("formName", "Edit bill");
        model.addObject("formAction", "/bills/edit");
        model.addObject("includeId", true);
        model.addObject("loggedInUsername", Utils.getLoggedUser().getUsername());
        model.setViewName("bill_form");
    }
}

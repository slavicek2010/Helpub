package cz.matyapav.utils;

import cz.matyapav.models.Bill;
import cz.matyapav.models.Item;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Pavel on 01.01.2017.
 */
@Embeddable
public class ItemBillId implements Serializable{

    public ItemBillId() {
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_name")
    private Item item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Column(name="added_by", nullable = false)
    private String addedBy;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }
}

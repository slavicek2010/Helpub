package cz.matyapav.models;

import cz.matyapav.utils.ItemBillId;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * Created by Pavel on 01.01.2017.
 */
@Entity
@Table(name="item_bill")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.item",
                joinColumns = @JoinColumn(name = "item_name")),
        @AssociationOverride(name = "primaryKey.bill",
                joinColumns = @JoinColumn(name = "bill_id"))
})
public class ItemBill {

    // composite-id key
    @EmbeddedId
    private ItemBillId primaryKey = new ItemBillId();

    @Column(name="price", nullable = false)
    @Min(value = 0)
    private double price;

    @Column(name="quantity", nullable = false)
    @Min(value = 0)
    private int quantity;

    public ItemBillId getPrimaryKey() {
        return primaryKey;
    }

    @Transient
    public Item getItem(){
        return getPrimaryKey().getItem();
    }

    @Transient
    public Bill getBill(){
        return getPrimaryKey().getBill();
    }

    public String getAddedBy() {
        return getPrimaryKey().getAddedBy();
    }

    public double getPrice() {
        return price;
    }

    public void setPrimaryKey(ItemBillId primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setAddedBy(String addedBy) {
        getPrimaryKey().setAddedBy(addedBy);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItem(Item item){
        getPrimaryKey().setItem(item);
    }

    public void setBill(Bill bill){
        getPrimaryKey().setBill(bill);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

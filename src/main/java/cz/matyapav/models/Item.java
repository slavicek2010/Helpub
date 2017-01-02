package cz.matyapav.models;

import cz.matyapav.models.enums.ItemTypes;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//TODO udelat nejaky rozumny search v itemech ktere uz jsou v db
/**
 * Created by Pavel on 21.12.2016.
 */
@Entity
@Table(name = "Item")
public class Item {

    @Id
    @Column(name = "item_name", unique = true, nullable = false)
    private String name;

    private ItemTypes type;

    @OneToMany(mappedBy = "primaryKey.item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ItemBill> itemBills = new HashSet<>();

    public Item(){
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ItemBill> getItemBills() {
        return itemBills;
    }

    public void setItemBills(Set<ItemBill> itemBills) {
        this.itemBills = itemBills;
    }

    @Enumerated(EnumType.STRING)
    public ItemTypes getType() {
        return type;
    }

    public void setType(ItemTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

}

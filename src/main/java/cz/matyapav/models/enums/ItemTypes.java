package cz.matyapav.models.enums;

/**
 * Created by Pavel on 01.01.2017.
 */
public enum ItemTypes {

    ALCOHOLIC_BEVERAGE("Alcoholic beverage"),
    FOOD("Food"),
    NON_ALCOHOLIC_BEVERAGE("Non-alcoholic beverage"),
    TOBACCO_PRODUCT("Tobacco product");

    private String typeName;

    ItemTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}

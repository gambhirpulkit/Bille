package in.bille.app.merchant;

/**
 * Created by SHUBHAM on 15-11-2015.
 */
public class CreateBillFeedItem {


    String itemName,itemPrice,menuId,category;


    CreateBillFeedItem(String itemName, String itemPrice, String menuId, String category)
    {
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.menuId = menuId;
            this.category = category;
    }

    public String getName() {
        return itemName;
    }

    public String getPrice() {
        return itemPrice;
    }

    public String getMenuId() {
        return menuId;
    }

    public String getCategory() {
        return category;
    }
}

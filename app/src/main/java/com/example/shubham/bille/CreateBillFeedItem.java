package com.example.shubham.bille;

/**
 * Created by SHUBHAM on 15-11-2015.
 */
public class CreateBillFeedItem {


    String itemName,itemPrice,menuId;


    CreateBillFeedItem(String itemName, String itemPrice, String menuId)
    {
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.menuId = menuId;
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


}

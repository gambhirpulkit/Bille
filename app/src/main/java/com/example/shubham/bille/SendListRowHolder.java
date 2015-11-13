package com.example.shubham.bille;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by pulkit-mac on 11/9/15.
 */
public class SendListRowHolder extends RecyclerView.ViewHolder{

    protected TextView itemName;
    protected TextView itemCost;
    protected TextView perItemCost;
    protected TextView itemQty;
    protected ImageButton plusItem;
    protected ImageButton minusItem;
    protected TextView qtyStatus;

    public SendListRowHolder(View view) {
        super(view);
        this.itemName = (TextView) view.findViewById(R.id.item_name);
        this.itemCost = (TextView) view.findViewById(R.id.item_price);
        this.perItemCost = (TextView) view.findViewById(R.id.perItemPrice);
        this.itemQty = (TextView) view.findViewById(R.id.itemQty);
        this.plusItem = (ImageButton) view.findViewById(R.id.plusItem);
        this.minusItem = (ImageButton) view.findViewById(R.id.minusItem);
        this.qtyStatus = (TextView) view.findViewById(R.id.qtyStatus);

    }
}

package in.bille.app.merchant;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pulkit-mac on 11/7/15.
 */
public class CreateListRowHolder extends RecyclerView.ViewHolder {
    //  protected ImageView thumbnail;
    protected TextView itemName;
    protected TextView itemPrice;
    protected ImageButton plusBtn;
    protected ImageButton minusBtn;
    protected TextView qty_show;
    protected ImageView foodcatg;
    //protected Button sendBill;

    public CreateListRowHolder(View view) {
        super(view);


 /*       this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);*/
        this.itemName = (TextView) view.findViewById(R.id.itemName);
        this.itemPrice = (TextView) view.findViewById(R.id.itemPrice);
        this.plusBtn = (ImageButton) view.findViewById(R.id.plus);
        this.minusBtn = (ImageButton) view.findViewById(R.id.minus);
        this.qty_show = (TextView) view.findViewById(R.id.qtyCount);
        this.foodcatg = (ImageView) view.findViewById(R.id.FoodcatImage);
        //this.sendBill = (Button) view.findViewById(R.id.sendBill);



    }
    public void bind(CreateBillFeedItem model) {
        itemName.setText(model.getName());
        itemPrice.setText(model.getPrice());
    }

}


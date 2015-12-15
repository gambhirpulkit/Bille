package in.bille.app.merchant;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SHUBHAM on 05-11-2015.
 */
public class FeedListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    protected TextView title;
    protected TextView price;
    protected TextView phone;
    protected TextView date;
    protected ImageView thumbnail;
    protected TextView customerName;
    protected TextView billAmount;
   // protected ImageButton editBtn;

    private ClickListener clickListener;

    public FeedListRowHolder(View view) {
        super(view);
 /*       this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);*/
        this.customerName = (TextView) view.findViewById(R.id.textViewCustName);
        this.billAmount = (TextView) view.findViewById(R.id.textViewBillAmount);
        //this.editBtn = (ImageButton) view.findViewById(R.id.editMenu);
        this.title = (TextView) view.findViewById(R.id.title);
        this.price = (TextView) view.findViewById(R.id.price);
        this.phone = (TextView) view.findViewById(R.id.homescreenCustphone);
        this.date = (TextView) view.findViewById(R.id.homescreenDate);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }


    public interface ClickListener {

        /**
         * Called when the view is clicked.
         *
         * @param v view that is clicked
         * @param position of the clicked item
         * @param isLongClick true if long click, false otherwise
         */
        public void onClick(View v, int position, boolean isLongClick);

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public void onClick(View v) {

        clickListener.onClick(v, getPosition(), false);

    }

    @Override
    public boolean onLongClick(View v) {

        clickListener.onClick(v, getPosition(), true);

        return true;
    }
}

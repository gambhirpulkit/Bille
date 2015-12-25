package in.bille.app.merchant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pulkit-mac on 12/25/15.
 */
public class BillDescriptionRecyclerAdapter extends RecyclerView.Adapter<BillListRowHolder> {

    private List<FeedItem> feedItemList;

    private Context mContext;

    public BillDescriptionRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;


    }

    @Override
    public BillListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bill_description, parent, false);
        BillListRowHolder mh = new BillListRowHolder(v);


        return mh;
    }

    @Override
    public void onBindViewHolder(final BillListRowHolder holder, final int position) {
        final FeedItem feedItem = feedItemList.get(position);

        holder.itemName.setText(feedItem.getTitle());
        holder.itemQty.setText(feedItem.getQty());
        holder.itemCost.setText(feedItem.getTotal());
        holder.perItemCost.setText(feedItem.getPrice());

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }



}

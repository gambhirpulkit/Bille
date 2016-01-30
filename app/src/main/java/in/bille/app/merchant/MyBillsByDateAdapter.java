package in.bille.app.merchant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by SHUBHAM on 30-01-2016.
 */
public class MyBillsByDateAdapter extends RecyclerView.Adapter<FeedListRowHolder> {


    private List<FeedItem> feedItemList;
    private Context mContext;


    public MyBillsByDateAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;

    }



    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;

    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {

        final FeedItem feedItem = feedItemList.get(i);
        final Integer pos = feedListRowHolder.getAdapterPosition();


        feedListRowHolder.customerName.setText(Html.fromHtml(feedItem.getTitle()));
        feedListRowHolder.billAmount.setText(Html.fromHtml(feedItem.getPrice()));
        feedListRowHolder.phone.setText(Html.fromHtml(feedItem.getPhone()));
        feedListRowHolder.date.setText(Html.fromHtml(feedItem.getDate()));

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}

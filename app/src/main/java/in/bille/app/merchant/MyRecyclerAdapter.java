package in.bille.app.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SHUBHAM on 05-11-2015.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> implements Serializable {


    private List<FeedItem> feedItemList;

    private transient Context mContext;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public MyRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;



    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup,false);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        FeedItem feedItem = feedItemList.get(i);

        //For applying font
       /* String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);*/
/*
        Picasso.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.thumbnail);

*/

        //feedListRowHolder.customerName.setTypeface(tf);
        /*feedListRowHolder.billAmount.setTypeface(tf);
        feedListRowHolder.phone.setTypeface(tf);
        feedListRowHolder.date.setTypeface(tf);*/
        feedListRowHolder.customerName.setText(Html.fromHtml(feedItem.getTitle()));
        feedListRowHolder.billAmount.setText(Html.fromHtml(feedItem.getPrice()));
        feedListRowHolder.phone.setText(Html.fromHtml(feedItem.getPhone()));
        feedListRowHolder.date.setText(Html.fromHtml(feedItem.getDate()));


        final String a,b,c,d,e,f,g;
        a = feedItem.getTitle();
        b = feedItem.getPrice();
        c = feedItem.getBillId();
        d = feedItem.getPhone();
        e = feedItem.gettype();
        f = feedItem.getcustomText();
        g = feedItem.getDiscount();

        feedListRowHolder.setClickListener(new FeedListRowHolder.ClickListener(){
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {
                    // View v at position pos is long-clicked.
                } else {
                    Log.d("bgjsyufgjhfjb",""+c);
                        Intent billdes = new Intent(mContext,BillDescription.class);
                        billdes.putExtra("cusname",a);
                        billdes.putExtra("billamt",b);
                        billdes.putExtra("bill_id",c);
                        billdes.putExtra("c_phone",d);
                        billdes.putExtra("type",e);
                        billdes.putExtra("customtext",f);
                        billdes.putExtra("discount",g);
                        mContext.startActivity(billdes);

                    // View v at position pos is clicked.
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }



}

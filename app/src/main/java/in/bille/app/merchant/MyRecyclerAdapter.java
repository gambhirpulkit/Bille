package in.bille.app.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHUBHAM on 05-11-2015.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter implements Serializable {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<FeedItem> feedItemList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 1;
    private transient Context mContext;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public MyRecyclerAdapter(RecyclerView recyclerView, final List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
       // this.mContext = context;
        Log.d("loadmore", "in MyRecyclerAdapter");
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            Log.d("loadmore", "in onScrolled()");

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();

                                }
                                loading = true;
                            }
                        }
                    });
        }




    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder vh;
        if(i==VIEW_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
             vh = new FeedListRowHolder(v);
        }else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progress_item, viewGroup, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }
    @Override
    public int getItemViewType(int position) {
        return feedItemList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public void setLoaded() {
        loading = false;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

       if(holder instanceof FeedListRowHolder) {
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
           ((FeedListRowHolder) holder).customerName.setText(Html.fromHtml(feedItem.getTitle()));
           ((FeedListRowHolder) holder).billAmount.setText(Html.fromHtml(feedItem.getPrice()));
           ((FeedListRowHolder) holder).phone.setText(Html.fromHtml(feedItem.getPhone()));
           ((FeedListRowHolder) holder).date.setText(Html.fromHtml(feedItem.getDate()));


           final String a, b, c, d, e, f, g;
           a = feedItem.getTitle();
           b = feedItem.getPrice();
           c = feedItem.getBillId();
           d = feedItem.getPhone();
           e = feedItem.gettype();
           f = feedItem.getcustomText();
           g = feedItem.getDiscount();

           ((FeedListRowHolder) holder).setClickListener(new FeedListRowHolder.ClickListener() {
               @Override
               public void onClick(View v, int pos, boolean isLongClick) {
                   if (isLongClick) {
                       // View v at position pos is long-clicked.
                   } else {
                       Log.d("bgjsyufgjhfjb", "" + c);
                       Intent billdes = new Intent(HomeScreen.ha.getApplicationContext(), BillDescription.class);
                       billdes.putExtra("cusname", a);
                       billdes.putExtra("billamt", b);
                       billdes.putExtra("bill_id", c);
                       billdes.putExtra("c_phone", d);
                       billdes.putExtra("type", e);
                       billdes.putExtra("customtext", f);
                       billdes.putExtra("discount", g);
                       HomeScreen.ha.startActivity(billdes);

                       // View v at position pos is clicked.
                   }
               }
           });

       }
        else
       {
           ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
       }
    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }



}

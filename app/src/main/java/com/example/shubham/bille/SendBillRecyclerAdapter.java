package com.example.shubham.bille;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SendBillRecyclerAdapter extends RecyclerView.Adapter<SendListRowHolder> {


    private List<FeedItem> feedItemList;

    private Context mContext;

    ArrayList<String> itemIds = new ArrayList<String>();
    ArrayList<String> itemQty = new ArrayList<String>();
    //List<String> itemQty;

    public SendBillRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;


    }


    @Override
    public SendListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_send_bill, parent, false);
        SendListRowHolder mh = new SendListRowHolder(v);


        return mh;
    }

    @Override
    public void onBindViewHolder(final SendListRowHolder holder, final int position) {
        final FeedItem feedItem = feedItemList.get(position);

        Integer pos = position;
        Log.d("position",pos.toString());

        //Log.d("itemids", feedItem.getMenuId());
        itemIds.add(position, feedItem.getMenuId());
        itemQty.add(position, feedItem.getQty());

        holder.itemName.setText(feedItem.getTitle());
        holder.itemQty.setText(feedItem.getQty());
        holder.itemCost.setText(feedItem.getTotal());
        holder.perItemCost.setText(feedItem.getPrice());
        holder.qtyStatus.setText(feedItem.getQty());
        //notifyItemRangeChanged(position, feedItemList.size());

        Log.d("id list", itemIds.toString());

        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // itemIds.remove(position);
                String valAtPos = itemQty.get(position);
                Integer qtyCount = Integer.parseInt(valAtPos) + 1;
                itemQty.set(position, qtyCount.toString() );
                Log.d("val", valAtPos);

                holder.qtyStatus.setText(qtyCount.toString());
                //itemIds.set(position,valAtPos);
                Log.d("id list", itemQty.toString());

            }
        });

        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // itemIds.remove(position);
                String valAtPos = itemQty.get(position);
                Integer qtyCount = Integer.parseInt(valAtPos) - 1;
                if (qtyCount < 0) {
                    qtyCount = 0;
                }
                itemQty.set(position, qtyCount.toString() );
                Log.d("val", valAtPos);
                if (qtyCount == 0) {
                    remove(position);

                    Log.d("feed list", feedItem.toString());
                }

                holder.qtyStatus.setText(qtyCount.toString());
                //itemIds.set(position,valAtPos);
                Log.d("id list", itemQty.toString());
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void remove(int position) {
        Integer pos = position;
        Log.d("removeAt", pos.toString());
        feedItemList.remove(position);
  //      itemIds.remove(position);
 //       itemQty.remove(position);
        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, feedItemList.size());
    }
}

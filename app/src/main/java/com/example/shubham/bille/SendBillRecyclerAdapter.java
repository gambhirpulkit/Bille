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

        final Integer holderPos = holder.getAdapterPosition();
        final Integer pos = position;
        Log.d("position",pos.toString());

        //Log.d("itemids", feedItem.getMenuId());
        itemIds.add(holder.getAdapterPosition(), feedItem.getMenuId());
        itemQty.add(holder.getAdapterPosition(), feedItem.getQty());

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
                String valAtPos = itemQty.get(holder.getAdapterPosition());
                Integer qtyCount = Integer.parseInt(valAtPos) + 1;
                itemQty.set(holder.getAdapterPosition(), qtyCount.toString());
                Log.d("val", valAtPos);

                holder.qtyStatus.setText(qtyCount.toString());
                holder.itemQty.setText(qtyCount.toString());
                Integer qtyCost = Integer.parseInt(feedItem.getPrice()) * qtyCount;
                holder.itemCost.setText(qtyCost.toString());
                //itemIds.set(position,valAtPos);
                Log.d("id list", itemQty.toString());

            }
        });

        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // itemIds.remove(position);
                String valAtPos = itemQty.get(holder.getAdapterPosition());
                Integer qtyCount = Integer.parseInt(valAtPos) - 1;
                if (qtyCount < 0) {
                    qtyCount = 0;
                }
                itemQty.set(holder.getAdapterPosition(), qtyCount.toString() );
                Log.d("val", valAtPos);
                if (qtyCount == 0) {
                    itemIds.remove(holder.getAdapterPosition());
                    itemQty.remove(holder.getAdapterPosition());
                    itemIds.trimToSize();
                    itemQty.trimToSize();
                    feedItemList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    Integer tempPos = holder.getAdapterPosition();
                    Log.d("POS", tempPos.toString());
                }

                holder.qtyStatus.setText(qtyCount.toString());
                holder.itemQty.setText(qtyCount.toString());
                Integer qtyCost = Integer.parseInt(feedItem.getPrice()) * qtyCount;
                holder.itemCost.setText(qtyCost.toString());
                //itemIds.set(position,valAtPos);
                Log.d("qty list", itemQty.toString());
                Log.d("id list", itemIds.toString());
                Log.d("feddlist",feedItemList.toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


}

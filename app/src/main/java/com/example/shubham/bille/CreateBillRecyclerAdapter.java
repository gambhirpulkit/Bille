package com.example.shubham.bille;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pulkit-mac on 11/7/15.
 */
public class CreateBillRecyclerAdapter extends RecyclerView.Adapter<CreateListRowHolder> {

    Integer qty = 0;

   // itemQty =
    //Integer[] itemPrice;

    private List<FeedItem> feedItemList;

    private Context mContext;
    Integer[] itemQty;
    String[] itemId;




    public CreateBillRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        itemQty = new Integer[feedItemList.size()];
        itemId = new String[feedItemList.size()];
    }


    @Override
    public CreateListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_create_bill, null);
        CreateListRowHolder mh = new CreateListRowHolder(v);


        return mh;
    }

    @Override
    public void onBindViewHolder(final CreateListRowHolder createListRowHolder, int i) {
        final FeedItem feedItem = feedItemList.get(i);
        final Integer pos = createListRowHolder.getAdapterPosition();


/*
        Picasso.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.thumbnail);
*/
        if(itemQty[pos] == null) {
            //qty = 0;
            itemQty[pos] = 0;
        }

        itemId[pos] = feedItem.getMenuId();
       // Log.d("id",itemId[0]);

        createListRowHolder.itemName.setText(Html.fromHtml(feedItem.getTitle()));
        createListRowHolder.itemPrice.setText(Html.fromHtml(feedItem.getPrice()));

        Log.d("pos", pos.toString());
        createListRowHolder.qty_show.setText(Html.fromHtml(itemQty[pos].toString()));

        createListRowHolder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = itemQty[pos];
                qty++;
                itemQty[pos] = qty;
                createListRowHolder.qty_show.setText(Html.fromHtml(itemQty[pos].toString()));

            }
        });
        createListRowHolder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = itemQty[pos];
                qty--;
                if (qty < 0) {
                    qty = 0;
                }
                itemQty[pos] = qty;
                createListRowHolder.qty_show.setText(Html.fromHtml(itemQty[pos].toString()));

            }
        });



    }



    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

}
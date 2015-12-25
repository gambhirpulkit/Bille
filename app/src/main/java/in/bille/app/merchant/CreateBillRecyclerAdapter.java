package in.bille.app.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateBillRecyclerAdapter extends RecyclerView.Adapter<CreateListRowHolder> {

    Integer qty = 0;
    String[] itemCat;
   // itemQty =
    //Integer[] itemPrice;

    private List<CreateBillFeedItem> feedItemList;
    String cattest;
    private Context mContext;
    Integer[] itemQty;
    String[] itemId;
    String[] itemColor;
    private List<CreateBillFeedItem> mModels;
    private Boolean qtyStatus = false;



    public CreateBillRecyclerAdapter(Context context, List<CreateBillFeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        //feedItemList = new ArrayList<>(feedItemList);

        this.mContext = context;
        itemQty = new Integer[feedItemList.size()];
        itemId = new String[feedItemList.size()];
        mModels = new ArrayList<>(feedItemList);
        itemColor = new String[feedItemList.size()];
        itemCat = new String[feedItemList.size()];
}


    @Override
    public CreateListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_create_bill, viewGroup, false);
        CreateListRowHolder mh = new CreateListRowHolder(v);
       // final Integer pos = mh.getAdapterPosition();


        return mh;
    }

    @Override
    public void onBindViewHolder(final CreateListRowHolder createListRowHolder, int i) {
        final CreateBillFeedItem feedItem = mModels.get(i);
            final Integer pos = createListRowHolder.getAdapterPosition();
        /*if((pos%2!=0) && itemQty[pos] == null )
        {
            Log.d("itemColorOdd",itemColor[pos] + "");
            itemColor[pos] = "1";
            Log.d("back",createListRowHolder.itemView.getBackground() + "");
            Log.d("pos_even", pos + "");
            createListRowHolder.itemView.setBackgroundColor(Color.parseColor("#e4e4e4"));
            //createListRowHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        else if (pos%2==0 && itemQty[pos] == null)  {
            Log.d("itemColorEven",itemColor[pos] + "");
            createListRowHolder.itemView.setBackgroundColor(Color.WHITE);
            itemColor[pos] = "0";
        }
        else if (pos%2!=0) {
            createListRowHolder.itemView.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        else if (pos%2==0) {
            createListRowHolder.itemView.setBackgroundColor(Color.WHITE);
        }*/
       // createListRowHolder.itemView.setBackgroundColor(Color.LTGRAY);
       // createListRowHolder.bind(feedItem);
      //  createListRowHolder.bind(feedItem);
/*
        Picasso.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.thumbnail);
*/
       /* String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);*/


        cattest = feedItem.getCategory();
        itemCat[pos] = feedItem.getCategory();

        Log.d("food cat",itemQty[pos] + "cat pos"+itemCat[pos] + pos);
        if(itemCat[pos] == null && itemQty[pos] == null)
        {
            createListRowHolder.foodcatg.setImageResource(R.drawable.nothing);
        }
        else if(itemCat[pos].matches("vg") && itemQty[pos] == null )
        {
            createListRowHolder.foodcatg.setImageResource(R.drawable.veg);
        }
        else if(itemCat[pos].matches("nvg") && itemQty[pos] == null)
        {
            createListRowHolder.foodcatg.setImageResource(R.drawable.nonveg);
        }
        else if(itemCat[pos].matches("vg"))
        {
            createListRowHolder.foodcatg.setImageResource(R.drawable.veg);
        }
        else if(itemCat[pos].matches("nvg"))
        {
            createListRowHolder.foodcatg.setImageResource(R.drawable.nonveg);
        }
        else
        {
            createListRowHolder.foodcatg.setImageResource(R.drawable.nothing);
        }



        if(itemQty[pos] == null) {
            //qty = 0;
            itemQty[pos] = 0;
        }

        itemId[pos] = feedItem.getMenuId();
       // Log.d("id",itemId[0]);


/*
        createListRowHolder.itemName.setTypeface(tf);
        createListRowHolder.itemPrice.setTypeface(tf);
*/


        createListRowHolder.itemName.setText(Html.fromHtml(feedItem.getName()));
        createListRowHolder.itemPrice.setText(Html.fromHtml(feedItem.getPrice()));

        /*cattest = feedItem.getCategory();

        Log.d("food cat",""+cattest);
        if(cattest.matches("vg"))
        {
            createListRowHolder.foodcatg.setImageResource(R.drawable.veg);
        }
        else if(cattest.matches("nvg"))
        {
            createListRowHolder.foodcatg.setImageResource(R.drawable.nonveg);
        }
        else {
            Log.d("food cat",""+cattest);
        }*/
        Log.d("pos", pos.toString());
        createListRowHolder.qty_show.setText(Html.fromHtml(itemQty[pos].toString()));

        createListRowHolder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = itemQty[pos];
                qty++;
                itemQty[pos] = qty;
                createListRowHolder.qty_show.setText(Html.fromHtml(itemQty[pos].toString()));

                Log.d("qtyArr", Arrays.asList(itemQty) + "");
                for (Integer anItemQty : itemQty) {
                    if (anItemQty != null) {
                        if (anItemQty > 0) qtyStatus = true;
                    }
                }
                if (qtyStatus) {
                    sendMessage("items");
                }
                else {
                    sendMessage("noItems");
                }

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
                Boolean flag = true;
                for (Integer anItemQty : itemQty) {
                    if (anItemQty != null) {
                        if (anItemQty > 0) {
                            flag = false;
                            qtyStatus = true;
                        }

                    }
                }
                if(flag) {
                    qtyStatus = false;
                }


                if (qtyStatus) {
                    sendMessage("items");
                }
                else {
                    sendMessage("noItem");
                }

            }
        });



    }



    private void sendMessage(String status) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("qty-count");
        // You can also include some extra data.
        intent.putExtra("qtyStatus", status);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }



    @Override
    public int getItemCount() {
        return (null != mModels ? mModels.size() : 0);
    }

    public void animateTo(List<CreateBillFeedItem> models) {
      //  Log.d("list",""+feedItemList.size());
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<CreateBillFeedItem> newModels) {
       // Log.d("list0",""+feedItemList.size());
       for (int i = mModels.size() - 1; i >= 0; i--) {
            final CreateBillFeedItem feedItem = mModels.get(i);
            if (!newModels.contains(feedItem)) {
                Log.d("list4","removeitem working");
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<CreateBillFeedItem> newModels) {
       // Log.d("list1",""+feedItemList.size());
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final  CreateBillFeedItem feedItem = newModels.get(i);
            if (!mModels.contains(feedItem)) {
                Log.d("list4","animateadditem working");
                addItem(i, feedItem);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<CreateBillFeedItem> newModels) {
       // Log.d("list2",""+feedItemList.size());
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final CreateBillFeedItem feedItem = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(feedItem);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                Log.d("list4","animatemoveditem working");
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public CreateBillFeedItem removeItem(int position) {
       // Log.d("list3",""+feedItemList.size());
        final CreateBillFeedItem feedItem = mModels.remove(position);
        notifyItemRemoved(position);
        return feedItem;
    }

    public void addItem(int position, CreateBillFeedItem feedItem) {
        //Log.d("list4",""+feedItemList.size());
        mModels.add(position, feedItem);

        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        //Log.d("list5",""+feedItemList.size());
        final CreateBillFeedItem feedItem = mModels.remove(fromPosition);
        Log.d("list7","move item working");
        mModels.add(toPosition, feedItem);
        notifyItemMoved(fromPosition, toPosition);
    }

}
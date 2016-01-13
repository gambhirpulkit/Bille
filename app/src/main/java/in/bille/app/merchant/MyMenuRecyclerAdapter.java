package in.bille.app.merchant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

/**
 * Created by pulkit-mac on 10/29/15.
 */


public class MyMenuRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {
    Integer qty = 0;
    private static String url = "";
    String menuid="";
    private List<FeedItem> feedItemList;
    String[] itemCat;
    Integer[] itemQty;
    String cattest;
    private Context mContext;
    Connectiondetector cd;
    Boolean isInternetPresent = false;


    public MyMenuRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        itemCat = new String[feedItemList.size()];
        itemQty = new Integer[feedItemList.size()];
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menulist_row, viewGroup, false);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(final FeedListRowHolder feedListRowHolder, int i) {
        final FeedItem feedItem = feedItemList.get(i);
        final Integer pos = feedListRowHolder.getAdapterPosition();

       /* String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);*/
        cd = new Connectiondetector(mContext.getApplicationContext());


        cattest = feedItem.getCategory();
        itemCat[pos] = feedItem.getCategory();

        Log.d("food cat",itemQty[pos] + "cat pos"+itemCat[pos] + pos);
        if(itemCat[pos] == null && itemQty[pos] == null)
        {
            feedListRowHolder.foodcatg.setImageResource(R.drawable.nothing);
        }
        else if(itemCat[pos].matches("vg") && itemQty[pos] == null )
        {
            feedListRowHolder.foodcatg.setImageResource(R.drawable.veg);
        }
        else if(itemCat[pos].matches("nvg") && itemQty[pos] == null)
        {
            feedListRowHolder.foodcatg.setImageResource(R.drawable.nonveg);
        }
        else if(itemCat[pos].matches("vg"))
        {
            feedListRowHolder.foodcatg.setImageResource(R.drawable.veg);
        }
        else if(itemCat[pos].matches("nvg"))
        {
            feedListRowHolder.foodcatg.setImageResource(R.drawable.nonveg);
        }
        else
        {
            feedListRowHolder.foodcatg.setImageResource(R.drawable.nothing);
        }


        if(itemQty[pos] == null) {
            //qty = 0;
            itemQty[pos] = 0;
        }


/*




        Picasso.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.thumbnail);
*/
        /*feedListRowHolder.title.setTypeface(tf);
        feedListRowHolder.price.setTypeface(tf);*/
        feedListRowHolder.title.setText(Html.fromHtml(feedItem.getTitle()));
        feedListRowHolder.price.setText(Html.fromHtml(feedItem.getPrice()));

        /*feedListRowHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent i = new Intent(mContext, EditItem.class);
                i.putExtra("menu_id", feedItem.getMenuId());
                i.putExtra("item_name", feedItem.getTitle());
                i.putExtra("item_price", feedItem.getPrice());
                mContext.startActivity(i);
                Log.d("onBindHolder", "test");
                Log.d("val", feedItem.getMenuId());*//*
            }
        });*/

        feedListRowHolder.setClickListener(new FeedListRowHolder.ClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {



                    // View v at position pos is long-clicked.
                } else {
                    isInternetPresent = cd.isConnectingToInternet();
                    Log.d("test", isInternetPresent.toString());

                    if(isInternetPresent) {
                        Intent i = new Intent(mContext, EditItem.class);
                        i.putExtra("menu_id", feedItem.getMenuId());
                        i.putExtra("item_name", feedItem.getTitle());
                        i.putExtra("item_price", feedItem.getPrice());
                        i.putExtra("foodcatg", feedItem.getCategory());
                        mContext.startActivity(i);
                        Log.d("onBindHolder", "test");
                        Log.d("val", feedItem.getMenuId());
                    }
                    else
                    {
                        Toast.makeText(mContext.getApplicationContext(),"No Connected to the Internet",Toast.LENGTH_LONG).show();
                    }
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

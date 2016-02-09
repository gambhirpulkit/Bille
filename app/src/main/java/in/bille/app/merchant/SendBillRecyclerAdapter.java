package in.bille.app.merchant;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendBillRecyclerAdapter extends RecyclerView.Adapter<SendListRowHolder> {

    Integer qty = 0;
    Integer flag = 0;
    SendBill sendbill;
    private List<FeedItem> feedItemList;
    Integer holderPos;
    Integer sendtotal=0,billamt;
    private Context mContext;
   // List<Integer> checkQty;
    Integer[] checkQty;

    ArrayList<Integer> intList;
   // Integer[] Qty;
    ArrayList<String> itemIds = new ArrayList<String>();
    ArrayList<String> itemQty = new ArrayList<String>();
    //List<String> itemQty;

    public SendBillRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
      //  checkQty = new ArrayList<>();
        checkQty = new Integer[feedItemList.size()];
       // Qty = new Integer[feedItemList.size()];
        intList = new ArrayList<Integer>(feedItemList.size());

    }


    @Override
    public SendListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_send_bill, parent, false);
        SendListRowHolder mh = new SendListRowHolder(v);



        return mh;
    }

    @Override
    public void onBindViewHolder(final SendListRowHolder holder, final int position) {

        flag++;
        final FeedItem feedItem = feedItemList.get(position);

        holderPos = holder.getAdapterPosition();
        final Integer pos = position;
        Log.d("position",pos.toString());

        try {
            intList.get( holderPos );
        } catch ( IndexOutOfBoundsException e ) {
            intList.add(holderPos, Integer.parseInt(feedItem.getQty()));
        }
          //  Log.d("item arraylistt",intList.get(holderPos).toString());
     //   if(intList.get(holderPos) == null) {
          //  intList.set(holderPos,Integer.parseInt(feedItem.getQty()));
        //if (intList.get(holderPos).toString())
       // intList.add(holderPos,Integer.parseInt(feedItem.getQty()));

        for (Integer s : intList){
            Log.d("My array list content: ", s + "");
        }
        Log.d("arrayList", intList.toString());
      //  }

      //  if(checkQty[holderPos] == null) {
            //qty = 0;
      //      checkQty[holderPos] = Integer.parseInt(feedItem.getQty());
      //  }
        Log.d("holderPosValue", holderPos + "");
        /*if(Qty[holderPos] == null) {
            //qty = 0;
            Qty[holderPos] = 0;
        }*/
        /*String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);*/

        //Log.d("itemids", feedItem.getMenuId());
        itemIds.add(holder.getAdapterPosition(), feedItem.getMenuId());
        itemQty.add(holder.getAdapterPosition(), feedItem.getQty());

       /* holder.itemName.setTypeface(tf);
        holder.itemQty.setTypeface(tf);
        holder.itemCost.setTypeface(tf);
        holder.perItemCost.setTypeface(tf);
        holder.qtyStatus.setTypeface(tf);*/
        billamt= Integer.parseInt(feedItem.getTotal());
        //sendtotal += billamt;
        Log.d("total", "" + sendtotal);

        holder.itemName.setText(feedItem.getTitle());
     //   holder.itemQty.setText(checkQty[holderPos].toString());
        holder.itemQty.setText(intList.get(holderPos).toString());

        holder.itemCost.setText(feedItem.getTotal());
        holder.perItemCost.setText(feedItem.getPrice());
        holder.qtyStatus.setText(intList.get(holderPos).toString());


       // ((SendBill)mContext).onsetAmt(new SendBill().totalAmt);

       // Log.d("amtShow",new SendBill().totalAmt.toString());
       // sendbill.onsetAmt(sendtotal);

        // sendbill.onsetAmt(sendtotal);

        //notifyItemRangeChanged(position, feedItemList.size());

        Log.d("id list", itemIds.toString());

        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("holderPos", holderPos + "");
                Log.d("adapterPos", holder.getAdapterPosition() + "");

                //checkQty[holder.getAdapterPosition()]++;
                intList.set(holder.getAdapterPosition(),intList.get(holder.getAdapterPosition())+1);
                // itemIds.remove(position);
                String valAtPos = itemQty.get(holder.getAdapterPosition());
                Integer qtyCount = Integer.parseInt(valAtPos) + 1;
                itemQty.set(holder.getAdapterPosition(), qtyCount.toString());
                Log.d("val", valAtPos);

                holder.qtyStatus.setText(intList.get(holder.getAdapterPosition()).toString());
                holder.itemQty.setText(intList.get(holder.getAdapterPosition()).toString());
                //holder.qtyStatus.setText(checkQty[holder.getAdapterPosition()].toString());
                //holder.itemQty.setText(checkQty[holder.getAdapterPosition()].toString());
                Integer peritemcost = Integer.parseInt(feedItem.getPrice());
             //   Integer qtyCost = Integer.parseInt(feedItem.getPrice()) * checkQty[holder.getAdapterPosition()];
                Integer qtyCost = Integer.parseInt(feedItem.getPrice()) * intList.get(holder.getAdapterPosition());
                Log.d("qtyCost", "" + qtyCost);
                //holder.itemCost.setText(qtyCost.toString());
                sendtotal += peritemcost;
                Log.d("afterplus", "" + sendtotal);

                ((SendBill) mContext).onPlusAmt(peritemcost);

               // ((SendBill) mContext).onPlusQty(checkQty[holder.getAdapterPosition()].toString(),holder.getAdapterPosition());
                ((SendBill) mContext).onPlusQty(intList.get(holder.getAdapterPosition()).toString(),holder.getAdapterPosition());

                /*else
                {
                    Integer setbill = Integer.parseInt(feedItem.getTotalBill());
                    ((SendBill) mContext).onsetAmt(setbill);
                }*/
                //itemIds.set(position,valAtPos);
                //Log.d("id list", itemQty.toString());

            }
        });

        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // itemIds.remove(position);
                Log.d("holderPos", holderPos + "");
                Log.d("adapterPos", holder.getAdapterPosition() + "");


                //checkQty[holder.getAdapterPosition()]--;
                intList.set(holder.getAdapterPosition(),intList.get(holder.getAdapterPosition())-1);

                String valAtPos = itemQty.get(holder.getAdapterPosition());
                Integer qtyCount = Integer.parseInt(valAtPos) - 1;
                if (intList.get(holder.getAdapterPosition()) < 0) {
                    intList.set(holder.getAdapterPosition(),0);
                }
                itemQty.set(holder.getAdapterPosition(), intList.get(holder.getAdapterPosition()).toString());
                Log.d("val", valAtPos);
                if (intList.get(holder.getAdapterPosition()) == 0) {
                    itemIds.remove(holder.getAdapterPosition());
                    itemQty.remove(holder.getAdapterPosition());
                    itemIds.trimToSize();
                    itemQty.trimToSize();
                    //List<Integer> aList  = Arrays.asList(checkQty);
                    //aList.remove(holder.getAdapterPosition());
                    //aList.toArray(checkQty);



                 //   holder.qtyStatus.setText(intList.get(holder.getAdapterPosition()).toString());
                 //   holder.itemQty.setText(intList.get(holder.getAdapterPosition()).toString());

                    Log.d("checkQty", checkQty.toString());
                   // checkQty =(Integer[])ArrayUtils.removeElement(checkQty, 1);
                    Log.d("holderPos when 0", holderPos + "");
                    Log.d("adapterPos when 0", holder.getAdapterPosition() + "");

                    Integer peritemcost = Integer.parseInt(feedItem.getPrice());
               //     Integer qtyCost = Integer.parseInt(feedItem.getPrice()) * intList.get(holder.getAdapterPosition());

//                    ((SendBill) mContext).onMinusQty(checkQty[holder.getAdapterPosition()].toString(), holder.getAdapterPosition());
                    ((SendBill) mContext).onMinusAmt(peritemcost);

                    intList.remove(holder.getAdapterPosition());
                    intList.trimToSize();

                    feedItemList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                  //  notifyItemRangeChanged(holder.getAdapterPosition(), feedItemList.size());
                    Log.d("feedItemList show",feedItemList.toString());
                    //Integer tempPos = holder.getAdapterPosition();
                    //Log.d("POS", tempPos.toString());

                }
                else {
                    Log.d("holderPos after remove", holderPos + "");
                    Log.d("adapterPos after remove", holder.getAdapterPosition() + "");

                    holder.qtyStatus.setText(intList.get(holder.getAdapterPosition()).toString());
                holder.itemQty.setText(intList.get(holder.getAdapterPosition()).toString());
               /* holder.qtyStatus.setText(qtyCount.toString());
                holder.itemQty.setText(qtyCount.toString());*/
                    Integer peritemcost = Integer.parseInt(feedItem.getPrice());
                    Integer qtyCost = Integer.parseInt(feedItem.getPrice()) * intList.get(holder.getAdapterPosition());
                    holder.itemCost.setText(qtyCost.toString());
                    sendtotal -= peritemcost;
                    Log.d("afterminus", "" + sendtotal);

                    ((SendBill) mContext).onMinusAmt(peritemcost);
                /*if (flag==1) {
                    ((SendBill) mContext).onsetAmt(sendtotal);
                }
                else
                {
                    Integer setbill = Integer.parseInt(feedItem.getTotalBill());
                    ((SendBill) mContext).onsetAmt(setbill);
                }*/
                    //itemIds.set(position,valAtPos);
                    //Log.d("qty list", itemQty.toString());
                    //Log.d("id list", itemIds.toString());
                    Log.d("feddlist", feedItemList.toString());

                    ((SendBill) mContext).onMinusQty(intList.get(holder.getAdapterPosition()).toString(), holder.getAdapterPosition());

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


}
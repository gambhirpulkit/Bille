package in.bille.app.merchant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BillDescription extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    SessionManager session;
    private List<FeedItem> feedsList;
    TextView name,amount,custphone,quickbilltext,billDiscount;
    TextView menulist,totalamt,paymentstat;
    ImageView paystatus;
    private RecyclerView mRecyclerView;
    private BillDescriptionRecyclerAdapter adapter;
    String cname,bamt,billid,cphone,billtype,Customtext,discount;
    String itemName;
    String a,b,sign,paystat;
    String url = Config.url+"order_mer.php?bill_id=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_description);
        session = new SessionManager(getApplicationContext());
        String fontPath = "fonts/Rupee_Foradian.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bill Description");





        name = (TextView)findViewById(R.id.textViewCustomerName);
        amount = (TextView)findViewById(R.id.textViewBillAmount);
        menulist = (TextView)findViewById(R.id.textView_menulist);
        custphone = (TextView)findViewById(R.id.textView_cphone);
        totalamt = (TextView)findViewById(R.id.textView_total);
        quickbilltext = (TextView)findViewById(R.id.QuickBilltextView);
        billDiscount = (TextView)findViewById(R.id.textView12);
        paystatus = (ImageView)findViewById(R.id.imageViewPaidStatus);
        paymentstat = (TextView)findViewById(R.id.textView16);

        // Initialize recycler view


        discount = "`";
        a = totalamt.getText().toString();
        b=a;

        Intent billdes = getIntent();
        cname = billdes.getStringExtra("cusname");
        bamt = billdes.getStringExtra("billamt");
        billid = billdes.getStringExtra("bill_id");
        cphone = billdes.getStringExtra("c_phone");
        billtype = billdes.getStringExtra("type");
        Customtext = billdes.getStringExtra("customtext");
        discount += billdes.getStringExtra("discount");
        paystat = billdes.getStringExtra("paystatus");

        Log.d("paystatbill",""+paystat);

        //menulist.setTypeface(tf);
        Log.d("billid",""+billid);

        url += billid + "&token="+Splash.sign;
        Log.d("oooooooooooooooo", "" + url);

        if(billtype.matches("detail"))
        {
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_bill_desc);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            new AsyncHttpTask().execute(url);
        }
        else
        {
            quickbilltext.setText(Customtext);
        }







        custphone.setText("+91"+cphone);
        name.setText(cname);
        //amount.setText(""+bamt);
        //totalamt.setText("@string/Rs"+bamt);
        billDiscount.setTypeface(tf);
        billDiscount.setText(discount);

        if(paystat.matches("0"))
        {
            Log.d("pay","In if");
            paystatus.setImageResource(R.drawable.cancel32);
            paymentstat.setText("PENDING");
        }
        else if(paystat.matches("1"))
        {
            Log.d("pay","In ElseIf");
            paystatus.setImageResource(R.drawable.ok32);
            paymentstat.setText("PAID");
        }
        else
        {
            Log.d("pay","In Else");
        }

        a += bamt;
        amount.setText(a);



        totalamt.setText(a);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(BillDescription.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Log.d("22222222222","99999999999");
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                Log.d("22222222222","88888888888");
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("str", response.toString());
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    Log.d("22222222222","7777777777");
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                //Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            try {
                if ((mProgressDialog != null) &&  mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                mProgressDialog = null;
            }

            if (result == 1) {
                adapter = new BillDescriptionRecyclerAdapter(BillDescription.this,feedsList);
                mRecyclerView.setAdapter(adapter);
               // menulist.setText(itemName);

            } else {
                Toast.makeText(BillDescription.this, "Please connect to internet to view complete bill.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {

        Log.d("22222222222",""+itemName);
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("order");

            Log.d("parse","result");
            feedsList = new ArrayList<>();
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
               // FeedItem item = new FeedItem();
               // Log.d("val", post.getString("c_name"));
                String itemname = post.optString("item");
                String itemprice = post.optString("price");
                String phone = post.optString("c_phone");
                String quantity = post.optString("qty");
                String totalcost = post.optString("cost");

                FeedItem item = new FeedItem();

                item.setTitle(itemname);
                item.setPrice(itemprice);
                item.setTotal(totalcost);
                item.setQty(quantity + " x ");

                feedsList.add(item);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}

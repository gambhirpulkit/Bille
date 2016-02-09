package in.bille.app.merchant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreateBill extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ProgressDialog mProgressDialog;
    private Integer flag = 0;
    final Context context = this;
   // Map<String, Integer> mapIndex;
    private static final String TAG = "menu";
    private List<CreateBillFeedItem> feedsList;
    private List<FeedItem> mModels;
    private RecyclerView mRecyclerView;
    public CreateBillRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private String phone = "";
    SessionManager session;
    public static Activity fa;
    private SearchView mSearchView;
    String checkphoneUrl = "",sign;
    String url = Config.url+"list_menu.php?mid=";
    private String stringId = null;
    private String stringQty = null;

    private List<CreateBillFeedItem> filteredModelList;

    Button checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
        fa =this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create Bill");

        session = new SessionManager(getApplicationContext());


        boolean check = session.checkLogin();

        if(check)
        {
            this.finish();
        }
        else
        {

        }


        mSearchView = (SearchView)findViewById(R.id.searchView);
        setupSearchView();

        getIntent();

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_bill);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        HashMap<String, String> user = session.getUserDetails();

        String mid = user.get(SessionManager.KEY_MID);

        url += mid + "&token="+Splash.sign;

/*        progressBar = (ProgressBar) findViewById(R.id.progress_bar_bill);
        progressBar.setVisibility(View.VISIBLE);*/

        new AsyncHttpTask().execute(url);



        checkout = (Button) findViewById(R.id.sendBill);
        checkout.setEnabled(false);
        checkout.getBackground().setAlpha(64);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("qty-count"));



        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = "";
                if (adapter.itemQty != null) {
                    StringBuilder qty = new StringBuilder("");
                    for (int i = 0; adapter.itemQty != null && i < adapter.itemQty.length; i++) {
                        if (adapter.itemQty[i] != null && adapter.itemQty[i] != 0) {
                            qty.append(adapter.itemQty[i]);
                            if (i < adapter.itemQty.length - 1) {
                                qty.append(',');
                            }
                        }
                    }
                    stringQty = qty.toString();
                    Log.d("qty", qty.toString());

                }
                if (adapter.itemId != null) {
                    StringBuilder ids = new StringBuilder("");
                    for (int i = 0; adapter.itemId != null && i < adapter.itemId.length; i++) {
                        if (adapter.itemId[i] != null && adapter.itemQty[i] != 0) {
                            ids.append(adapter.itemId[i]);
                            if (i < adapter.itemId.length - 1) {
                                ids.append(',');
                            }
                        }
                    }
                    Log.d("OrderId", ids.toString());
                    stringId = ids.toString();

                }


                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        phone = userInput.getText().toString();

                        if (phone.equals("")||phone.length()!=10) {

                            Toast.makeText(getApplicationContext(),
                                    "Enter the correct mobile number.", Toast.LENGTH_SHORT).show();

                        }else {
                            checkphoneUrl = Config.url+"check_phone.php?phone="+phone + "&token="+Splash.sign;
                            Log.d("phoneurl", "" + checkphoneUrl);
                            new PhoneAsyncHttpTask().execute(checkphoneUrl);

                        }

                    }
                })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getBaseContext(), "CANCEL Clicked!", Toast.LENGTH_SHORT).show();
                            }
                        });


                android.app.AlertDialog alert = builder.create();
                alert.show();


        }


        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        boolean check = session.checkLogin();

        if(check)
        {
            this.finish();
        }
        else
        {
            /*Log.d("sdhs","kgfogfow");
            Intent i = getIntent();
            String merchemail = i.getStringExtra("email");
            Log.d("testit",""+merchemail);
            merchEmail.setText(merchemail);*/
            //session.checkLogin();
        }

    }


    /*private void getIndexList(List<CreateBillFeedItem> models) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (CreateBillFeedItem feedItem : models) {
            String text = feedItem.getName().toLowerCase();
            String index = text.substring(0, 1);

            *//*if (mapIndex.get(index) == null)
                mapIndex.put(index, i);*//*
        }
    }

    private void displayIndex() {
        LinearLayout indexLayout = (LinearLayout) findViewById(R.id.side_index);

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            indexLayout.addView(textView);
        }
    }*/

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("qtyStatus");
            Log.d("receiver", "Got message: " + message);
            if (message.equals("items")) {
                checkout.setEnabled(true);
                checkout.getBackground().setAlpha(255);

            }
            else if (message.equals("noItem")) {
                checkout.setEnabled(false);
                checkout.getBackground().setAlpha(64);
            }
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                CreateBill.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
        mSearchView.setSubmitButtonEnabled(false);

    }



    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(CreateBill.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
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
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
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
                    adapter = new CreateBillRecyclerAdapter(CreateBill.this,feedsList);

                    mRecyclerView.setAdapter(adapter);

                    /*getIndexList(feedsList);

                    displayIndex();*/

                } else {
                    SharedPreferences sharedPrefs = getSharedPreferences("MyPrefsCreateBill", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = sharedPrefs.getString(TAG, null);
                    //feedsList = new ArrayList<>();
                    Type type = new TypeToken<ArrayList<CreateBillFeedItem>>() {}.getType();
                    List<CreateBillFeedItem> feedsList = gson.fromJson(json,type);

                    adapter = new CreateBillRecyclerAdapter(CreateBill.this,feedsList);
                    mRecyclerView.setAdapter(adapter);

                    Toast.makeText(CreateBill.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("menu");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                CreateBillFeedItem item = new CreateBillFeedItem(post.optString("item"),post.optString("price"),post.optString("menu_id"),post.optString("cat"));
                Log.d("val", post.getString("item"));
                /*item.setTitle(post.optString("item"));
                item.setPrice(post.optString("price"));
                item.setMenuId(post.optString("menu_id"));*/
               /* item.setThumbnail(post.optString("thumbnail"));
*/
                feedsList.add(item);

                SharedPreferences sharedPrefs = this.getSharedPreferences("MyPrefsCreateBill", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(feedsList);
                Log.d("offline",""+json);
                editor.putString(TAG, json);
                editor.commit();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class PhoneAsyncHttpTask extends AsyncTask<String, Void, Integer> {

       // private Integer flag = 0;
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(CreateBill.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Checking Number...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
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
                    phoneparseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            checkphoneUrl = "";
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

                if(flag == 1)
                {
                    Log.d("result=1","Inside this");
                    Toast.makeText(CreateBill.this, "This Number is not Registered!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("result=0","Inside this");
                    Intent i = new Intent(getApplicationContext(), SendBill.class);
                    i.putExtra("itemString", stringId);
                    i.putExtra("qtyString", stringQty);
                    i.putExtra("cPhone", phone);
                    startActivity(i);
                }
                    /*getIndexList(feedsList);

                    displayIndex();*/

            } else {


                Toast.makeText(CreateBill.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void phoneparseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String checkphone = response.optString("error");

            if(checkphone.matches("true"))
            {
                Log.d("phoneerrortrue","Inside this");
                flag = 1;
            }
            else if(checkphone.matches("false"))
            {
                Log.d("phoneerrorfalse","Inside this");
                flag = 0;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public boolean onQueryTextChange(String query) {
        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefsCreateBill", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, null);
        //feedsList = new ArrayList<>();
        Type type = new TypeToken<ArrayList<CreateBillFeedItem>>() {}.getType();
        List<CreateBillFeedItem> feedsList = gson.fromJson(json,type);

        filteredModelList = filter(feedsList, query);

        adapter.animateTo(filteredModelList);

        mRecyclerView.scrollToPosition(0);
        filteredModelList.clear();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }
    public List<CreateBillFeedItem> filter(List<CreateBillFeedItem> models, String query) {
        query = query.toLowerCase();

        final List<CreateBillFeedItem> filteredModelList = new ArrayList<>();
        for (CreateBillFeedItem feedItem : models) {
            final String text = feedItem.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(feedItem);
            }
        }
        return filteredModelList;
    }


}

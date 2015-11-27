package com.example.shubham.bille;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.SearchView;
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

public class CreateBill extends AppCompatActivity implements SearchView.OnQueryTextListener {

    final Context context = this;

    private static final String TAG = "menu";
    private List<CreateBillFeedItem> feedsList;
    private List<FeedItem> mModels;
    private RecyclerView mRecyclerView;
    public CreateBillRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private String phone = null;
    SessionManager session;

    private SearchView mSearchView;

    String url = Config.url+"list_menu.php?mid=";
    private String stringId = null;
    private String stringQty = null;

    private List<CreateBillFeedItem> filteredModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session = new SessionManager(getApplicationContext());


        mSearchView = (SearchView)findViewById(R.id.searchView);
        setupSearchView();

        getIntent();

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_bill);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        HashMap<String, String> user = session.getUserDetails();

        String mid = user.get(SessionManager.KEY_MID);

        url += mid;

/*        progressBar = (ProgressBar) findViewById(R.id.progress_bar_bill);
        progressBar.setVisibility(View.VISIBLE);*/

        new AsyncHttpTask().execute(url);



/*
        feedsList = new List<FeedItem>();

        feedsList.addAll(adapter.feedItemList);

        adapter = new CreateBillRecyclerAdapter(getApplicationContext(), feedsList);
        mRecyclerView.setAdapter(adapter);*/

        Button checkout = (Button) findViewById(R.id.sendBill);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Toast.makeText(getBaseContext(), "OK Clicked!", Toast.LENGTH_LONG).show();
                        phone = userInput.getText().toString();
                        Intent i = new Intent(getApplicationContext(), SendBill.class);
                        i.putExtra("itemString", stringId);
                        i.putExtra("qtyString",stringQty);
                        i.putExtra("cPhone",phone);
                        startActivity(i);


                    }
                })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getBaseContext(), "CANCEL Clicked!", Toast.LENGTH_LONG).show();
                            }
                        });


                android.app.AlertDialog alert = builder.create();
                alert.show();






        }





        });
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
    }


   /* @Override
    public boolean onQueryTextChange(String query) {
        final List<FeedItem> filteredModelList = filter(feedsList, query);

        adapter.animateTo(filteredModelList);

        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }
    public List<FeedItem> filter(List<FeedItem> models, String query) {
        query = query.toLowerCase();

        final List<FeedItem> filteredModelList = new ArrayList<>();
        for (FeedItem feedItem : models) {
            final String text = feedItem.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(feedItem);
            }
        }
        return filteredModelList;
    }*/




    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
          //  setProgressBarIndeterminateVisibility(true);
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
                // Download complete. Let us update UI
                //progressBar.setVisibility(View.GONE);

                if (result == 1) {
                    adapter = new CreateBillRecyclerAdapter(CreateBill.this,feedsList);
                    mRecyclerView.setAdapter(adapter);

                } else {
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
                CreateBillFeedItem item = new CreateBillFeedItem(post.optString("item"),post.optString("price"),post.optString("menu_id"));
                Log.d("val", post.getString("item"));
                /*item.setTitle(post.optString("item"));
                item.setPrice(post.optString("price"));
                item.setMenuId(post.optString("menu_id"));*/
               /* item.setThumbnail(post.optString("thumbnail"));
*/
                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






    @Override
    public boolean onQueryTextChange(String query) {
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

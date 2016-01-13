package in.bille.app.merchant;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog mProgressDialog;

    Connectiondetector cd;
    Boolean isInternetPresent = false;

    private static final String TAG = "Menu";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyMenuRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    SessionManager session;

    String url = Config.url+"list_menu.php?mid=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Menu");

        cd = new Connectiondetector(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        Log.d("test", "onCreate Menu");
        Intent intentBack = getIntent();

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.d("swipetest","refresh");
                refreshContent();

            }
        });


        /*Button addBtn = (Button) findViewById(R.id.create);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddItem.class);
                MainActivity.this.startActivity(intent);
            }
        });*/



        HashMap<String, String> user = session.getUserDetails();

        String mid = user.get(SessionManager.KEY_MID);

       // ImageButton editBtn = (ImageButton) findViewById(R.id.editMenu);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Downloading data from below url
        //final String url = "http://javatechig.com/?json=get_recent_posts&count=45";

        url += mid;

        new AsyncHttpTask().execute(url);
    }


    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("swipetest", "" + url);
                //new AsyncHttpTask().execute(url);
                new AsyncHttpTask().execute(url);
                /*Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);*/
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
        }



        @Override
    protected void onResume() {
        super.onResume();
        Log.d("test", "The onResume() event");
    }



/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @SuppressLint("NewApi")
    private void CreateMenu(Menu menu)
    {
        MenuItem menu1=menu.add(0,0,0,"AddItem");
        {
            menu1.setIcon(R.drawable.addlistfilled);
            menu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                MainActivity.this.finish();
                return true;
            case 0:
                isInternetPresent = cd.isConnectingToInternet();
                if(isInternetPresent) {
                    Toast.makeText(getApplicationContext(), "Add Item into Menu", Toast.LENGTH_SHORT).show();
                    Intent add = new Intent(getApplicationContext(), AddItem.class);
                    startActivity(add);

                    return true;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(MainActivity.this);
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
                    Log.d("str",response.toString());
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
                adapter = new MyMenuRecyclerAdapter(MainActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);
            } else {

                SharedPreferences sharedPrefs = getSharedPreferences("MyPrefsMenu", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPrefs.getString(TAG, null);
                //feedsList = new ArrayList<>();
                Type type = new TypeToken<ArrayList<FeedItem>>() {}.getType();
                List<FeedItem> feedsList = gson.fromJson(json,type);

                adapter = new MyMenuRecyclerAdapter(MainActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "Failed to fetch data! Not Connected to Internet", Toast.LENGTH_SHORT).show();
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
                FeedItem item = new FeedItem();
                Log.d("val",post.getString("item"));
                item.setTitle(post.optString("item"));
                item.setPrice(post.optString("price"));
                item.setMenuId(post.optString("menu_id"));
                item.setCategory(post.optString("cat"));
               /* item.setThumbnail(post.optString("thumbnail"));
*/
                feedsList.add(item);

                SharedPreferences sharedPrefs = this.getSharedPreferences("MyPrefsMenu", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(feedsList);
                Log.d("offline", "" + json);
                editor.putString(TAG, json);
                editor.commit();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}

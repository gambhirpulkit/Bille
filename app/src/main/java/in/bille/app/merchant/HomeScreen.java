package in.bille.app.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Bills";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
   // private LinearLayoutManager layoutmanager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
   // private ProgressBar progressBar;

    TextView merchName,merchEmail;
    ImageView merchImage;
    Bitmap bitmap;


    SessionManager session;
    private int backpresscount = 0;

    String url = Config.url+"billing_merchant.php?mid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeHomeScreen);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.d("swipetest", "refresh");
                refreshContent();
            }
        });



        /*String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);*/
        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.takeorder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createbill = new Intent(getApplicationContext(),CreateBill.class);
                startActivity(createbill);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_home_screen, null);
        navigationView.addHeaderView(header);

        merchName = (TextView)header.findViewById(R.id.MerchantName);
        merchEmail = (TextView)header.findViewById(R.id.MerchantEmail);
        merchImage = (ImageView)header.findViewById(R.id.MerchantImg);

        /*merchEmail.setTypeface(tf);
        merchName.setTypeface(tf);*/


        boolean check = session.checkLogin();

        if(check)
        {
            this.finish();
        }
        else
        {

        }

      //  boolean c = session.isLoggedIn();


        //Log.d("testit",""+merchemail);
        HashMap<String, String> user = session.getUserDetails();

        String merchemail = user.get(SessionManager.KEY_Email);

        // email
        String merchname = user.get(SessionManager.KEY_MerchantName);

        String imgurl = user.get(SessionManager.KEY_LogoUrl);

        String mid = user.get(SessionManager.KEY_MID);
        Log.d("checkmid", "" + mid);

        merchName.setText(merchname);
        merchEmail.setText(merchemail);

        new LoadImage().execute(imgurl);


        /*if(c)
        {
            String sharedname,sharedemail;
           sharedname = sharedpreferences.getString(Name,"");
           sharedemail = sharedpreferences.getString(Email,"");
            merchName.setText(sharedname);
            merchEmail.setText(sharedemail);
        }
        else {
            Log.d("click", "clicked");
            merchName.setText(merchname);
            merchEmail.setText(merchemail);
            //  setContentView(merchEmail);
        }*/



       // layoutmanager = new (this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

          // progressBar = (ProgressBar) findViewById(R.id.progress_bar);
       // progressBar.setVisibility(View.VISIBLE);
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
                mSwipeRefreshLayout.setRefreshing(false);
                /*Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);*/

            }
        },3000);
    }
    /*void refreshItems()
    {
        new AsyncHttpTask().execute(url);
        onItemsLoadComplete();
    }

    void onItemsLoadComplete()
    {
        mSwipeRefreshLayout.setRefreshing(false);
    }*/

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
    @Override
    public void onBackPressed() {

        /*if (backpresscount==1) {
            Toast.makeText(getApplicationContext(),"Press again to exit.",Toast.LENGTH_SHORT);
        }
        else if(backpresscount==2)
        {
            this.finish();
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {

            case R.id.action_settings:
                return true;

            case R.id.action_logout:
                session.logoutUser();
                return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent about = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(about);
           // HomeScreen.this.finish();
            // Handle the camera action
        } else if (id == R.id.nav_bills) {

        } else if (id == R.id.nav_TandC) {

            Intent about = new Intent(getApplicationContext(),TermsPrivacy.class);
            startActivity(about);
            HomeScreen.this.finish();
        }  else if (id == R.id.navAboutus) {

            Intent about = new Intent(getApplicationContext(),AboutUs.class);
            startActivity(about);
            HomeScreen.this.finish();
        } else if (id == R.id.navLogout) {

            session.logoutUser();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> implements Serializable {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
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
            // Download complete. Let us update UI
           // progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MyRecyclerAdapter(HomeScreen.this, feedsList);
                mRecyclerView.setAdapter(adapter);
            } else {
                SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPrefs.getString(TAG, null);
                //feedsList = new ArrayList<>();
                Type type = new TypeToken<ArrayList<FeedItem>>() {}.getType();
                List<FeedItem> feedsList = gson.fromJson(json,type);

                adapter = new MyRecyclerAdapter(HomeScreen.this, feedsList);
                mRecyclerView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(HomeScreen.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("bill");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
                Log.d("val", post.getString("c_name"));
                item.setBillId(post.optString("bill_id"));
                item.setPhone(post.optString("customer_phone"));
                item.setTitle(post.optString("c_name"));
                item.setPrice(post.optString("amount"));
                item.setDate(post.optString("date"));
               /* item.setThumbnail(post.optString("thumbnail"));
*/
                feedsList.add(item);


                SharedPreferences sharedPrefs = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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



    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                merchImage.setImageBitmap(image);


            }else{


                merchImage.setImageResource(R.drawable.defaultauthorimage);

            }
        }
    }


}

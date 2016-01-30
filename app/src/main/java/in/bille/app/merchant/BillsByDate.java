package in.bille.app.merchant;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BillsByDate extends AppCompatActivity implements View.OnClickListener{

    ProgressDialog mProgressDialog;
    Connectiondetector cd;
    Boolean isInternetPresent = false;
    private static final String TAG = "Menu";
    private List<FeedItem> feedsList;
    private MyBillsByDateAdapter adapter;
    private RecyclerView mRecyclerView;
    String startdate,enddate;
    EditText start,end;
    Integer flag=0;
    Calendar myCalendar = Calendar.getInstance();
    ImageButton load;
    SessionManager session;
    private ProgressBar progressBar;
    String url = Config.url+"sales_date.php?mid=";
    String checkerror="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_by_date);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sales");

        cd = new Connectiondetector(getApplicationContext());

        session = new SessionManager(getApplicationContext());


        HashMap<String, String> user = session.getUserDetails();

        String mid = user.get(SessionManager.KEY_MID);

        load = (ImageButton)findViewById(R.id.dateSubmit);

        load.setOnClickListener(this);

        start = (EditText)findViewById(R.id.startDateText);
        end = (EditText)findViewById(R.id.endDateText);

        start.setFocusable(false);
        end.setFocusable(false);

        final   DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = 1;
                new DatePickerDialog(BillsByDate.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = 2;
                new DatePickerDialog(BillsByDate.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        url += mid;

    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(flag==1)
        {
            start.setText(sdf.format(myCalendar.getTime()));
        }
        else if(flag==2) {
            end.setText(sdf.format(myCalendar.getTime()));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                BillsByDate.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        isInternetPresent = cd.isConnectingToInternet();

        if(isInternetPresent) {
            startdate = start.getText().toString();
            enddate = end.getText().toString();

            Log.d("startdate", "" + startdate);

            url += "&start_date=" + startdate + "&end_date=" + enddate;
            Log.d("startdate",""+url);

            new AsyncHttpTask().execute(url);

        }else
        {
            Toast.makeText(getApplicationContext(), "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
        }



    }


    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(BillsByDate.this);
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
                adapter = new MyBillsByDateAdapter(BillsByDate.this, feedsList);
                mRecyclerView.setAdapter(adapter);
            } else {

                /*SharedPreferences sharedPrefs = getSharedPreferences("MyPrefsMenu", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPrefs.getString(TAG, null);
                //feedsList = new ArrayList<>();
                Type type = new TypeToken<ArrayList<FeedItem>>() {}.getType();
                List<FeedItem> feedsList = gson.fromJson(json,type);

                adapter = new MyMenuRecyclerAdapter(MainActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);*/

                Toast.makeText(BillsByDate.this, "Failed to fetch data! Not Connected to Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            checkerror = response.optString("error");

            if(checkerror.matches("true")) {

                Toast.makeText(BillsByDate.this, "No Bills!", Toast.LENGTH_SHORT).show();

            }else {

            JSONArray posts = response.optJSONArray("customer");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
               // Log.d("val",post.getString("item"));
                item.setTitle(post.optString("name"));
                item.setPrice(post.optString("total"));
                item.setPhone(post.optString("phone"));
                item.setDate(post.optString("datetime"));
               /* item.setThumbnail(post.optString("thumbnail"));
*/
                feedsList.add(item);

                /*SharedPreferences sharedPrefs = this.getSharedPreferences("MyPrefsMenu", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(feedsList);
                Log.d("offline", "" + json);
                editor.putString(TAG, json);
                editor.commit();*/

            }
        } }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

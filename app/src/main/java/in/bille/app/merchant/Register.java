package in.bille.app.merchant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener{
    ProgressDialog mProgressDialog;


    String email = "", companyname = "", name = "",mobile="";

    EditText Name,Companyname,Email,Mobile;
    String sign;
    SessionManager session;
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        session = new SessionManager(getApplicationContext());
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
      //  toolbar.setBackgroundColor(Color.GREEN);



        Name = (EditText)findViewById(R.id.editText_name);
        Companyname = (EditText)findViewById(R.id.editText_company);
        Email = (EditText)findViewById(R.id.editText_email);
        Mobile = (EditText)findViewById(R.id.editText_mobile);

        Submit = (Button)findViewById(R.id.button_submit);

        Submit.setOnClickListener(this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        boolean flag = true;


        // find the radiobutton by returned id


        name = Name.getText().toString();
        email = Email.getText().toString();
        companyname = Companyname.getText().toString();
        mobile = Mobile.getText().toString();

        if (name.equals("") || name == "") {
            flag = false;
            Toast.makeText(getApplicationContext(),
                    "Name is required.", Toast.LENGTH_SHORT).show();
            Name.setFocusable(true);

        } else if (email.equals("") || email == "") {
            flag = false;
            Toast.makeText(getApplicationContext(),
                    "Email is required.", Toast.LENGTH_SHORT).show();
            Email.setFocusable(true);

        } else if (email.contains("@") != true) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Invalid email.",
                    Toast.LENGTH_SHORT).show();
            Email.setFocusable(true);

        } else if (companyname.equals("")) {
            flag = false;
            Toast.makeText(getApplicationContext(),
                    "Company Name is required.", Toast.LENGTH_SHORT).show();
            Companyname.setFocusable(true);

        }else if (mobile.equals("")||mobile.length()<10) {
            flag = false;
            Toast.makeText(getApplicationContext(),
                    "Enter the correct mobile number.", Toast.LENGTH_SHORT).show();
            Mobile.setFocusable(true);
        }

        if(flag)
        {
            String url;
             Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_SHORT).show();
            url = Config.url+"signup_mer.php?name="+name+"&company="+companyname+"&phone="+mobile+"&email="+email + "&token"+Splash.sign;
            // Uri.parse(url);
            Log.d("url","url");

            new ReadJSONFeedTask().execute(url);
        }


    }

    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
        } catch (Exception e) {

        }
        return stringBuilder.toString();
    }

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Register.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();


        }

        @Override
        protected String doInBackground(String... urls)
        {
            return readJSONFeed(urls[0]);
        }
        protected void onPostExecute(String result)
        { try {
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
            try
            {
                JSONObject jsonObject= new JSONObject(result);
                //  JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                Log.d("test", "test");
                // Toast.makeText(getApplicationContext(), jsonObject.getString("error"),Toast.LENGTH_LONG).show();

                String checkerror = jsonObject.getString("error");

                if(checkerror.equals("false"))
                {
                    Toast.makeText(getApplicationContext(),"Request Submitted Successfully",Toast.LENGTH_LONG).show();
                }




    			/*for(int i=0;i<jsonArray.length();i++)
    			{
    				JSONObject jsonObject=jsonArray.getJSONObject(i);
    				Toast.makeText(getApplicationContext(), jsonObject.getString("survId"),Toast.LENGTH_LONG).show();
    			}*/
                // Log.d("string",jsonArray.toString());
            }
            catch(Exception e)
            {

            }
        }
    }


}

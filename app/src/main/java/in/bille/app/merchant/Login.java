package in.bille.app.merchant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Login extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog mProgressDialog;

    Connectiondetector cd;
    Boolean isInternetPresent = false;

    Config con;
    EditText username,password;
    Button login;
    SessionManager session;
    String useremail,pass;
    private boolean clicked = false;

    TextView reg;
    String url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        cd = new Connectiondetector(getApplicationContext());
  // toolbar.setBackgroundColor(Color.GREEN);
       /* String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);*/

        username = (EditText)findViewById(R.id.edit_username);
        password = (EditText)findViewById(R.id.edit_password);

        login = (Button)findViewById(R.id.buttonLogin);

        reg = (TextView)findViewById(R.id.textView5);
        //reg.setTypeface(tf);
        reg.setText(Html.fromHtml("<u>Not a member?Register Now</u>"));

       // register = (Button)findViewById(R.id.button_register);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);

            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        login.setOnClickListener(this);

    }

    /*@Override
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
            *//*Log.d("sdhs","kgfogfow");
            Intent i = getIntent();
            String merchemail = i.getStringExtra("email");
            Log.d("testit",""+merchemail);
            merchEmail.setText(merchemail);*//*
            //session.checkLogin();
        }

    }*/

    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();

        if(isInternetPresent) {
            useremail = username.getText().toString();
            pass = password.getText().toString();


            boolean flag = true;

            if (useremail.equals("")) {
                flag = false;
                Toast.makeText(getApplicationContext(), "Email is required.",
                        Toast.LENGTH_SHORT).show();
            } else if (useremail.contains("@") != true) {
                flag = false;
                Toast.makeText(getApplicationContext(), "Invalid email.",
                        Toast.LENGTH_SHORT).show();
            } else if (useremail.contains(".") != true) {
                flag = false;
                Toast.makeText(getApplicationContext(), "Invalid email.",
                        Toast.LENGTH_SHORT).show();
            } else if (pass.equals("")) {
                flag = false;
                Toast.makeText(getApplicationContext(), "Password is required.",
                        Toast.LENGTH_SHORT).show();
            }

            if (flag) {
                //  Toast.makeText(getApplicationContext(),""+Email,Toast.LENGTH_LONG).show();

                try {

                    url = Config.url + "login_mer.php?user=" + useremail + "&pwd=" + URLEncoder.encode(pass, "UTF-8") + "&reg_id=" + Splash.regid;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //  Toast.makeText(getApplicationContext(),""+url,Toast.LENGTH_SHORT).show();
                new ReadJSONFeedTask().execute(url);
                Log.d("urlaaaaaaaaaaas", url);
            }
            clicked = true;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
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

    public class ReadJSONFeedTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Login.this);
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
            //Log.d("backg","yo");
            return readJSONFeed(urls[0]);
        }
        protected void onPostExecute(String result)
        {   try {
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
                String checkerror = jsonObject.optString("error");
                Log.d("error", checkerror);


                if(checkerror.matches("true"))
                {
                    Log.d("error","in if");
                    Toast.makeText(getApplicationContext(),"User not Registered. Please send us a registration request.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                String checkemail = jsonObject1.getString("email");
                String checkMID = jsonObject1.getString("m_id");
                String merchlogo = jsonObject1.getString("logo");
                String merchname = jsonObject1.getString("name");
                Log.d("email",checkemail);

                    Log.d("test", "login");
                    //SharedPreferences.Editor editor = sharedpreferences.edit();
                    Toast.makeText(getApplicationContext(),"WELCOME",Toast.LENGTH_SHORT).show();
                    session.createLoginSession(checkemail, merchname, checkMID, merchlogo);



                    Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                    startActivity(i);
                    Login.this.finish();

                    //editor.putBoolean(IS_LOGIN, true);
                    // editor.putString(Password1,Password);
                   /* editor.putString(Email1, checkemail);
                    editor.putString(MID,checkMID);

                    editor.commit();

                    String show = sharedpreferences.getString(Email1,checkemail);*/
                    // Toast.makeText(getApplicationContext(),"WELCOME",Toast.LENGTH_LONG).show();
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
                Log.d("",""+e.toString());
            }
        }
    }

}

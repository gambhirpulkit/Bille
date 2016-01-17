package in.bille.app.merchant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class CreateQuickBill extends AppCompatActivity {


    EditText phone,amt,desc;
    Button send;
    SessionManager session;
    private String checkoutUrl = "";

    Connectiondetector cd;
    Boolean isInternetPresent = false;
    private String apiUrl = Config.url;
    private String phonestring;
    private String descstring;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quick_bill);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Quick Bill");

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();

        final String mid = user.get(SessionManager.KEY_MID);
        cd = new Connectiondetector(getApplicationContext());



        phone = (EditText)findViewById(R.id.quickbillPhoneEditText);
        amt = (EditText)findViewById(R.id.quickbillAmountEditText);
        desc = (EditText)findViewById(R.id.quickbillTextEditText);

        send = (Button)findViewById(R.id.quickbillButton);

        send.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    send.setBackground(getResources().getDrawable(R.drawable.pressed));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // set to normal color
                    send.setBackground(getResources().getDrawable(R.drawable.button));
                    phonestring = phone.getText().toString();
                    descstring = desc.getText().toString();
                    amount = amt.getText().toString();

                    if (phonestring.matches("") || phonestring.length()!= 10) {
                        Toast.makeText(CreateQuickBill.this, "Please Enter the Mobile No.", Toast.LENGTH_SHORT).show();
                    } else if (amount.matches("")) {
                        Toast.makeText(CreateQuickBill.this, "Please Enter the Bill Amount", Toast.LENGTH_SHORT).show();
                    } else if (descstring.matches("")) {
                        Toast.makeText(CreateQuickBill.this, "Please Enter the Bill Description", Toast.LENGTH_SHORT).show();
                    } else {
                        isInternetPresent = cd.isConnectingToInternet();
                        Log.d("test", isInternetPresent.toString());
                        if (isInternetPresent) {
                            try {

                                checkoutUrl = apiUrl + "billing.php?mid=" + mid + "&phone=" + phonestring + "&text=" + URLEncoder.encode(descstring, "UTF-8") + "&price=" + amount + "&type=custom";
                                Log.d("checkoutUrl", checkoutUrl);
                            }catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            new VerifyBill().execute();
                            send.setEnabled(false);




                        } else if (!isInternetPresent) {
                            Toast.makeText(CreateQuickBill.this, "Not Connected to the Internet!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }

                return true;
            }
        });






    }


    private class VerifyBill extends AsyncTask<Void, Void, Void> {

        private Integer flag = 0;
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(checkoutUrl, ServiceHandler.GET);

            Log.d("Response", "<" + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    String err = jsonObj.getString("error");
                    String msg = jsonObj.getString("msg");
                    Log.d("err",err);


                    if(err.equals("false")) {
                        flag = 1;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Could not get data");
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


/*            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected void onPostExecute(Void result) {
            if(flag == 1) {
                Toast.makeText(getApplicationContext(), "Bill sent", Toast.LENGTH_LONG).show();
                Intent intentBack = new Intent(getApplicationContext(),HomeScreen.class);
                intentBack.putExtra("desc",descstring);
                startActivity(intentBack);
                CreateQuickBill.this.finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Error generating bill", Toast.LENGTH_LONG).show();
            }


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                CreateQuickBill.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

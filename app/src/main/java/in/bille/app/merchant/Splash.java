package in.bille.app.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import io.fabric.sdk.android.Fabric;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Splash extends AppCompatActivity {

    SessionManager session;
    ImageView image;
    TextView text,tag;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    public static String sign = "";
    Context context;
    // Connection detector class
    Connectiondetector cd;

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    String SENDER_ID = "977548678851";
    GoogleCloudMessaging gcm;
    public static String regid;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageManager pm = this.getPackageManager();
        String packageName = this.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;

        PackageInfo packageInfo = null;

        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;

        byte[] cert = signatures[0].toByteArray();

        InputStream input = new ByteArrayInputStream(cert);

        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");


        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }


        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getPublicKey().getEncoded());


            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<publicKey.length;i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]);
                if(appendString.length()==1)hexString.append("0");
                hexString.append(appendString);
            }

            sign = hexString.toString();
            Log.d("Example", "Cer: "+ hexString.toString());

        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }


        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        String fontPath = "fonts/Walkway_Oblique.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        cd = new Connectiondetector(getApplicationContext());
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
       // image = (ImageView)findViewById(R.id.imageView2);
        text = (TextView)findViewById(R.id.textView2);
        tag = (TextView)findViewById(R.id.textView14);
        //text.setTypeface(tf);
        tag.setTypeface(tf);
      //  image.setVisibility(View.VISIBLE);
        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(this);
        new RegisterBackground().execute();

       /* TranslateAnimation slide = new TranslateAnimation(0, 0, 100, 0);
        slide.setDuration(5000);
        slide.setFillAfter(true);
        image.startAnimation(slide);


        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2000);
        text.startAnimation(anim);*/

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        isInternetPresent = cd.isConnectingToInternet();
        Log.d("test", isInternetPresent.toString());



        if(isInternetPresent) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent it = new Intent(getApplicationContext(),
                            HomeScreen.class);

                    startActivity(it);

                    Splash.this.finish();

                }
            }, 3000);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent it = new Intent(getApplicationContext(),
                            HomeScreen.class);

                    startActivity(it);

                    Splash.this.finish();
                    Toast.makeText(getApplicationContext(),"Not connected to the internet",Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        }
    }

    class RegisterBackground extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }

                regid = gcm.register(SENDER_ID);
                Log.d("regid",""+regid);
                msg = "Device registered, registration ID=" + regid;
                sendRegistrationIdToBackend(regid);

            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            Log.d("checking",msg);
           // String regid;
        }
        private void sendRegistrationIdToBackend(String storeregid)
        {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://158.85.122.170:81/UI/notif/start_notify.php");
//            session.storeregid(storeregid);


            // Add your data
                /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("id", regid));
                nameValuePairs.add(new BasicNameValuePair("type","android"));
                nameValuePairs.add(new BasicNameValuePair("imei",imei));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);*/

            System.out.print("here is regid"+ regid);


            /*} catch (ClientProtocolException e) {
                System.out.print("here is exception"+ e);

                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }*/



        }



    }


}

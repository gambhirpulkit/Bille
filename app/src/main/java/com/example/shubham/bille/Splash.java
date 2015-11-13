package com.example.shubham.bille;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    SessionManager session;
    ImageView image;
    TextView text;
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    Connectiondetector cd;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        cd = new Connectiondetector(getApplicationContext());
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        image = (ImageView)findViewById(R.id.imageView2);
        text = (TextView)findViewById(R.id.textView2);

        image.setVisibility(View.VISIBLE);




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
                            NotConnected.class);
                    startActivity(it);

                    Splash.this.finish();
                    Toast.makeText(getApplicationContext(),"Not connected to the internet",Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        }
    }




}

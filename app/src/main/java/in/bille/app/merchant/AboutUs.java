package in.bille.app.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AboutUs extends AppCompatActivity {

    TextView appname,ver,rights,create;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        //for applying font
        /*String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        appname = (TextView)findViewById(R.id.textView);
        ver = (TextView)findViewById(R.id.textView7);
        rights = (TextView)findViewById(R.id.textView8);
        create = (TextView)findViewById(R.id.textView9);


        /*appname.setTypeface(tf);
        ver.setTypeface(tf);
        rights.setTypeface(tf);
        create.setTypeface(tf);*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.contactsprompt, null);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setView(promptsView);

                final ImageButton mail = (ImageButton) promptsView
                        .findViewById(R.id.imageButton_MailUs);

                final ImageButton call = (ImageButton)promptsView.findViewById(R.id.imageButton_CallUs);


                mail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(Intent.ACTION_SEND);


                        Intent email_intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "shubhamsaxena.msit@gmail.com", null));

                        startActivity(Intent.createChooser(email_intent, "Send Query"));
                    }
                });

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:123456789"));
                            startActivity(callIntent);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"Your call has failed...", Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }

                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();


                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                AboutUs.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

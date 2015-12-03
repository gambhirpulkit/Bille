package in.bille.app.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NotConnected extends AppCompatActivity implements View.OnClickListener{


    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    Connectiondetector cd;

    Button retry;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_connected);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cd = new Connectiondetector(getApplicationContext());

        retry = (Button)findViewById(R.id.buttonRetry);

        retry.setOnClickListener(this);

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
    public void onClick(View v) {

        isInternetPresent = cd.isConnectingToInternet();

        if(isInternetPresent)
        {
            Intent it = new Intent(getApplicationContext(),
                    Splash.class);
            startActivity(it);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Not Connected To The Internet",Toast.LENGTH_SHORT).show();
        }
    }
}

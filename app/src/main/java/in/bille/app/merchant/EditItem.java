package in.bille.app.merchant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EditItem extends AppCompatActivity implements View.OnClickListener {
ProgressDialog mProgressDialog;
    String foodcat;
    CheckBox veg, nonveg;
    //private Static String url;
    private static String url = "";
    String menuid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Item");

        if(getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        veg = (CheckBox)findViewById(R.id.editItemvegcheckBox);
        nonveg = (CheckBox)findViewById(R.id.editItemnonvegcheckBox);

        veg.setOnClickListener(this);
        nonveg.setOnClickListener(this);


        Intent i = getIntent();
        final String menu_id = i.getStringExtra("menu_id");
        final String item_name = i.getStringExtra("item_name");
        final String item_price = i.getStringExtra("item_price");
        final String foodcatg = i.getStringExtra("foodcatg");
        menuid += menu_id;

        Log.d("foodcategory",""+foodcatg);

        if(foodcatg.matches("vg"))
        {
            veg.setChecked(true);
        }
        else if(foodcatg.matches("nvg"))
        {
            nonveg.setChecked(true);
        }

        final EditText itemName = (EditText) findViewById(R.id.editItem);
        final EditText itemPrice = (EditText) findViewById(R.id.editPrice);
        // Log.d(itemName.getText().toString(),"text");
        Button savechanges = (Button) findViewById(R.id.saveChanges);

        itemName.setText(item_name);
        itemPrice.setText(item_price);

        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String iName = itemName.getText().toString();
                final String iPrice = itemPrice.getText().toString();
                if (iName.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter valid item name", Toast.LENGTH_SHORT).show();
                }
                if (iPrice.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter valid price", Toast.LENGTH_SHORT).show();
                }
                if (iName.length() > 0 && iPrice.length() > 0) {
                   try {
                       url = url + Config.url + "edit_menu.php?menu_id=" + menu_id + "&item=" + URLEncoder.encode(iName, "UTF-8") + "&price=" + iPrice + "&cat=" + foodcat;
                   }catch (UnsupportedEncodingException e) {
                       e.printStackTrace();
                   }
                       new EditBill().execute();
                }

                Log.d("url", url);

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.editItemvegcheckBox)
        {
            if(veg.isChecked()) {
                foodcat = "vg";
                nonveg.setChecked(false);
            }
        }
        else if(v.getId()==R.id.editItemnonvegcheckBox)
        {
            if(nonveg.isChecked()) {
                foodcat = "nvg";
                veg.setChecked(false);
            }
        }



    }

    private class EditBill extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response", "<" + jsonStr);

            if (jsonStr != null) {
                try {
                    Integer result = 0;
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    String err = jsonObj.getString("error");
                    String msg = jsonObj.getString("msg");
                    Log.d("err",err);

                    if(err.equals("false")) {
                        Log.d("err1", err);
                        result = 1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
            mProgressDialog = new ProgressDialog(EditItem.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }






        @Override
        protected void onPostExecute(Void result) {
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

            url = "";
            Toast.makeText(getApplicationContext(), "Menu Updated", Toast.LENGTH_SHORT).show();
            Intent intentBack = new Intent(EditItem.this,MainActivity.class);
            EditItem.this.startActivity(intentBack);
            EditItem.this.finish();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @SuppressLint("NewApi")
    private void CreateMenu(Menu menu)
    {
        MenuItem menu1=menu.add(0,0,0,"DeleteItem");
        {
            menu1.setIcon(R.drawable.delete);
            menu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home :
                super.onBackPressed();
                EditItem.this.finish();
                return true;
            case 0:
                Toast.makeText(getApplicationContext(), "Delete Item from Menu", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder b=new AlertDialog.Builder(this);
                b.setMessage("Do you really want to delete the item from the menu?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub

                                url += Config.url+"del_menu.php?menu_id="+menuid;

                                Log.d("checkit",""+url);
                                new EditBill().execute();


                                finish();


                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub

                                //Action for no button
                                arg0.cancel();

                            }
                        });

                AlertDialog alert = b.create();
                alert.setTitle("DELETE CONFIRMATION");//sets title of the dialog box
                alert.show();



                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

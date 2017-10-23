package com.andarpoblacion.andrade.andar;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResidentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private ArrayList<String> listItems=new ArrayList<String>();
    LayoutInflater li;
    View promptsView;
    private String[] street;
    private String stName;
    private TrackGPS gps;
    private double longitude, latitude;
    private String message;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragMan = getSupportFragmentManager();
            FragmentTransaction trans = fragMan.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_emergency:
                    trans.replace(R.id.content, new EmergencyFragment()).commit();
                    return true;
                case R.id.navigation_prepare:
                    trans.replace(R.id.content, new PrepareFragment()).commit();
                    return true;
                case R.id.navigation_update:
                    trans.replace(R.id.content, new UpdateFragment()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("ANDAR");

        //Toast.makeText(getApplicationContext(),"Welcome "+SharedPrefManager.getInstance(this).getUserName(),Toast.LENGTH_SHORT).show();


        //checkAndRequestPermissions();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send Alert Message to Admin and Emergency Contacts", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // get the complete name of the user
                // get the address
                // date and time
                //SharedPrefManager.getInstance(ResidentActivity.this).logout();
                sendAlert();

            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction trans = fragMan.beginTransaction();
        trans.replace(R.id.content, new EmergencyFragment()).commit();

        /*TwitterConfig config = new TwitterConfig.Builder(getApplicationContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("1h3yGRCRBld8xdNPenI8aDn21", "nE6uINZxUicaHCmhwH3hmkP5dAsjlX9rMD917L7Lgi6R7YaSs6"))
                .debug(true)
                .build();
        Twitter.initialize(config);*/


    }

    private void fetchClick(){
        gps = new TrackGPS(ResidentActivity.this);
        if(gps.canGetLocation()){
            longitude = gps.getLongitude();
            latitude = gps .getLatitude();
        }
        else
            gps.showSettingsAlert();

    }


    @Override
    public void onBackPressed() {
        exitApp();
        //super.onBackPressed();
    }

    public void sendAlert(){

        Resources resources = getResources();
        street = resources.getStringArray(R.array.loc);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, street);


        li = LayoutInflater.from(this);
        promptsView = li.inflate(R.layout.send_alert, null);

        final Spinner spn = (Spinner) promptsView.findViewById(R.id.spinner);

        if(isConnectedToInternet()){
            //Toast.makeText(this,"connected",Toast.LENGTH_LONG).show();
            spn.setVisibility(View.GONE);
            fetchClick();
        } else {
            //Toast.makeText(this,"not",Toast.LENGTH_LONG).show();
        }

        HintAdapter hintAdapter=new HintAdapter(this,R.layout.support_simple_spinner_dropdown_item,street);
        spn.setAdapter(hintAdapter);
        // show hint
        spn.setSelection(hintAdapter.getCount());

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stName = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setView(promptsView)
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        createMessage(stName);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Ask for help");
        alertDialog.show();
    }

    private void createMessage(String stName){
        Database db = new Database(this);

        Cursor res1 = db.getAllContact();

        listItems.add("09307814761");
        while (res1.moveToNext()){
            listItems.add(res1.getString(1));
        }

        for (int i = 0; i < listItems.size(); i++)
        {

            Calendar c = Calendar.getInstance();
            int minutes = c.get(Calendar.MINUTE);
            int hour = c.get(Calendar.HOUR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DATE);
            int ampm = c.get(Calendar.AM_PM);
            int year = c.get(Calendar.YEAR);
            String chk;
            if(ampm == 1)
                chk = "PM";
            else
                chk = "AM";

            if (hour == 0){
                hour = 12;
            }

            final String currDate = ""+month+"/"+day+"/"+year+"\n"+hour+":"+minutes+" "+chk;

            String name="",address="";

            Cursor res2 = db.getUserNameAddress();
            while (res2.moveToNext()){
                name = res2.getString(0);
                address = res2.getString(1);
            }

            if(isConnectedToInternet()){
                message = "ANDAR ALERT MESSAGE\n\n"
                        +name+" needs emergency response"
                        +"\n"+"LONGITUDE: " +longitude
                        +"\n"+"LATITUDE: " +latitude+" \n"
                        +currDate;
            } else {
                message = "ANDAR ALERT MESSAGE\n\n"
                        +name+" needs emergency response"
                        +"\n"+"LOCATION: " +stName+"\n"+currDate;
            }

            String tempMobileNumber = listItems.get(i).toString();

            //Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

            MultipleSMS(tempMobileNumber, message);

        }
    }

    private void MultipleSMS(String phoneNumber, final String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ContentValues values = new ContentValues();
                        for (int i = 0; i < listItems.size() - 1; i++) {
                            values.put("address", listItems.get(i).toString());
                            // txtPhoneNo.getText().toString());
                            values.put("body", message);

                        }
                        getContentResolver().insert(
                                Uri.parse("content://sms/sent"), values);
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Check Operator Services",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    public void exitApp(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Are you sure you want to exit the application?")
                .setCancelable(false)
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(ResidentActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("Exit", true);
                        startActivity(i);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Exit Application");
        alertDialog.show();
    }

    private boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(null!=ni){
            return true;
        } else {
            return false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.res_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.rate_this_app) {
            startActivity(new Intent(this,Rate.class));
            return true;
        } else if(id == R.id.resources) {
            startActivity(new Intent(this,References.class));
            return true;
        } else if(id == R.id.about) {
            startActivity(new Intent(this,About.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private  boolean checkAndRequestPermissions() {
        int readcont = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int sendsms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int writesettings = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS);
        int readphonestate = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int readsms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int netState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (readcont != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (sendsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (writesettings != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_SETTINGS);
        }
        if (readsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (readphonestate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (netState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}

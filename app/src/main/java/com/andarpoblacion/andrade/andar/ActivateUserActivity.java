package com.andarpoblacion.andrade.andar;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivateUserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText compNameEdt, phoneEdt, houseNoEdt, emailEdt, pwEdt;
    private Button activateBtn, declineBtn;
    private TextView bdayTxt;
    private ProgressDialog progressDialog;
    private String id="",data="",st="",sts="",cont="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_user);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Activate User");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        data = intent.getStringExtra("name");

        Toast.makeText(this, data,Toast.LENGTH_SHORT).show();

        compNameEdt = (EditText) findViewById(R.id.edtCompName);
        phoneEdt = (EditText) findViewById(R.id.edtPhoneNo);
        houseNoEdt = (EditText) findViewById(R.id.edtHouseNo);
        emailEdt = (EditText) findViewById(R.id.edtEmail);
        activateBtn = (Button) findViewById(R.id.btnActivate);
        declineBtn = (Button) findViewById(R.id.btnDecline);
        bdayTxt = (TextView) findViewById(R.id.txtBday);

        cont=phoneEdt.getText().toString();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        fetchData(data);

        activateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st = "ACTIVE";
                sts = "ACTIVATE";
                confirmAct(st, sts);

            }
        });
        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st = "DECLINE";
                sts = "DECLINE";
                confirmAct(st, sts);
            }
        });

    }

    public void fetchData(final String name){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {


                            if (TextUtils.isEmpty(response)){
                                noConnection();

                            } else {


                                JSONObject obj = new JSONObject(response);
                                id = ""+obj.getInt("id");
                                compNameEdt.setText(obj.getString("name"));
                                phoneEdt.setText(obj.getString("phone"));
                                houseNoEdt.setText(obj.getString("address"));
                                bdayTxt.setText(obj.getString("bday"));
                                emailEdt.setText(obj.getString("email"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ActivateUserActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();


                        if (TextUtils.isEmpty(error.getMessage())){
                            noConnection();
                        }

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                return params;
            }

        };

        RequestHandler.getInstance(ActivateUserActivity.this).addToRequestQueue(stringRequest);

    }



    public void updateUser(final String name, final String stats){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_UPDATE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (TextUtils.isEmpty(response)){
                                noConnection();

                            } else {


                                progressDialog.dismiss();
                                //sendSMSMessage();


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ActivateUserActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();


                        if (TextUtils.isEmpty(error.getMessage())){
                            noConnection();
                        }

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("status", stats);
                return params;
            }

        };

        RequestHandler.getInstance(ActivateUserActivity.this).addToRequestQueue(stringRequest);

    }

    public void noInternet(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("No internet connection...")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Error");
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

    private void noConnection(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Can't connect to server right now\nPlease try again later")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                ActivateUserActivity.this.finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }



    public void confirmAct(final String st, String sts){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivateUserActivity.this);
        alertDialogBuilder
                .setMessage("Are you sure you want to "+sts+" "+data+" ?")
                .setCancelable(false)
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String message = "You are now a verified user of ANDAR "+data+"\n\nUse your email and password to login.\nTHANK YOU AND GOD BLESS!!";
                        Toast.makeText(ActivateUserActivity.this,message,Toast.LENGTH_LONG).show();
                        MultipleSMS(
                                phoneEdt.getText().toString()
                                , message
                        );
                        updateUser(data,st);
                        finish();
                        startActivity(new Intent(ActivateUserActivity.this,RequestActivity.class));
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Confirmation");
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void MultipleSMS(final String phoneNumber, final String message) {
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

                        values.put("address", phoneNumber);
                        // txtPhoneNo.getText().toString());
                        values.put("body", message);

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


}
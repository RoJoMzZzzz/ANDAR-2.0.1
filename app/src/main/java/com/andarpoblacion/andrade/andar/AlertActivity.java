package com.andarpoblacion.andrade.andar;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlertActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private static AlertActivity inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    private ProgressDialog progressDialog;
    LayoutInflater li;
    View promptsView;
    private String[] names;
    private String rescName;

    public static AlertActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Alert Messages");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        smsListView = (ListView) findViewById(R.id.lvAlerts);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);
        smsListView.setSelector(R.drawable.bg_key);
        smsListView.setOnItemClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        refreshSmsInbox();
        getStringReq("RESCUER");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AlertActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody);
            if(str.contains("ANDAR ALERT MESSAGE"))
                arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
    }

    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        view.setSelected(true);
        try {
            String[] smsMessages = smsMessagesList.get(pos).split("\n");
            String address = smsMessages[0];
            String smsMessage = "";
            for (int i = 1; i < smsMessages.length; ++i) {
                smsMessage += smsMessages[i] + "\n";
                //Toast.makeText(this, smsMessage, Toast.LENGTH_SHORT).show();
            }

            String smsMessageStr = address + "\n";
            smsMessageStr += smsMessage;
            //Toast.makeText(this, smsMessageStr, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, smsMessage.trim(), Toast.LENGTH_SHORT).show();

            confirmationMessage(smsMessage, address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void confirmationMessage(final String message, final String number){

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, names);
        li = LayoutInflater.from(this);
        promptsView = li.inflate(R.layout.select_rescuer, null);

        final Spinner spn = (Spinner) promptsView.findViewById(R.id.spinner);


        spn.setAdapter(adp);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rescName = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setView(promptsView)
                .setCancelable(false)
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Send",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Context ctx = AlertActivity.this;
                        //deleteSMS(ctx,message,number);
                        getRescuerNumber(rescName,message);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Send Rescuer");
        alertDialog.show();
    }

    public void getStringReq(final String userType){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("resident");
                            if (TextUtils.isEmpty(response)){
                                noConnection();
                                //Toast.makeText(UsersActivity.this,"walang laman",Toast.LENGTH_LONG).show();
                            } else {
                                names = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    names[i] = jsonObject.getString("name");
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AlertActivity.this,e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("userType", userType);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void getRescuerNumber(final String nameRescuer, final String mensahe){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_RESCUER_NUMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject obj = new JSONObject(response);

                            if (TextUtils.isEmpty(response)){
                                noConnection();
                                //Toast.makeText(UsersActivity.this,"walang laman",Toast.LENGTH_LONG).show();
                            } else {
                                String numberResc = obj.getString("phone");
                                MultipleSMS(numberResc,mensahe);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AlertActivity.this,e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("name", nameRescuer);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

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


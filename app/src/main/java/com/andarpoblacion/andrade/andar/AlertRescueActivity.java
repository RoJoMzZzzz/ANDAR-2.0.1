package com.andarpoblacion.andrade.andar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AlertRescueActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private static AlertRescueActivity inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    private ProgressDialog progressDialog;
    LayoutInflater li;
    View promptsView;
    private String[] names;
    private String rescName;

    public static AlertRescueActivity instance() {
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
        setContentView(R.layout.activity_alert_rescue);

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

        smsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String  message = parent.getItemAtPosition(position).toString();

                if (message.contains("LONGITUDE")) {
                    String[] sp1 = message.split("ANDAR ALERT MESSAGE\n\n");
                    String[] sp2 = sp1[1].split(" needs emergency response");
                    String tulo = sp2[0];

                    String[] dataSplit = message.split("ANDAR ALERT MESSAGE\n\n"+tulo+" needs emergency response\nLONGITUDE: ");
                    String[] dataSplit1 = dataSplit[1].split("\n"+"LATITUDE: ");
                    String longi = dataSplit1[0];

                    String[] dataSplit2 = message.split("ANDAR ALERT MESSAGE\n\n"+tulo+" needs emergency response\nLONGITUDE: "+longi+"\n"+"LATITUDE: ");
                    String[] dataSplit3 = dataSplit2[1].split(" \n");
                    String lati = dataSplit3[0];


                    Intent editCont = new Intent(AlertRescueActivity.this, MapsActivity.class);
                    editCont.putExtra("lati", lati);
                    editCont.putExtra("longi", longi);
                    startActivity(editCont);

                    //Toast.makeText(AlertRescueActivity.this,"LATI : "+lati+"\nLONGI: "+longi, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AlertRescueActivity.this,"You don't have coordinates", Toast.LENGTH_SHORT).show();
                }


                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AlertRescueActivity.this.finish();
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

            String[] sp1 = smsMessage.trim().split("ANDAR ALERT MESSAGE\n\n");
            String[] sp2 = sp1[1].split("needs emergency response");
            String tulo = sp2[0];

            Toast.makeText(this, tulo, Toast.LENGTH_SHORT).show();

            Intent nameResident = new Intent(this, ReportActivity.class);
            nameResident.putExtra("name", tulo);
            startActivity(nameResident);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

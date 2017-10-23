package com.andarpoblacion.andrade.andar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EmergencyContacts extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView emContLv;
    private ArrayList<String> listItems=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private static final int PICK_CONTACT = 10;
    private EditText contEdt;
    private ImageButton addContBtn;
    private Button addFrmPhBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Emergency Contacts");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Database db = new Database(this);

        contEdt = (EditText)findViewById(R.id.edtAddCont);
        addContBtn = (ImageButton)findViewById(R.id.imgBtnAddCont);
        addFrmPhBtn = (Button)findViewById(R.id.btnAddFrPhone);

        addFrmPhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        addContBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contEdt.getText().toString().equals("") || contEdt.getText().toString().equals(" ")){

                    contEdt.setError("This item can't be empty");

                } else {


                    boolean insCont = db.insertContact(contEdt.getText().toString(), contEdt.getText().toString());
                    if (insCont){
                        //Toast.makeText(EmergencyContacts.this, "inserted", Toast.LENGTH_LONG).show();
                    }
                    else{
                        //Toast.makeText(EmergencyContacts.this, "not inserted", Toast.LENGTH_LONG).show();
                    }

                    finish();
                    startActivity(getIntent());
                }
            }
        });


        Cursor res = db.getAllContact();
        adapter=new ArrayAdapter<String>(this,
                R.layout.contacts,
                listItems);

        while (res.moveToNext()){
            listItems.add(res.getString(0)+"\n\t"+res.getString(1));
            adapter.notifyDataSetChanged();
        }

        emContLv = (ListView) findViewById(R.id.lvEmCont);
        emContLv.setAdapter(adapter);
        emContLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(EmergencyContacts.this, listItems.get(i), Toast.LENGTH_SHORT).show();

                Intent editCont = new Intent(EmergencyContacts.this, EditEmergencyContacts.class);
                editCont.putExtra("data", listItems.get(i));
                startActivity(editCont);

            }
        });

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        Database db = new Database(this);
        switch(reqCode)
        {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst())
                    {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                        {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();

                            String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

//                            editText.setText(nameContact+ " "+ cNumber);
                            boolean insCont = db.insertContact(nameContact, cNumber);
                            if(insCont){
                                //Toast.makeText(EmergencyContacts.this, "Inserted", Toast.LENGTH_LONG).show();
                            }

                            else{
                                //Toast.makeText(EmergencyContacts.this, "not inserted", Toast.LENGTH_LONG).show();
                        }

                            finish();
                            startActivity(getIntent());
                        }
                    }
                }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            EmergencyContacts.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(EmergencyContacts.this, ResidentActivity.class));
        super.onBackPressed();
    }
}

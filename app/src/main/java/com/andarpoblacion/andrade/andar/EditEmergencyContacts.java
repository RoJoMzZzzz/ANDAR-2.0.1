package com.andarpoblacion.andrade.andar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditEmergencyContacts extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText dispNameEdt, contNumEdt;
    private Button saveBtn, delBtn, cancBtn;
    private String disp, num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emergency_contacts);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Emergency Contacts");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Database db = new Database(this);

        dispNameEdt = (EditText) findViewById(R.id.edtDispName);
        contNumEdt = (EditText) findViewById(R.id.edtContNum);
        saveBtn = (Button) findViewById(R.id.btnSaveCont);
        delBtn = (Button) findViewById(R.id.btnDelCont);
        cancBtn = (Button) findViewById(R.id.btnCancelCont);


        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String[] dataSplit = data.split("\n\t");
        String data1 = dataSplit[0];
        String data2 = dataSplit[1];

        dispNameEdt.setText(data1);
        contNumEdt.setText(data2);

        disp = dispNameEdt.getText().toString();
        num = contNumEdt.getText().toString();

        cancBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditEmergencyContacts.this.finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!disp.equals(dispNameEdt.getText().toString())&& num.equals(contNumEdt.getText().toString())) {
                    //Toast.makeText(getApplicationContext(),"nagbago ang pangalan",Toast.LENGTH_SHORT).show();

                    boolean isupdate = db.updateDataEmContDisp(dispNameEdt.getText().toString(), contNumEdt.getText().toString());
                    /*if (isupdate){
                        Toast.makeText(getApplicationContext(),"Data updated", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(EditEmergencyContacts.this,EmergencyContacts.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Data not Updated",Toast.LENGTH_LONG).show();
                    }*/

                } else if (disp.equals(dispNameEdt.getText().toString())&& !num.equals(contNumEdt.getText().toString())){
                    //Toast.makeText(getApplicationContext(),"nagbago ang numero",Toast.LENGTH_SHORT).show();
                    boolean isupdate = db.updateDataEmContNum(dispNameEdt.getText().toString(), contNumEdt.getText().toString());
                    /*if (isupdate){
                        Toast.makeText(getApplicationContext(),"Data updated", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(EditEmergencyContacts.this,EmergencyContacts.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Data not Updated",Toast.LENGTH_LONG).show();
                    }*/
                } else {
                    //Toast.makeText(getApplicationContext(),"walang binago",Toast.LENGTH_SHORT).show();
                }
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditEmergencyContacts.this);
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this emergency contact?")
                        .setCancelable(false)
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Integer isDelete=db.deleteDataEmCont(contNumEdt.getText().toString());
                                if (isDelete>0){
                                    Toast.makeText(EditEmergencyContacts.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditEmergencyContacts.this,EmergencyContacts.class));
                                }
                                else {
                                    Toast.makeText(EditEmergencyContacts.this, "Not Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setTitle("Deleting Contact");
                alertDialog.show();


            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            EditEmergencyContacts.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

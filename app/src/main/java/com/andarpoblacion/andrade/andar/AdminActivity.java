package com.andarpoblacion.andrade.andar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Button alertsBtn, reportsBtn, usersBtn, requestsBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("ADMIN");

        alertsBtn = (Button) findViewById(R.id.btnAlert);
        reportsBtn = (Button) findViewById(R.id.btnReport);
        usersBtn = (Button) findViewById(R.id.btnUsers);
        requestsBtn = (Button) findViewById(R.id.btnRequest);

        alertsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,AlertActivity.class));
            }
        });

        reportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,ReportAdminActivity.class));
            }
        });

        usersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,UsersActivity.class));
            }
        });

        requestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,RequestActivity.class));
            }
        });


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
                        Intent i = new Intent(AdminActivity.this, LoginActivity.class);
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

    @Override
    public void onBackPressed() {
        exitApp();
        //super.onBackPressed();
    }
}

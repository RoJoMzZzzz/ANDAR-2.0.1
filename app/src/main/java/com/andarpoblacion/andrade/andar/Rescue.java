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

public class Rescue extends AppCompatActivity {

    private Toolbar toolbar;
    private Button alertBtn, reportBtn, tipsBtn, firstAidBtn, scannerBtn, flashlightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("RESCUER");

        alertBtn = (Button) findViewById(R.id.btnAlert);
        reportBtn = (Button) findViewById(R.id.btnReport);
        tipsBtn = (Button) findViewById(R.id.btnTips);
        firstAidBtn = (Button) findViewById(R.id.btnFirstAid);
        scannerBtn = (Button) findViewById(R.id.btnScanner);
        flashlightBtn = (Button) findViewById(R.id.btnFlashlight);

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rescue.this,AlertRescueActivity.class));
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rescue.this,RescueReportsActivity.class));
            }
        });

        tipsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rescue.this,PrepareActivity.class));
            }
        });

        firstAidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rescue.this,FirstAidActivity.class));
            }
        });

        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rescue.this,ScanQR.class));
            }
        });

        flashlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rescue.this,FlashlightActivity.class));
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
                        Intent i = new Intent(Rescue.this, LoginActivity.class);
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

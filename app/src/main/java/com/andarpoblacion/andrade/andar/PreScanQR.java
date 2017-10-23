package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PreScanQR extends AppCompatActivity {

    private Toolbar toolbar;
    private Button preScanBtn;
    private TextView resQRTxt;
    boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_scan_qr);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" QR CODE Scanner");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preScanBtn = (Button) findViewById(R.id.btnScan);
        resQRTxt = (TextView) findViewById(R.id.txtResultQR);

        preScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = true;
                startActivity(new Intent(PreScanQR.this, ScanQR.class));
                finish();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            PreScanQR.this.finish();
            startActivity(new Intent(PreScanQR.this,ResidentActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

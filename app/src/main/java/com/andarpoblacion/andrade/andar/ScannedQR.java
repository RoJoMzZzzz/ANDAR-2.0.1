package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScannedQR extends AppCompatActivity {

    private Toolbar toolbar;
    private Button preScanBtn;
    private TextView resQRTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_qr);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" QR CODE Scanner");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preScanBtn = (Button) findViewById(R.id.btnScan);
        resQRTxt = (TextView) findViewById(R.id.txtResultQR);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        resQRTxt.setText(data);

        preScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScannedQR.this, ScanQR.class));
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ScannedQR.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

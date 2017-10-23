package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PrepareActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button earthBtn, fireBtn, floodBtn, landBtn, tsunamiBtn, typBtn, volBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Disaster Tips");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        earthBtn = (Button) findViewById(R.id.btnEarthquake);
        fireBtn = (Button) findViewById(R.id.btnFire);
        floodBtn = (Button) findViewById(R.id.btnFlood);
        landBtn = (Button) findViewById(R.id.btnLandslide);
        tsunamiBtn = (Button) findViewById(R.id.btnTsunami);
        typBtn = (Button) findViewById(R.id.btnTyphoon);
        volBtn = (Button) findViewById(R.id.btnVolcano);

        earthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrepareActivity.this, Earth.class));
            }
        });

        fireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrepareActivity.this, Fire.class));
            }
        });

        floodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrepareActivity.this, Flood.class));
            }
        });

        landBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrepareActivity.this, Landslide.class));
            }
        });

        tsunamiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrepareActivity.this, Tsunami.class));
            }
        });

        typBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrepareActivity.this, Typhoon.class));
            }
        });

        volBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrepareActivity.this, Volcano.class));
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            PrepareActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class FirstAidActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private Button burnsbtn, cprBtn, cutsBtn, dislocationBtn, electricalBtn,
            fracturesBtn, heartAttackBtn, heatStrokeBtn, punctureBtn,
            severeBleedingBtn, spinalBtn, sprainBtn, strokeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("First Aid");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        burnsbtn = (Button) findViewById(R.id.btnBurns);
        burnsbtn.setOnClickListener(this);
        cprBtn = (Button) findViewById(R.id.btnCpr);
        cprBtn.setOnClickListener(this);
        cutsBtn = (Button) findViewById(R.id.btnCuts);
        cutsBtn.setOnClickListener(this);
        dislocationBtn = (Button) findViewById(R.id.btnDislocation);
        dislocationBtn.setOnClickListener(this);
        electricalBtn = (Button) findViewById(R.id.btnElectrical);
        electricalBtn.setOnClickListener(this);
        fracturesBtn = (Button) findViewById(R.id.btnFracture);
        fracturesBtn.setOnClickListener(this);
        heartAttackBtn = (Button) findViewById(R.id.btnHeartAttack);
        heartAttackBtn.setOnClickListener(this);
        heatStrokeBtn = (Button) findViewById(R.id.btnHeatStroke);
        heatStrokeBtn.setOnClickListener(this);
        punctureBtn = (Button) findViewById(R.id.btnPuncture);
        punctureBtn.setOnClickListener(this);
        severeBleedingBtn = (Button) findViewById(R.id.btnSevereBleeding);
        severeBleedingBtn.setOnClickListener(this);
        spinalBtn = (Button) findViewById(R.id.btnSpinalInjury);
        spinalBtn.setOnClickListener(this);
        sprainBtn = (Button) findViewById(R.id.btnSprain);
        sprainBtn.setOnClickListener(this);
        strokeBtn = (Button) findViewById(R.id.btnStroke);
        strokeBtn.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FirstAidActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnBurns:
                startActivity(new Intent(this,BurnsActivity.class));
                break;

            case R.id.btnCpr:
                startActivity(new Intent(this,CprActivity.class));
                break;

            case R.id.btnCuts:
                startActivity(new Intent(this,CutsActivity.class));
                break;

            case R.id.btnDislocation:
                startActivity(new Intent(this,DislocationActivity.class));
                break;

            case R.id.btnElectrical:
                startActivity(new Intent(this,ElectricalActivity.class));
                break;

            case R.id.btnFracture:
                startActivity(new Intent(this,FractureActivity.class));
                break;

            case R.id.btnHeartAttack:
                startActivity(new Intent(this,HeartAttackActivity.class));
                break;

            case R.id.btnHeatStroke:
                startActivity(new Intent(this,HeatStrokeActivity.class));
                break;

            case R.id.btnPuncture:
                startActivity(new Intent(this,PunctureActivity.class));
                break;

            case R.id.btnSevereBleeding:
                startActivity(new Intent(this,SeverActivity.class));
                break;

            case R.id.btnSpinalInjury:
                startActivity(new Intent(this,SpinalActivity.class));
                break;

            case R.id.btnSprain:
                startActivity(new Intent(this,SprainActivity.class));
                break;

            case R.id.btnStroke:
                startActivity(new Intent(this,StrokeActivity.class));
                break;

            default:
                break;
        }

    }
}

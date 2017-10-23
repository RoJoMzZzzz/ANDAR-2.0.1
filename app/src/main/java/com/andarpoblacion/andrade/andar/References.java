package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class References extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView refTxt, ndrrmcTxt, githubTxt, stackTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_references);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Resources");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refTxt = (TextView) findViewById(R.id.txtFirstAidApp);
        ndrrmcTxt = (TextView) findViewById(R.id.txtNdrrmcRes);
        githubTxt = (TextView) findViewById(R.id.txtGithubRes);
        stackTxt = (TextView) findViewById(R.id.txtStackOverFlowRes);

        refTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(refTxt.getText().toString()));
                startActivity(intent);
            }
        });

        ndrrmcTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(ndrrmcTxt.getText().toString()));
                startActivity(intent);
            }
        });

        githubTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(githubTxt.getText().toString()));
                startActivity(intent);
            }
        });

        stackTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(stackTxt.getText().toString()));
                startActivity(intent);
            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            References.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

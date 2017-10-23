package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Rate extends AppCompatActivity {

    private Toolbar toolbar;
    private RatingBar ratingBar;
    private TextView rateTxt;
    private EditText feedEdt;
    private Button sendFeedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Rate");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        rateTxt = (TextView) findViewById(R.id.txtRating);
        feedEdt = (EditText) findViewById(R.id.edtFeedback);
        sendFeedBtn = (Button) findViewById(R.id.btnSendRate);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateTxt.setText(""+rating+" stars");
            }
        });

        sendFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ratingBar.getRating() == 0){
                    Toast.makeText(Rate.this,"Please rate first",Toast.LENGTH_SHORT).show();
                } else if(feedEdt.getText().toString().trim().length() == 0){
                    feedEdt.setError("This item can't be empty!");
                } else {
                    String rateFeed = "Rate: "+ratingBar.getRating()+" stars\n"+"Feedback: "+feedEdt.getText().toString();
                    Toast.makeText(Rate.this,rateFeed, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"rojom91214@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "ANDAR USER'S RATE AND FEEDBACK");
                    i.putExtra(Intent.EXTRA_TEXT, rateFeed);
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(Rate.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Rate.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

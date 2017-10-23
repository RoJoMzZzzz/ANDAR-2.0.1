package com.andarpoblacion.andrade.andar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView one, two, three;
    private int j =0;
    private String userType="",status="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Database db = new Database(this);

        logo = (ImageView) findViewById(R.id.imageView);
        one = (TextView) findViewById(R.id.txtAndroid);
        two = (TextView) findViewById(R.id.txtNatural);
        three = (TextView) findViewById(R.id.txtAlert);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_fade_in_from_bottom);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        logo.setAnimation(anim);
        one.setAnimation(anim1);
        two.setAnimation(anim1);
        three.setAnimation(anim1);

        Cursor res = db.getUserType();
        while(res.moveToNext()) {
            userType = res.getString(0);
        }

        Timer RunSplash = new Timer();

        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {

                if(userType.equalsIgnoreCase("ADMIN")){
                    startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                } else if (userType.equalsIgnoreCase("RESIDENT")){
                    startActivity(new Intent(getApplicationContext(),ResidentActivity.class));
                } else if (userType.equalsIgnoreCase("RESCUER")) {
                    startActivity(new Intent(getApplicationContext(),Rescue.class));
                }
                else {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }

                finish();

            }
        };

        RunSplash.schedule(ShowSplash, 2000);

    }


}

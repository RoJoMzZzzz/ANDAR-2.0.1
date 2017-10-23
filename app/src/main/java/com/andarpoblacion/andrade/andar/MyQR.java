package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MyQR extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView myQrImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);

        final Database db = new Database(this);
        myQrImg = (ImageView) findViewById(R.id.imgMyQR);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My QR Code");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button gen = (Button) findViewById(R.id.btnGenQR);
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyQR.this,NewQR2.class));
            }
        });

        Cursor res = db.getMyQR();

        if (res.getCount() == 0) {
            myQrImg.setImageResource(R.drawable.andarlogo1);
        } else {
            while(res.moveToNext()){
                Bitmap myQRtoBitmap = getImage(res.getBlob(0));
                myQrImg.setImageBitmap(myQRtoBitmap);
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MyQR.this.finish();
            startActivity(new Intent(MyQR.this, ResidentActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyQR.this, ResidentActivity.class));
        finish();
        super.onBackPressed();
    }
}

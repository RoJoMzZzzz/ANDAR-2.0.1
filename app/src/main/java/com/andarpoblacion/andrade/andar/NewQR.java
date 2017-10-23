package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class NewQR extends AppCompatActivity {

    private Toolbar toolbar;
    private Button gen;
    private EditText name,address,others,bday;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_qr);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Generate QR Code");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Database db = new Database(this);

        gen = (Button) findViewById(R.id.btnGenQR2);
        name = (EditText) findViewById(R.id.edtName);
        address = (EditText) findViewById(R.id.edtAddress);
        others = (EditText) findViewById(R.id.edtOthers);
        bday = (EditText) findViewById(R.id.edtBday);

        TextWatcher tw = new TextWatcher() {

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    bday.setText(current);
                    bday.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        bday.addTextChangedListener(tw);







        
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteMyQr();


                    if(name.getText().toString().trim().length()==0)
                        name.setError("Please input your name");
                    else if(address.getText().toString().trim().length()==0)
                        address.setError("Please input your address");
                    else if(bday.getText().toString().trim().length()==0)
                        bday.setError("Please input your bday");
                    else {

                        String text2Qr = name.getText().toString() +"\n"+address.getText().toString() +"\n"+bday.getText().toString() +"\n"+others.getText().toString();
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                            byte[] myQRtoByte = getBytes(bitmap);

                        boolean ins = db.insertQR(myQRtoByte);

                        if (ins) {

                            //Toast.makeText(NewQR.this, "inserted", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(NewQR.this, MyQR.class));
                        } else{
                            //Toast.makeText(NewQR.this, "not inserted", Toast.LENGTH_LONG).show();
                        }


                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NewQR.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class NewQR2 extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText heightEdt, weightEdt, allergyEdt, cancerEdt, othersEdt;
    private Spinner bloodSpn;
    private ArrayAdapter bloodAdp;
    private CheckBox allergyChk, amoebiasisChk,anemiaChk, asthmaChk, cancerChk, chronicChk, diabetesChk, dysmenorrheaChk,
        faintingChk, earChk, endocrineChk, frequentChk, headChk, heartChk, herniaChk, highChk, insomiaChk,
        kidneyChk, mentalChk, nervousChk, noseChk, pepticChk, tuberculosisChk;
    private Button updateBtn;
    String bloodTypeStr = "", text2qr="", email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_qr2);

        final Database db = new Database(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Update QR Code");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //final Database db = new Database(this);

        heightEdt = (EditText) findViewById(R.id.edtHeight);
        weightEdt = (EditText) findViewById(R.id.edtWeight);
        allergyEdt = (EditText) findViewById(R.id.edtAllergy);
        cancerEdt = (EditText) findViewById(R.id.edtCancer);
        othersEdt = (EditText) findViewById(R.id.edtOthers);
        bloodSpn = (Spinner) findViewById(R.id.spnBloodType);
        allergyChk = (CheckBox) findViewById(R.id.chkAllergy);
        amoebiasisChk = (CheckBox) findViewById(R.id.chkAmoebiasis);
        anemiaChk = (CheckBox) findViewById(R.id.chkAnemia);
        asthmaChk = (CheckBox) findViewById(R.id.chkAsthma);
        cancerChk = (CheckBox) findViewById(R.id.chkCancer);
        chronicChk = (CheckBox) findViewById(R.id.chkChronic);
        diabetesChk = (CheckBox) findViewById(R.id.chkDiabetes);
        dysmenorrheaChk = (CheckBox) findViewById(R.id.chkDysmenorrhea);
        faintingChk = (CheckBox) findViewById(R.id.chkFainting);
        earChk = (CheckBox) findViewById(R.id.chkEar);
        endocrineChk = (CheckBox) findViewById(R.id.chkEndocrine);
        frequentChk = (CheckBox) findViewById(R.id.chkFrequent);
        headChk = (CheckBox) findViewById(R.id.chkHead);
        heartChk = (CheckBox) findViewById(R.id.chkHeart);
        herniaChk = (CheckBox) findViewById(R.id.chkHernia);
        highChk = (CheckBox) findViewById(R.id.chkHigh);
        insomiaChk = (CheckBox) findViewById(R.id.chkInsomia);
        kidneyChk = (CheckBox) findViewById(R.id.chkKidney);
        mentalChk = (CheckBox) findViewById(R.id.chkMental);
        nervousChk = (CheckBox) findViewById(R.id.chkNervous);
        noseChk = (CheckBox) findViewById(R.id.chkNose);
        pepticChk = (CheckBox) findViewById(R.id.chkPeptic);
        tuberculosisChk = (CheckBox) findViewById(R.id.chkTuberculosis);
        updateBtn = (Button) findViewById(R.id.btnUpdate);
        bloodSpn = (Spinner) findViewById(R.id.spnBloodType);


        Resources res = getResources();
        final String[] bloodTypes = res.getStringArray(R.array.blood);

        HintAdapter hintAdapter=new HintAdapter(this,R.layout.support_simple_spinner_dropdown_item,bloodTypes);
        bloodSpn.setAdapter(hintAdapter);
        // show hint
        bloodSpn.setSelection(hintAdapter.getCount());
        bloodSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodTypeStr = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        allergyChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    allergyEdt.setVisibility(View.VISIBLE);
                    allergyEdt.requestFocus();
                } else {
                    allergyEdt.setVisibility(View.GONE);
                }
            }
        });

        cancerChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cancerEdt.setVisibility(View.VISIBLE);
                    cancerEdt.requestFocus();
                } else {
                    cancerEdt.setVisibility(View.GONE);
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String medicalStr = "", heightStr = "", weightStr = "", otherStr= "";

                if (TextUtils.isEmpty(heightEdt.getText().toString())) {
                    heightEdt.setError("Height field can't be empty");
                } else if (TextUtils.isEmpty(weightEdt.getText().toString())){
                    weightEdt.setError("Weight field can't be empty");
                } else if(allergyChk.isChecked() && (TextUtils.isEmpty(allergyEdt.getText().toString()))){
                    allergyEdt.setError("Allergy field can't be empty");
                } else if(cancerChk.isChecked() && (TextUtils.isEmpty(cancerEdt.getText().toString()))){
                    cancerEdt.setError("Allergy field can't be empty");
                } else {

                    heightStr = heightEdt.getText().toString() + " cm";
                    weightStr = weightEdt.getText().toString() + " kg";
                    otherStr = othersEdt.getText().toString();

                    if(allergyChk.isChecked()){
                        String allergyStr = allergyChk.getText().toString() + " - " + allergyEdt.getText().toString();
                        medicalStr += allergyStr + "\n";
                    }

                    if (amoebiasisChk.isChecked()) {
                        medicalStr += amoebiasisChk.getText().toString() + "\n";
                    }

                    if (anemiaChk.isChecked()) {
                        medicalStr += anemiaChk.getText().toString() + "\n";
                    }

                    if (asthmaChk.isChecked()) {
                        medicalStr += asthmaChk.getText().toString() + "\n";
                    }

                    if (cancerChk.isChecked()) {
                        String cancerStr = cancerChk.getText().toString() + " - " + cancerEdt.getText().toString();
                        medicalStr += cancerStr + "\n";
                    }

                    if (chronicChk.isChecked()) {
                        medicalStr += chronicChk.getText().toString() + "\n";
                    }

                    if(diabetesChk.isChecked()) {
                        medicalStr += diabetesChk.getText().toString() + "\n";
                    }

                    if(dysmenorrheaChk.isChecked()) {
                        medicalStr += dysmenorrheaChk.getText().toString() + "\n";
                    }

                    if(faintingChk.isChecked()) {
                        medicalStr += faintingChk.getText().toString() + "\n";
                    }

                    if(earChk.isChecked()) {
                        medicalStr += earChk.getText().toString() + "\n";
                    }

                    if(endocrineChk.isChecked()) {
                        medicalStr += endocrineChk.getText().toString() + "\n";
                    }

                    if(frequentChk.isChecked()) {
                        medicalStr += frequentChk.getText().toString() + "\n";
                    }

                    if(headChk.isChecked()) {
                        medicalStr += headChk.getText().toString() + "\n";
                    }

                    if(heartChk.isChecked()) {
                        medicalStr += heartChk.getText().toString() + "\n";
                    }

                    if(herniaChk.isChecked()) {
                        medicalStr += herniaChk.getText().toString() + "\n";
                    }

                    if(highChk.isChecked()) {
                        medicalStr += highChk.getText().toString() + "\n";
                    }

                    if(insomiaChk.isChecked()) {
                        medicalStr += insomiaChk.getText().toString() + "\n";
                    }

                    if (kidneyChk.isChecked()) {
                        medicalStr += kidneyChk.getText().toString() + "\n";
                    }

                    if(mentalChk.isChecked()) {
                        medicalStr += mentalChk.getText().toString() + "\n";
                    }

                    if(nervousChk.isChecked()) {
                        medicalStr += nervousChk.getText().toString() + "\n";
                    }

                    if(noseChk.isChecked()) {
                        medicalStr += noseChk.getText().toString() + "\n";
                    }

                    if (pepticChk.isChecked()) {
                        medicalStr += pepticChk.getText().toString() + "\n";
                    }

                    if(tuberculosisChk.isChecked()) {
                        medicalStr += tuberculosisChk.getText().toString();
                    }

                    //Toast.makeText(NewQR2.this, heightStr+"\n"+weightStr+"\n"+bloodTypeStr+"\n"+medicalStr+"\n"+otherStr, Toast.LENGTH_LONG).show();
                    String others = heightStr+"\n"+weightStr+"\n"+bloodTypeStr+"\n"+medicalStr+"\n"+otherStr;

                    db.deleteMyQr();

                    Cursor res = db.getUserEmail();
                    while (res.moveToNext()){
                        email = res.getString(0);
                    }

                    //Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();

                    boolean ins = db.updateUserOther(email,others);

                    newQr();

                    if (ins) {

                        //Toast.makeText(NewQR.this, "inserted", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(NewQR2.this, MyQR.class));
                        finish();
                    } else{
                        //Toast.makeText(NewQR.this, "not inserted", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NewQR2.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NewQR2.this, MyQR.class));
        finish();
        super.onBackPressed();
    }

    private static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private void newQr(){
        Database db = new Database(NewQR2.this);
        Cursor res = db.getUserDataForQR();
        while(res.moveToNext()){
            for (int i = 0; i < res.getColumnCount(); i++)
                text2qr += res.getString(i) + "\n";
        }

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text2qr, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            byte[] myQRtoByte = getBytes(bitmap);
            boolean insert = db.insertQR(myQRtoByte);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


}

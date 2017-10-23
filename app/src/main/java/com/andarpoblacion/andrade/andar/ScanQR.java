package com.andarpoblacion.andrade.andar;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    public void handleResult(Result result) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        mScannerView.resumeCameraPreview(this);*/

        Intent editCont = new Intent(ScanQR.this, ScannedQR.class);
        editCont.putExtra("data", result.getText());
        startActivity(editCont);
        mScannerView.stopCamera();
        finish();

    }

    @Override
    public void onBackPressed() {
        mScannerView.stopCamera();
        super.onBackPressed();
    }
}

package com.andarpoblacion.andrade.andar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private EditText emailEdt, passwordEdt;
    private TextView registerTxt;
    private Button signInBtn;
    private String text2qr="";
    private ProgressDialog progressDialog;
    private String userType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Database db = new Database(this);

        /*if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, ResidentActivity.class));
            return;
        }*/

        if(getIntent().getBooleanExtra("Exit", false)){
            finish();
            return;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Sign In");

        checkAndRequestPermissions();

        emailEdt = (EditText) findViewById(R.id.edtEmail);
        passwordEdt = (EditText) findViewById(R.id.edtPassword);
        registerTxt = (TextView) findViewById(R.id.txtRegister);
        signInBtn = (Button) findViewById(R.id.btnSignIn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailEdt.setError(null);
                passwordEdt.setError(null);

                final String emailEdtStr = emailEdt.getText().toString();
                final String passwordEdtStr = passwordEdt.getText().toString();

                if(TextUtils.isEmpty(emailEdtStr)){
                    emailEdt.setError("This field can't be empty");
                } else if(emailEdtStr.equalsIgnoreCase("admin")){
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                }
                else if(emailEdtStr.equalsIgnoreCase("resident")){
                    startActivity(new Intent(LoginActivity.this, ResidentActivity.class));
                }
                else if(emailEdtStr.equalsIgnoreCase("rescue")){
                    startActivity(new Intent(LoginActivity.this, Rescue.class));
                }
                else if(!emailEdtStr.contains("@")){
                    emailEdt.setError("Invalid Email Address");
                }
                else if(TextUtils.isEmpty(passwordEdtStr))
                    passwordEdt.setError("This field can't be empty");
                else if(!isConnectedToInternet()){
                    noInternet();
                }

                else {

                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            Constants.URL_LOGIN,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    try {

                                        Database db = new Database(LoginActivity.this);
                                        JSONObject obj = new JSONObject(response);

                                        if(obj.getBoolean("error") == false){

                                            boolean ins = db.insertUser(
                                                    ""+obj.getInt("id"),
                                                    obj.getString("name"),
                                                    obj.getString("phone"),
                                                    obj.getString("bday"),
                                                    obj.getString("address"),
                                                    obj.getString("email"),
                                                    obj.getString("password"),
                                                    obj.getString("status"),
                                                    obj.getString("userType")
                                            );


                                            Cursor res12 = db.getUserType();
                                            while(res12.moveToNext()) {
                                                userType = res12.getString(0);
                                            }

                                            if(userType.equalsIgnoreCase("ADMIN")){
                                                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                                                finish();
                                            } else if (userType.equalsIgnoreCase("RESIDENT")){
                                                createQR();
                                                startActivity(new Intent(getApplicationContext(), ResidentActivity.class));
                                                finish();
                                            }
                                            else if (userType.equalsIgnoreCase("RESCUER")) {
                                                startActivity(new Intent(getApplicationContext(),Rescue.class));
                                                finish();
                                            }

                                        }else{
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    obj.getString("message"),
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();

                                    Toast.makeText(
                                            getApplicationContext(),
                                            error.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();

                                    if(error.getMessage().equalsIgnoreCase("")){
                                        noConnection();
                                    }
                                }
                            }
                    ){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", emailEdtStr);
                            params.put("password", passwordEdtStr);
                            return params;
                        }

                    };

                    RequestHandler.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });

    }


    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private void createQR(){
        Database db = new Database(LoginActivity.this);
        db.deleteMyQr();
        Cursor res = db.getPreUserData();
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
            boolean ins = db.insertQR(myQRtoByte);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void noConnection(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Can't connect to server right now\nPlease try again later")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void noInternet(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("No internet connection..."+"\n"+"You need internet connection to login your account")
                .setCancelable(true);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Error");
        alertDialog.show();
    }

    private boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(null!=ni){
            return true;
        } else {
            return false;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private  boolean checkAndRequestPermissions() {
        int readcont = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int sendsms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int writesettings = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS);
        int readphonestate = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int readsms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int netState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int receiveSms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int vibrate = ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (readcont != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (sendsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (writesettings != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_SETTINGS);
        }
        if (readsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (readphonestate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (netState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (receiveSms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (vibrate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.VIBRATE);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}

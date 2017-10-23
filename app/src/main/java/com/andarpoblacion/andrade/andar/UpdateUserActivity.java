package com.andarpoblacion.andrade.andar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateUserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText compNameEdt, phoneEdt, houseNoEdt, emailEdt, pwEdt;
    private Button updateBtn, bdayBtn, delBtn;
    private TextView bdayTxt;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    int year, month, day;
    Calendar calendar;
    private ProgressDialog progressDialog;
    private String id, comp_name, phone, houseNo, street, email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("User Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        Intent intent = getIntent();
        String data = intent.getStringExtra("name");


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        compNameEdt = (EditText) findViewById(R.id.edtCompName);
        phoneEdt = (EditText) findViewById(R.id.edtPhoneNo);
        houseNoEdt = (EditText) findViewById(R.id.edtHouseNo);
        emailEdt = (EditText) findViewById(R.id.edtEmail);
        pwEdt = (EditText) findViewById(R.id.edtPassword);
        updateBtn = (Button) findViewById(R.id.btnUpdate);
        delBtn = (Button) findViewById(R.id.btnDelete);
        bdayTxt = (TextView) findViewById(R.id.txtBday);
        bdayBtn = (Button) findViewById(R.id.btnBday);

        bdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatePickerDialog dialog = new DatePickerDialog(
                        UpdateUserActivity.this,
                        mDateSetListener,
                        year, month, day);
                dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                dialog.setCancelable(false);
                dialog.show();


            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int years, int months, int days) {
                year = years;
                month = months;
                day = days;
                bdayTxt.setText("" + (months + 1) + "/" + days + "/" + years);
            }
        };

        getStringReq(data);

    }

    public void getStringReq(final String userType){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            if (TextUtils.isEmpty(response)){
                                noConnection();
                            } else {
                                JSONObject obj = new JSONObject(response);
                                id = ""+obj.getInt("id");
                                compNameEdt.setText(obj.getString("name"));
                                phoneEdt.setText(obj.getString("phone"));
                                houseNoEdt.setText(obj.getString("address"));
                                bdayTxt.setText(obj.getString("bday"));
                                emailEdt.setText(obj.getString("email"));
                                pwEdt.setText(obj.getString("password"));
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


                        if (TextUtils.isEmpty(error.getMessage())){
                            noConnection();
                        }

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", userType);
                return params;
            }

        };

        RequestHandler.getInstance(UpdateUserActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            UpdateUserActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void noInternet(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("No internet connection...")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

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

    private void noConnection(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserActivity.this);
        builder.setTitle("Error");
        builder.setMessage("Can't connect to database right now\nPlease try again later\nContact your developer")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

package com.andarpoblacion.andrade.andar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText gNameEdt, mNameEdt, fNameEdt, phoneEdt, houseNoEdt, stEdt, townEdt, emailEdt, email2Edt, pwEdt, pw2Edt;
    private Button regBtn, bdayBtn;
    private TextView bdayTxt;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    int year, month, day;
    Calendar calendar;
    private ProgressDialog progressDialog;
    private Spinner locSpn;
    private ArrayAdapter locAdp;
    private String locData="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Register");


        progressDialog = new ProgressDialog(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        gNameEdt = (EditText) findViewById(R.id.edtGivenName);
        locSpn = (Spinner) findViewById(R.id.spnLoc);
        mNameEdt = (EditText) findViewById(R.id.edtMiddleName);
        fNameEdt = (EditText) findViewById(R.id.edtFamilyName);
        phoneEdt = (EditText) findViewById(R.id.edtPhoneNo);
        houseNoEdt = (EditText) findViewById(R.id.edtHouseNo);
        stEdt = (EditText) findViewById(R.id.edtStreet);
        townEdt = (EditText) findViewById(R.id.edtTown);
        emailEdt = (EditText) findViewById(R.id.edtEmail);
        email2Edt = (EditText) findViewById(R.id.edtEmail2);
        pwEdt = (EditText) findViewById(R.id.edtPassword);
        pw2Edt = (EditText) findViewById(R.id.edtPassword2);
        regBtn = (Button) findViewById(R.id.btnRegister);
        bdayTxt = (TextView) findViewById(R.id.txtBday);
        bdayBtn = (Button) findViewById(R.id.btnBday);


        Resources res = getResources();
        final String[] loc = res.getStringArray(R.array.loc);
        HintAdapter hintAdapter=new HintAdapter(this,R.layout.support_simple_spinner_dropdown_item,loc);
        //locAdp = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, loc);
        locSpn.setAdapter(hintAdapter);
        locSpn.setSelection(hintAdapter.getCount());

        locSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locData = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        mDateSetListener,
                        year,month,day);
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
                bdayTxt.setText(""+(months+1)+"/"+days+"/"+years);
            }
        };

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*gNameEdt.setError(null);
                fNameEdt.setError(null);
                phoneEdt.setError(null);
                houseNoEdt.setError(null);
                stEdt.setError(null);
                emailEdt.setError(null);
                email2Edt.setError(null);
                pwEdt.setError(null);
                pw2Edt.setError(null);*/

                if(!isConnectedToInternet()){
                    noInternet();
                } else if(TextUtils.isEmpty(gNameEdt.getText().toString())){
                    gNameEdt.setError("Given name can't be empty");
                    gNameEdt.requestFocus();
                } else if(TextUtils.isEmpty(fNameEdt.getText().toString())){
                    fNameEdt.setError("Family name can't be empty");
                    fNameEdt.requestFocus();
                } else if (TextUtils.isEmpty(phoneEdt.getText().toString())){
                    phoneEdt.setError("Phone number can't be empty");
                    phoneEdt.requestFocus();
                } else if (TextUtils.isEmpty(houseNoEdt.getText().toString())){
                    houseNoEdt.setError("This field can't be empty");
                    houseNoEdt.requestFocus();
                } else if (TextUtils.isEmpty(emailEdt.getText().toString())){
                    emailEdt.setError("Email can't be empty");
                    emailEdt.requestFocus();
                } else if (TextUtils.isEmpty(email2Edt.getText().toString())){
                    email2Edt.setError("This field can't be empty");
                    email2Edt.requestFocus();
                } else if (TextUtils.isEmpty(pwEdt.getText().toString())){
                    pwEdt.setError("Password can't be empty");
                    pwEdt.requestFocus();
                } else if (pwEdt.getText().toString().length()<6){
                    pwEdt.setError("Password must at least 6 characters");
                    pwEdt.requestFocus();
                } else if (TextUtils.isEmpty(pw2Edt.getText().toString())){
                    pw2Edt.setError("This field can't be empty");
                    pw2Edt.requestFocus();
                } else if(!pw2Edt.getText().toString().equalsIgnoreCase(pwEdt.getText().toString())){
                    pw2Edt.setError("Password Mismatch");
                    pw2Edt.requestFocus();
                } else if(!email2Edt.getText().toString().equalsIgnoreCase(emailEdt.getText().toString())){
                    email2Edt.setError("Email Mismatch");
                    email2Edt.requestFocus();
                }
                else {

                    progressDialog.setMessage("Registering user...");
                    progressDialog.show();

                    final String name = gNameEdt.getText().toString()+" "+mNameEdt.getText().toString()+" "+fNameEdt.getText().toString(),
                            phone = phoneEdt.getText().toString(),
                            bday = bdayTxt.getText().toString(),
                            address = houseNoEdt.getText().toString()+" "+locData+" "+townEdt.getText().toString(),
                            email = emailEdt.getText().toString(),
                            password = pwEdt.getText().toString(),
                            status = "PENDING", userType = "RESIDENT";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Constants.URL_REGISTER,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        if(jsonObject.getString("message").equalsIgnoreCase("User registered successfully"))
                                            confirmUserMs();
                                        //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.hide();
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", name);
                            params.put("phone", phone);
                            params.put("bday", bday);
                            params.put("address", address);
                            params.put("email", email);
                            params.put("password", password);
                            params.put("status", status);
                            params.put("userType", userType);
                            return params;
                        }
                    };

                    RequestHandler.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);

                }

            }
        });

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

    private void confirmUserMs(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Thank you for registering an account. Your registration is subject for approval. We will be sending you a text message after we review your account. Please wait up to 24 hours. GOD BLESS!!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

package com.andarpoblacion.andrade.andar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private EditText rescuerNameEdt, resNameEdt, caseEdt;
    private Button sendReportBtn;
    private RadioButton doneRdo, negaRdo, needRdo;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private String stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Create Report");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Database db = new Database(this);

        Intent intent = getIntent();
        final String resName = intent.getStringExtra("name");

        rescuerNameEdt = (EditText) findViewById(R.id.edtRescName);
        resNameEdt = (EditText) findViewById(R.id.edtResName);
        caseEdt = (EditText) findViewById(R.id.edtCase);
        sendReportBtn = (Button) findViewById(R.id.btnSendReport);
        doneRdo = (RadioButton) findViewById(R.id.rdoDone);
        negaRdo = (RadioButton) findViewById(R.id.rdoNegative);
        needRdo = (RadioButton) findViewById(R.id.rdoNeedsFurther);

        resNameEdt.setText(resName);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        caseEdt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(caseEdt, InputMethodManager.SHOW_IMPLICIT);

        Cursor res = db.getUserName();
        while(res.moveToNext()){
            rescuerNameEdt.setText(res.getString(0));
        }


        sendReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(caseEdt.getText().toString())){
                    caseEdt.setError("This field can't be empty");
                } else if (doneRdo.isChecked() || negaRdo.isChecked() || needRdo.isChecked()){
                    if(doneRdo.isChecked())
                        stats="DONE";
                    else if(negaRdo.isChecked())
                        stats="NEGATIVE (FAKE)";
                    else if(needRdo.isChecked())
                        stats="NEEDS FURTHER ASSISTANCE";
                    stringReq(rescuerNameEdt.getText().toString(),resName,caseEdt.getText().toString(),stats);
                } else {
                    Toast.makeText(ReportActivity.this,"Remarks Missing...",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ReportActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void stringReq(final String rescuer, final String resident, final String message, final String status){
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_INSERT_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            if(jsonObject.getString("message").equalsIgnoreCase("Report created successfully")){
                                finish();
                            }

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
                params.put("rescuer", rescuer);
                params.put("resident", resident);
                params.put("message", message);
                params.put("status", status);
                return params;
            }
        };


        RequestHandler.getInstance(ReportActivity.this).addToRequestQueue(stringRequest);

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


}

package com.andarpoblacion.andrade.andar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RescueReportsActivity extends AppCompatActivity {

    private ListView reportsLv;
    private Spinner reportTypeSpn;
    private String[] reports;
    private ArrayAdapter<String> arrayAdapter;
    private Toolbar toolbar;
    private String reportType = "";
    private String rescuerName="";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_reports);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Reports");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Database db = new Database(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        Cursor res1 = db.getUserName();
        while(res1.moveToNext()){
            rescuerName = res1.getString(0);
        }

        reportsLv = (ListView) findViewById(R.id.lvReports);
        reportTypeSpn = (Spinner) findViewById(R.id.spnReportType);

        Resources res = getResources();
        final String[] bloodTypes = res.getStringArray(R.array.reportType);

        HintAdapter hintAdapter=new HintAdapter(this,R.layout.support_simple_spinner_dropdown_item,bloodTypes);
        reportTypeSpn.setAdapter(hintAdapter);
        // show hint
        reportTypeSpn.setSelection(hintAdapter.getCount());
        reportTypeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reportType = adapterView.getItemAtPosition(i).toString();
                if (reportType.equalsIgnoreCase("ALL")){
                    getStringReq(rescuerName);
                } else {
                    getStringReqSpec(rescuerName,reportType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            RescueReportsActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getStringReq(final String rescuerName){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("reports");
                            if (TextUtils.isEmpty(response)){
                                noConnection();
                                //Toast.makeText(UsersActivity.this,"walang laman",Toast.LENGTH_LONG).show();
                            } else {

                                reports = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    reports[i] = jsonObject.getString("resident") + "\n" + jsonObject.getString("message") + "\n" + jsonObject.getString("status") + "\n" + jsonObject.getString("date");
                                }

                                arrayAdapter = new ArrayAdapter<String>(RescueReportsActivity.this, android.R.layout.simple_list_item_1, reports);
                                reportsLv.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RescueReportsActivity.this,e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("rescuer", rescuerName);
                return params;
            }

        };

        RequestHandler.getInstance(RescueReportsActivity.this).addToRequestQueue(stringRequest);

    }

    public void getStringReqSpec(final String rescuerName, final String stats){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_SPECREPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("reports");
                            if (TextUtils.isEmpty(response)){
                                noConnection();
                                //Toast.makeText(UsersActivity.this,"walang laman",Toast.LENGTH_LONG).show();
                            } else {

                                reports = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    reports[i] = jsonObject.getString("resident") + "\n" + jsonObject.getString("message") + "\n" + jsonObject.getString("status") + "\n" + jsonObject.getString("date");
                                }

                                arrayAdapter = new ArrayAdapter<String>(RescueReportsActivity.this, android.R.layout.simple_list_item_1, reports);
                                reportsLv.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RescueReportsActivity.this,e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("rescuer", rescuerName);
                params.put("status", stats);
                return params;
            }

        };

        RequestHandler.getInstance(RescueReportsActivity.this).addToRequestQueue(stringRequest);

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

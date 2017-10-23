package com.andarpoblacion.andrade.andar;

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
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {

    private ListView reqLv;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private String[] usersPending;
    private ArrayAdapter<String> arrayAdapter;
    private TextView emptyTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Account Request/s");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reqLv = (ListView) findViewById(R.id.lvReq);
        emptyTxt = (TextView) findViewById(R.id.txtEmpty);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        getStringReq("PENDING");
        reqLv.setEmptyView(emptyTxt);



    }

    public void getStringReq(final String status){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_PENDING_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("pending");

                            if (TextUtils.isEmpty(response)){
                                noConnection();

                            } else {

                                usersPending = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    usersPending[i] = jsonObject.getString("name");
                                }

                                arrayAdapter = new ArrayAdapter<String>(RequestActivity.this, android.R.layout.simple_list_item_1, usersPending);
                                arrayAdapter.notifyDataSetChanged();
                                reqLv.setAdapter(arrayAdapter);
                                reqLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        //Toast.makeText(RequestActivity.this, usersPending[i], Toast.LENGTH_SHORT).show();

                                        Intent editUser = new Intent(RequestActivity.this, ActivateUserActivity.class);
                                        editUser.putExtra("name", usersPending[i]);
                                        startActivity(editUser);
                                        finish();
                                    }
                                });

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RequestActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
                params.put("status", status);
                return params;
            }

        };

        RequestHandler.getInstance(RequestActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            RequestActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void noConnection(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestActivity.this);
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
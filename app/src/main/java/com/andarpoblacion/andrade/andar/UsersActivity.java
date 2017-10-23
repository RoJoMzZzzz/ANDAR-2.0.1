package com.andarpoblacion.andrade.andar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioButton residentRdo, rescuerRdo;
    private ListView usersLv;
    private String[] names;
    private ArrayAdapter<String> arrayAdapter;
    private String userType = "RESCUER";
    private ProgressDialog progressDialog;
    private ProgressBar pbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Users");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        residentRdo = (RadioButton) findViewById(R.id.rdoResident);
        rescuerRdo = (RadioButton) findViewById(R.id.rdoRescuer);
        usersLv = (ListView) findViewById(R.id.lvUsers);
        pbUser = (ProgressBar) findViewById(R.id.progressBar3);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UsersActivity.this, AddUserActivity.class));
                finish();
            }
        });

        if(!isConnectedToInternet()){
            noInternet();
        } else {

            rescuerRdo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        userType = "RESCUER";
                        getStringReq(userType);
                    } else
                        userType = "RESIDENT";
                }
            });

            residentRdo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        userType = "RESIDENT";
                        getStringReq(userType);
                    } else
                        userType = "RESCUER";
                }
            });

        }

        usersLv.setEmptyView(pbUser);

    }

    public void getStringReq(final String userType){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("resident");
                            if (TextUtils.isEmpty(response)){
                                noConnection();
                                //Toast.makeText(UsersActivity.this,"walang laman",Toast.LENGTH_LONG).show();
                            } else {
                                names = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    names[i] = jsonObject.getString("name");
                                }

                                arrayAdapter = new ArrayAdapter<String>(UsersActivity.this, android.R.layout.simple_list_item_1, names);
                                usersLv.setAdapter(arrayAdapter);
                                usersLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Toast.makeText(UsersActivity.this, names[i], Toast.LENGTH_SHORT).show();

                                        Intent editUser = new Intent(UsersActivity.this, UpdateUserActivity.class);
                                        editUser.putExtra("name", names[i]);
                                        startActivity(editUser);
                                        finish();
                                    }
                                });

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UsersActivity.this,e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("userType", userType);
                return params;
            }

        };

        RequestHandler.getInstance(UsersActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            UsersActivity.this.finish();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
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

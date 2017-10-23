package com.andarpoblacion.andrade.andar;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ReportAdminActivity extends AppCompatActivity {

    private ListView reportsLv;
    private Spinner reportTypeSpn;
    private ArrayAdapter arrayAdapter;
    private String[] reports, tagasagip, residente, remarka, statusa, araw;
    private Toolbar toolbar;
    private String reportType = "";
    private ProgressDialog progressDialog;
    private String month;
    private String[] months;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_admin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Reports");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        textView = (TextView) findViewById(R.id.txtEmpty);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Send Alert Message to Admin and Emergency Contacts", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                showOptionMonth();

            }
        });

        reportsLv = (ListView) findViewById(R.id.lvReports);
        reportTypeSpn = (Spinner) findViewById(R.id.spnReportType);

        reportsLv.setEmptyView(textView);

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
                    getStringAll("ALL");
                } else {
                    getStringReq(reportType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void getStringReq(final String stat){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_REPORTSTAT,
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
                                    reports[i] = "RESCUER: "+jsonObject.getString("rescuer") + "\n"
                                            +"RESIDENT: "+ jsonObject.getString("resident") + "\n"
                                            + jsonObject.getString("message") + "\n"
                                            + jsonObject.getString("status") + "\n"
                                            + jsonObject.getString("date");
                                }

                                arrayAdapter = new ArrayAdapter<String>(ReportAdminActivity.this, android.R.layout.simple_list_item_1, reports);
                                reportsLv.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReportAdminActivity.this,e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("status", stat);
                return params;
            }

        };

        RequestHandler.getInstance(ReportAdminActivity.this).addToRequestQueue(stringRequest);

    }

    public void getStringAll(final String stat){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_REPORTad,
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
                                    reports[i] = "RESCUER: "+jsonObject.getString("rescuer") + "\n"
                                            +"RESIDENT: "+ jsonObject.getString("resident") + "\n"
                                            + jsonObject.getString("message") + "\n"
                                            + jsonObject.getString("status") + "\n"
                                            + jsonObject.getString("date");
                                }

                                arrayAdapter = new ArrayAdapter<String>(ReportAdminActivity.this, android.R.layout.simple_list_item_1, reports);
                                reportsLv.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReportAdminActivity.this,e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("status", stat);
                return params;
            }

        };

        RequestHandler.getInstance(ReportAdminActivity.this).addToRequestQueue(stringRequest);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ReportAdminActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showOptionMonth(){

        months = new String[]{"October 2017"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, months);
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.select_rescuer, null);

        final Spinner spn = (Spinner) promptsView.findViewById(R.id.spinner);
        final TextView textView = (TextView) promptsView.findViewById(R.id.textView25);

        textView.setText("Please Select Month");

        spn.setAdapter(adp);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setView(promptsView)
                .setCancelable(false)
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Context ctx = ReportAdminActivity.this;
                        //deleteSMS(ctx,message,number);
                        //getRescuerNumber(rescName,message);
                        getReports("ALL");
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Print Report");
        alertDialog.show();
    }

    public void getReports(final String stat){

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_REPORTad,
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
                                tagasagip = new String[jsonArray.length()];
                                residente = new String[jsonArray.length()];
                                remarka = new String[jsonArray.length()];
                                statusa = new String[jsonArray.length()];
                                araw = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    reports[i] = "RESCUER: "+jsonObject.getString("rescuer") + "\n"
                                            +"RESIDENT: "+ jsonObject.getString("resident") + "\n"
                                            + jsonObject.getString("message") + "\n"
                                            + jsonObject.getString("status") + "\n"
                                            + jsonObject.getString("date");
                                    tagasagip[i] = jsonObject.getString("rescuer");
                                    residente[i] = jsonObject.getString("resident");
                                    remarka[i] = jsonObject.getString("message");
                                    statusa[i] = jsonObject.getString("status");
                                    araw[i] = jsonObject.getString("date");
                                }

                                Document doc=new Document();

                                String outpath= Environment.getExternalStorageDirectory()+"/andar_reports.pdf";
                                try {


                                    PdfWriter.getInstance(doc, new FileOutputStream(outpath));

                                    doc.open();

                                    Paragraph title = new Paragraph();
                                    title.setAlignment(Element.ALIGN_MIDDLE);
                                    title.add(new Paragraph("ANDAR REPORTS", new Font(Font.FontFamily.TIMES_ROMAN, 18,
                                            Font.BOLD)));
                                    doc.add(title);

                                    title = new Paragraph();
                                    title.setAlignment(Element.ALIGN_CENTER);
                                    title.add(new Paragraph("October 2017"));
                                    doc.add(title);

                                    title = new Paragraph();
                                    for (int i = 0; i < 3; i++) {
                                        title.add(new Paragraph(" "));
                                    }
                                    doc.add(title);

                                    PdfPTable table = new PdfPTable(6);

                                    PdfPCell c1 = new PdfPCell(new Phrase("#", new Font(Font.FontFamily.TIMES_ROMAN, 14,
                                            Font.BOLD)));
                                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.addCell(c1);

                                    c1 = new PdfPCell(new Phrase("Rescuer's Name", new Font(Font.FontFamily.TIMES_ROMAN, 14,
                                            Font.BOLD)));
                                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.addCell(c1);

                                    c1 = new PdfPCell(new Phrase("Resident's Name", new Font(Font.FontFamily.TIMES_ROMAN, 14,
                                            Font.BOLD)));
                                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.addCell(c1);

                                    c1 = new PdfPCell(new Phrase("Remarks", new Font(Font.FontFamily.TIMES_ROMAN, 14,
                                            Font.BOLD)));
                                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.addCell(c1);

                                    c1 = new PdfPCell(new Phrase("Status", new Font(Font.FontFamily.TIMES_ROMAN, 14,
                                            Font.BOLD)));
                                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.addCell(c1);

                                    c1 = new PdfPCell(new Phrase("Date", new Font(Font.FontFamily.TIMES_ROMAN, 14,
                                            Font.BOLD)));
                                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.addCell(c1);

                                    table.setHeaderRows(1);

                                    for(int i = 0; i < reports.length;i++){
                                        table.addCell("  "+(i+1));
                                        table.addCell(" "+tagasagip[i]);
                                        table.addCell(" "+residente[i]);
                                        table.addCell(" "+remarka[i]);
                                        table.addCell(" "+statusa[i]);
                                        table.addCell(" "+araw[i]);
                                    }


                                    c1 = new PdfPCell(new Phrase(" ~~ Nothing Follows ~~ ", new Font(Font.FontFamily.TIMES_ROMAN, 14,
                                            Font.ITALIC)));
                                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    c1.setColspan(6);
                                    table.addCell(c1);

                                    doc.add(table);

                                    doc.close();

                                    File file = new File(Environment.getExternalStorageDirectory(),
                                            "andar_reports.pdf");
                                    Uri path = Uri.fromFile(file);
                                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    pdfOpenintent.setDataAndType(path, "application/pdf");
                                    try {
                                        startActivity(pdfOpenintent);
                                    }
                                    catch (ActivityNotFoundException e) {

                                    }

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                }


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReportAdminActivity.this,e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("status", stat);
                return params;
            }

        };

        RequestHandler.getInstance(ReportAdminActivity.this).addToRequestQueue(stringRequest);

    }

}
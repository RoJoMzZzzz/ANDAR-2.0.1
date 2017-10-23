package com.andarpoblacion.andrade.andar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class EmergencyHotlines3 extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioButton localRdo, fireRdo, ncrRdo;
    private Button call1Btn, call2Btn, call3Btn, call4Btn;
    private Spinner deptSpn;
    private ArrayAdapter deptAdp;
    private String telNum="", department="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_hotlines3);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Emergency Hotlines");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        localRdo = (RadioButton) findViewById(R.id.rdoLocal);
        fireRdo = (RadioButton) findViewById(R.id.rdoFire);
        ncrRdo = (RadioButton) findViewById(R.id.rdoNcr);
        call1Btn = (Button) findViewById(R.id.btnCall1);
        call2Btn = (Button) findViewById(R.id.btnCall2);
        call3Btn = (Button) findViewById(R.id.btnCall3);
        call4Btn = (Button) findViewById(R.id.btnCall4);
        deptSpn = (Spinner) findViewById(R.id.spnDept);
        
        //call1Btn.setVisibility(View.GONE);
        call2Btn.setVisibility(View.GONE);
        call3Btn.setVisibility(View.GONE);
        call4Btn.setVisibility(View.GONE);

        Resources res = getResources();
        final String[] nearby = res.getStringArray(R.array.nearby);
        final String[] local = res.getStringArray(R.array.local);
        final String[] ncr = res.getStringArray(R.array.ncr);
        
        EmergencyHotlines3.PhoneCallListener phoneListener = new EmergencyHotlines3.PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        


        if(localRdo.isChecked()){
            deptAdp = new ArrayAdapter(EmergencyHotlines3.this, R.layout.support_simple_spinner_dropdown_item, local);
            deptSpn.setAdapter(deptAdp);
            call2Btn.setVisibility(View.GONE);
            call3Btn.setVisibility(View.GONE);
            call4Btn.setVisibility(View.GONE);
            deptSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Police")){
                        call1Btn.setText("(02) 6428235");
                        department="Local Police";
                    } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Fire")){
                        call1Btn.setText("(02) 6411365");
                        department="Local Fire";
                    } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Rescue")){
                        call1Btn.setText("(02) 6425159");
                        department="Local Rescue";
                    } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Barangay Poblacion")){
                        call1Btn.setText("(02) 6415502");
                        department="Barangay Poblacion";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

       localRdo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //Toast.makeText(EmergencyHotlines3.this,"local",Toast.LENGTH_SHORT).show();
                    deptAdp = new ArrayAdapter(EmergencyHotlines3.this, R.layout.support_simple_spinner_dropdown_item, local);
                    deptSpn.setAdapter(deptAdp);
                    deptAdp.notifyDataSetChanged();
                    call2Btn.setVisibility(View.GONE);
                    call3Btn.setVisibility(View.GONE);
                    call4Btn.setVisibility(View.GONE);
                    deptSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Police")){
                                call1Btn.setText("(02) 6428235");
                                department="Local Police";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Fire")){
                                call1Btn.setText("(02) 6411365");
                                department="Local Fire";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Rescue")){
                                call1Btn.setText("(02) 6425159");
                                department="Local Rescue";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Barangay Poblacion")){
                                call1Btn.setText("(02) 6415502");
                                department="Barangay Poblacion";
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    

                }
            }
        });

        fireRdo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //Toast.makeText(EmergencyHotlines3.this,"fire",Toast.LENGTH_SHORT).show();
                    deptAdp = new ArrayAdapter(EmergencyHotlines3.this, R.layout.support_simple_spinner_dropdown_item, nearby);
                    deptSpn.setAdapter(deptAdp);
                    call4Btn.setVisibility(View.GONE);
                    deptAdp.notifyDataSetChanged();
                    deptSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Bonifacio Fire Department")){
                                call1Btn.setText("(02) 4946667");
                                call2Btn.setVisibility(View.GONE);
                                call3Btn.setVisibility(View.GONE);
                                department="Bonifacio Fire Department";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Taguig Fire Department")){
                                call1Btn.setText("(02) 8370704");
                                call2Btn.setVisibility(View.VISIBLE);
                                call3Btn.setVisibility(View.VISIBLE);
                                call2Btn.setText("(02) 5423695");
                                call3Btn.setText("(02) 8374496");
                                department="Taguig Fire Department";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Makati Fire Department")){
                                call1Btn.setText("(02) 8185150");
                                call2Btn.setVisibility(View.GONE);
                                call3Btn.setVisibility(View.GONE);
                                department="Makati Fire Department";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Pasig Fire Department")){
                                call1Btn.setText("(02) 6411939");
                                call2Btn.setText("(02) 6412815");
                                call2Btn.setVisibility(View.VISIBLE);
                                call3Btn.setVisibility(View.GONE);
                                department="Pasig Fire Department";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Pasay Fire Department")){
                                call1Btn.setText("(02) 8442120");
                                call2Btn.setText("(02) 8436523");
                                call2Btn.setVisibility(View.VISIBLE);
                                call3Btn.setVisibility(View.GONE);
                                department="Pasay Fire Department";
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });

        ncrRdo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //Toast.makeText(EmergencyHotlines3.this,"ncr",Toast.LENGTH_SHORT).show();
                    deptAdp = new ArrayAdapter(EmergencyHotlines3.this, R.layout.support_simple_spinner_dropdown_item, ncr);
                    deptSpn.setAdapter(deptAdp);
                    deptAdp.notifyDataSetChanged();

                    deptSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("NDRRMC")){
                                call1Btn.setText("(02) 9111406");
                                call2Btn.setVisibility(View.VISIBLE);
                                call3Btn.setVisibility(View.VISIBLE);
                                call2Btn.setText("(02) 9122665");
                                call3Btn.setText("(02) 9125668");
                                call4Btn.setVisibility(View.GONE);
                                department="NDRRMC";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Bureau of Fire Protection")){
                                call1Btn.setText("(02) 7295166");
                                call2Btn.setVisibility(View.VISIBLE);
                                call3Btn.setVisibility(View.VISIBLE);
                                call2Btn.setText("(02) 4106254");
                                call3Btn.setText("(02) 4318859");
                                call4Btn.setVisibility(View.VISIBLE);
                                call4Btn.setText("(02) 4071230");
                                department="Bureau of Fire Protection";
                            }  else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("PNP Hotline Patrol")){
                                call1Btn.setText("117");
                                call2Btn.setVisibility(View.GONE);
                                call3Btn.setVisibility(View.GONE);
                                call4Btn.setVisibility(View.GONE);
                                department="PNP Hotline Patrol";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("DOTC")){
                                call1Btn.setText("(02) 7890");
                                call2Btn.setVisibility(View.VISIBLE);
                                call3Btn.setVisibility(View.GONE);
                                call2Btn.setText("09188848484");
                                call4Btn.setVisibility(View.GONE);
                                department="DOTC";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("MMDA Metrobase")){
                                call1Btn.setText("(02) 8185150");
                                call2Btn.setVisibility(View.GONE);
                                call3Btn.setVisibility(View.GONE);
                                call4Btn.setVisibility(View.GONE);
                                department="MMDA Metrobase";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("MMDA Flood Control")){
                                call1Btn.setText("(02) 8824177");
                                call2Btn.setVisibility(View.VISIBLE);
                                call3Btn.setVisibility(View.GONE);
                                call2Btn.setText("(02) 8820925");
                                call4Btn.setVisibility(View.GONE);
                                department="MMDA Flood Control";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("DPWH")){
                                call1Btn.setText("(02) 3043713");
                                call2Btn.setVisibility(View.GONE);
                                call3Btn.setVisibility(View.GONE);
                                call4Btn.setVisibility(View.GONE);
                                department="DPWH";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Red Cross")){
                                call1Btn.setText("143");
                                call2Btn.setVisibility(View.VISIBLE);
                                call3Btn.setVisibility(View.GONE);
                                call2Btn.setText("(02) 9111876");
                                call4Btn.setVisibility(View.GONE);
                                department="Red Cross";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("PAGASA")){
                                call1Btn.setText("(02) 4338526");
                                call2Btn.setVisibility(View.GONE);
                                call3Btn.setVisibility(View.GONE);
                                call4Btn.setVisibility(View.GONE);
                                department="PAGASA";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Philippine Coast Guard")){
                                call1Btn.setText("(02) 5273877");
                                call2Btn.setVisibility(View.VISIBLE);
                                call3Btn.setVisibility(View.VISIBLE);
                                call2Btn.setText("(02) 5278481");
                                call3Btn.setText("09177243682");
                                call4Btn.setVisibility(View.GONE);
                                department="Philippine Coast Guard";
                            } else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("DOH")){
                                call1Btn.setText("(02) 7111001");
                                call2Btn.setVisibility(View.GONE);
                                call3Btn.setVisibility(View.GONE);
                                call4Btn.setVisibility(View.GONE);
                                department="DOH";
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }
        });

        call1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telNum = call1Btn.getText().toString();
                department= deptSpn.getSelectedItem().toString();
                StartCall(telNum);
            }
        });

        call2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telNum = call2Btn.getText().toString();
                department= deptSpn.getSelectedItem().toString();
                StartCall(telNum);
            }
        });

        call3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telNum = call3Btn.getText().toString();
                department= deptSpn.getSelectedItem().toString();
                StartCall(telNum);
            }
        });

        call4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telNum = call4Btn.getText().toString();
                department= deptSpn.getSelectedItem().toString();
                StartCall(telNum);
            }
        });






    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            EmergencyHotlines3.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        deptAdp.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        deptAdp.notifyDataSetChanged();
    }

    private void StartCall(String telNum){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telNum));
        if (ActivityCompat.checkSelfPermission(EmergencyHotlines3.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Calendar c = Calendar.getInstance();
        int minutes = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        int ampm = c.get(Calendar.AM_PM);
        int year = c.get(Calendar.YEAR);
        String chk;
        if(ampm == 1)
            chk = "PM";
        else
            chk = "AM";

        final String currDate = ""+month+"/"+day+"/"+year;
        final String currTime = hour+":"+minutes+" "+chk;

        /*boolean insCont = db.insertCall(telNum, dept, currDate, currTime);
        if(insCont){
            //Toast.makeText(this, "inserted", Toast.LENGTH_LONG).show();
        }

        else{
            //Toast.makeText(this, "not inserted", Toast.LENGTH_LONG).show();
        }*/


        startActivity(callIntent);
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    /*Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/

                    isPhoneCalling = false;
                }

            }
        }
    }

}

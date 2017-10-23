package com.andarpoblacion.andrade.andar;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyFragment extends Fragment {

    private Switch flashlight, strobeFlashlight, screenFlashlight, alarm;
    private Button messageContacts, emergencyContacts, callAuthority, scanQrClass, angQRnaIto;
    private MediaPlayer mp;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private Camera.Parameters params;
    private int i = 0, freq = 5;
    private StroboRunner sr;
    private Thread t;


    public EmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        flashlight = (Switch) view.findViewById(R.id.swtFlashlight);
        strobeFlashlight = (Switch) view.findViewById(R.id.swtStrobe);
        screenFlashlight = (Switch) view.findViewById(R.id.swtScreenFlash);
        alarm = (Switch) view.findViewById(R.id.swtAlarm);

        messageContacts = (Button) view.findViewById(R.id.btnMessage);
        emergencyContacts = (Button) view.findViewById(R.id.btnContacts);
        callAuthority = (Button) view.findViewById(R.id.btnCall);
        angQRnaIto = (Button) view.findViewById(R.id.btnQRCode);
        scanQrClass = (Button) view.findViewById(R.id.btnQrCodeScanners);

        checkSupport();
        //getCamera();

        scanQrClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),PreScanQR.class));
            }
        });

        angQRnaIto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyQR.class));
            }
        });


        /*messageContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MessageContact2.class));
            }
        });*/

        emergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EmergencyContacts.class));
            }
        });

        callAuthority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmergencyHotlines3.class));
            }
        });


        flashlight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {              if (b) {
                    screenFlashlight.setChecked(false);
                    strobeFlashlight.setChecked(false);
                    //Toast.makeText(getActivity(), "Flashlight", Toast.LENGTH_SHORT).show();
                        turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });

        strobeFlashlight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    flashlight.setChecked(false);
                    screenFlashlight.setChecked(false);
                    //Toast.makeText(getActivity(), "Strobe Flashlight", Toast.LENGTH_SHORT).show();

                        blinkFlash(b);

                } else {
                    blinkFlash(b);
                    turnOffFlash();
                    //Toast.makeText(getActivity(),""+b, Toast.LENGTH_SHORT).show();
                }

            }
        });

        screenFlashlight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (screenFlashlight.isChecked()) {
                    flashlight.setChecked(false);
                    strobeFlashlight.setChecked(false);
                    //Toast.makeText(getActivity(), "Screen Flashlight", Toast.LENGTH_SHORT).show();
                    MaxBright();
                } else
                    MinBright();
            }
        });

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //Toast.makeText(getActivity(), "Alarm", Toast.LENGTH_SHORT).show();
                    PlayAlarm();
                } else {
                    mp.stop();
                }

            }
        });

        return view;
    }


    private void MaxBright() {
        try {
            //sets manual mode and brightnes 255
            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  //this will set the manual mode (set the automatic mode off)
            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);  //this will set the brightness to maximum (255)

            //refreshes the screen
            int br = Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.screenBrightness = (float) br / 255;
            getActivity().getWindow().setAttributes(lp);

        } catch (Exception e) {
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getActivity())) {
                ContentResolver cResolver = this.getActivity().getContentResolver();
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 255);
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

    }

    private void MinBright() {
        try {

            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);  //this will set the manual mode (set the automatic mode off)

            //refreshes the screen
            int br = Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.screenBrightness = (float) br / 1;
            getActivity().getWindow().setAttributes(lp);

        } catch (Exception e) {
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getActivity())) {
                ContentResolver cResolver = this.getActivity().getContentResolver();
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 0);
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

    }


    private void PlayAlarm() {
        mp = MediaPlayer.create(getActivity(), R.raw.alarm);
        mp.setLooping(true);
        mp.start();
    }

    private void checkSupport() {

        Boolean isFlashAvailable = getActivity().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            strobeFlashlight.setEnabled(false);
            flashlight.setEnabled(false);

            AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
            alert.setTitle("Error!!");
            alert.setMessage("Your device does'nt support flashlight!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();
            return;

        }

    }

    @SuppressLint("LongLogTag")
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }


    private void turnOnFlash() {

        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }


         params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            SurfaceTexture dummy = new SurfaceTexture(1);
            try {
                camera.setPreviewTexture(dummy);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
            isFlashOn = true;


        }


    }

    private void turnOffFlash() {

        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

        }

    }

    private void blinkFlash(boolean b) {

        if (camera == null || params == null) {
            return;
        }

        if (b) {



                sr = new StroboRunner();
                t = new Thread(sr);
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                t.start();
                isFlashOn = true;
                return;

        } else if (!b) {
            if (t != null) {
                sr.stopRunning = true;
                t = null;
                return;
            } else {
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                isFlashOn = false;
            }
        }
        camera.setParameters(params);
        camera.startPreview();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();

        // on resume turn on the flash
        if(hasFlash)
            turnOnFlash();
    }

    @Override
    public void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    public void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();

        //mp.stop();
    }

    @Override
    public void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }

        //mp.stop();
    }

    private class StroboRunner implements Runnable {
        boolean stopRunning = false;

        @Override
        public void run() {
            Camera.Parameters paramsOn = camera.getParameters();
            Camera.Parameters paramsOff = params;
            paramsOn.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            paramsOff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            try {
                while (!stopRunning) {
                    camera.setParameters(paramsOn);
                    camera.startPreview();
                    Thread.sleep((100 - freq) * 1);
                    camera.setParameters(paramsOff);
                    camera.startPreview();
                    Thread.sleep(100 - freq);
                }
            } catch (Exception e) {
            }
        }
    }

}

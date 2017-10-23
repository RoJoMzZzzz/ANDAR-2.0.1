package com.andarpoblacion.andrade.andar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrepareFragment extends Fragment {

    private Button earthBtn, fireBtn, floodBtn, landBtn, tsunamiBtn, typBtn, volBtn;


    public PrepareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_prepare, container, false);

        earthBtn = (Button) v.findViewById(R.id.btnEarthquake);
        fireBtn = (Button) v.findViewById(R.id.btnFire);
        floodBtn = (Button) v.findViewById(R.id.btnFlood);
        landBtn = (Button) v.findViewById(R.id.btnLandslide);
        tsunamiBtn = (Button) v.findViewById(R.id.btnTsunami);
        typBtn = (Button) v.findViewById(R.id.btnTyphoon);
        volBtn = (Button) v.findViewById(R.id.btnVolcano);

        earthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Earth.class));
            }
        });

        fireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Fire.class));
            }
        });

        floodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Flood.class));
            }
        });

        landBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Landslide.class));
            }
        });

        tsunamiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Tsunami.class));
            }
        });

        typBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Typhoon.class));
            }
        });

        volBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Volcano.class));
            }
        });

        return v;
    }

}

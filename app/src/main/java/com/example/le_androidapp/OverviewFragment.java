package com.example.le_androidapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.le_androidapp.data.AccelResult;

public class OverviewFragment extends Fragment {

    //AccelResult accelResult;

    TextView xView;
    TextView yView;
    TextView zView;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                xView.setText(Integer.toString((int) accelResult.getXAccel()));
                yView.setText(Integer.toString((int) accelResult.getYAccel()));
                zView.setText(Integer.toString((int) accelResult.getZAccel()));
            }
        }, 0);

         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        /*
        xView = (TextView) view.findViewById(R.id.accelX);
        yView = (TextView) view.findViewById(R.id.accelY);
        zView = (TextView) view.findViewById(R.id.accelZ);

        xView.setText(Integer.toString((int) accelResult.getXAccel()));
        yView.setText(Integer.toString((int) accelResult.getYAccel()));
        zView.setText(Integer.toString((int) accelResult.getZAccel()));

         */

        return view;
    }
}
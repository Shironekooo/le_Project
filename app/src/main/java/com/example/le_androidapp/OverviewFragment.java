package com.example.le_androidapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.le_androidapp.data.DeviceResult;
import com.example.le_androidapp.data.ConnectionState;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


// TODO change to be able to show history or actionable data not raw data

@AndroidEntryPoint
public class OverviewFragment extends Fragment {

    @Inject DeviceViewModel deviceViewModel;

    TextView pitchView;
    TextView flexView;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        pitchView = (TextView) view.findViewById(R.id.pitchView);
        flexView = (TextView) view.findViewById(R.id.flexView);

        deviceViewModel.getConnectionState().observe(getViewLifecycleOwner(), new Observer<ConnectionState>() {
            @Override
            public void onChanged(ConnectionState connectionState) {
                // Log.e("OverviewFragment", "LifeCycleOnChanged");
                if (connectionState instanceof ConnectionState.Connected) {
                    pitchView.setText(Float.toString(((ConnectionState.Connected) connectionState).getPitch()));
                    flexView.setText(Float.toString(((ConnectionState.Connected) connectionState).getFlex()));
                }
            }
        });

        return view;
    }
}
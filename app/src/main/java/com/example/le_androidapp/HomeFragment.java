package com.example.le_androidapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.le_androidapp.data.DeviceResult;
import com.example.le_androidapp.data.ConnectionState;
import com.example.le_androidapp.data.DeviceReceiveManager;
import com.example.le_androidapp.data.ble.DeviceBLEReceiveManager;
import com.example.le_androidapp.util.Resource;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.flow.MutableSharedFlow;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    @Inject DeviceViewModel deviceViewModel;

    ImageButton notificationsButton;
    ImageButton settingsButton;

    Button badPostureButtonResting;
    Button badPostureButtonWorking;
    Button bleButton;

    TextView txv;

    ImageView bendy;

    private int badPostureCount = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txv.setText(Integer.toString(badPostureCount));
            }
        }, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        int modeSelect = sp.getInt("mode", -1);
        SharedPreferences.Editor editor = sp.edit();

        bendy = (ImageView) view.findViewById(R.id.bendy_guy);

        deviceViewModel.getConnectionState().observe(getViewLifecycleOwner(), new Observer<ConnectionState>() {
            @Override
            public void onChanged(ConnectionState connectionState) {
                Log.e("HomeFragment", "LifeCycleOnChanged");
                if (connectionState instanceof ConnectionState.Connected) {
                    Log.e("HomeFragment", "LifeCycleOnChanged-Connected");
                    float pitch = ((ConnectionState.Connected) connectionState).getYVal();

                    if (pitch >= 0) bendy.setImageResource(R.drawable.body90);
                    else if (pitch >= -2) bendy.setImageResource(R.drawable.body85);
                    else if (pitch >= -4) bendy.setImageResource(R.drawable.body80);
                    else if (pitch >= -6) bendy.setImageResource(R.drawable.body75);
                    else if (pitch >= -8) bendy.setImageResource(R.drawable.body70);
                    else if (pitch >= -9) bendy.setImageResource(R.drawable.body65);
                    else if (pitch >= -10) bendy.setImageResource(R.drawable.body60);
                    else if (pitch >= -12) bendy.setImageResource(R.drawable.body55);
                    else if (pitch >= -14) bendy.setImageResource(R.drawable.body50);
                    else if (pitch >= -16) bendy.setImageResource(R.drawable.body45);
                    else bendy.setImageResource(R.drawable.body40);
                }
                // put code to change bendy guy's image here
            }
        });

        settingsButton = (ImageButton) view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("currentScreen", "settings");
                editor.commit();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.container, new SettingsFragment());
                fr.commit();
            }
        });

        notificationsButton = (ImageButton) view.findViewById(R.id.notification_button);
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("currentScreen", "notifications");
                editor.commit();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.container, new NotificationsFragment());
                fr.commit();
            }
        });

        badPostureButtonWorking = (Button) view.findViewById(R.id.bad_posture_button);
        badPostureButtonResting = (Button) view.findViewById(R.id.bad_posture_button_2);
        txv = (TextView) view.findViewById(R.id.bad_posture_count);

        switch (modeSelect){
            case 1:
                badPostureButtonWorking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        badPostureCount++;
                        txv.setText(Integer.toString(badPostureCount));
                    }
                });
                break;
            case 2:
                badPostureButtonResting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        badPostureCount++;
                        txv.setText(Integer.toString(badPostureCount));
                    }
                });
                break;
            case -1:
            default:
                Toast.makeText(getActivity(), "Error in Setting Mode", Toast.LENGTH_SHORT).show();
                break;
        }

        bleButton = (Button) view.findViewById(R.id.ble_scan_button);
        bleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Uninitialized) {
                    deviceViewModel.initializeConnection();
                    Log.e("HomeFragment", "InitializeConnection");
                    Toast.makeText(getActivity(), "Initializing connection", Toast.LENGTH_SHORT).show();
                } else if (deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Connected) {
                    deviceViewModel.disconnect();
                    Log.e("HomeFragment", "Disconnected");
                } else if (deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Disconnected) {
                    Toast.makeText(getActivity(), "Please restart the device to reconnect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
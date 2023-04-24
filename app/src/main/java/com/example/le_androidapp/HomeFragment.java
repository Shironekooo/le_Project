package com.example.le_androidapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
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

    SettingsFragment settingsFragment = new SettingsFragment();

    // ImageButton notificationsButton;
    ImageButton settingsButton;

    /*
    Button badPostureButtonResting;
    Button badPostureButtonWorking;
     */
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
        badPostureCount = sp.getInt("badCount", 0);
        int modeSelect = sp.getInt("mode", -1);
        int phoneVibrate = sp.getInt("phoneVibrate", -1);
        SharedPreferences.Editor editor = sp.edit();

        bendy = (ImageView) view.findViewById(R.id.bendy_guy);
        txv = (TextView) view.findViewById(R.id.bad_posture_count);
        final boolean[] incrementationOccurred = {false};

        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        deviceViewModel.getConnectionState().observe(getViewLifecycleOwner(), new Observer<ConnectionState>() {
            @Override
            public void onChanged(ConnectionState connectionState) {
                if (connectionState instanceof ConnectionState.Connected) {
                    Log.e("HomeFragment", "Connected");

                    float bend = ((ConnectionState.Connected) connectionState).getYVal();
                    float flex = ((ConnectionState.Connected) connectionState).getZVal();

                    if (bend >= 0) bendy.setImageResource(R.drawable.body90);
                    else if (bend >= -2) bendy.setImageResource(R.drawable.body85);
                    else if (bend >= -4) bendy.setImageResource(R.drawable.body80);
                    else if (bend >= -6) bendy.setImageResource(R.drawable.body75);
                    else if (bend >= -8) bendy.setImageResource(R.drawable.body70);
                    else if (bend >= -9) bendy.setImageResource(R.drawable.body65);
                    else if (bend >= -10) bendy.setImageResource(R.drawable.body60);
                    else if (bend >= -12) bendy.setImageResource(R.drawable.body55);
                    else if (bend >= -14) bendy.setImageResource(R.drawable.body50);
                    else if (bend >= -16) bendy.setImageResource(R.drawable.body45);
                    else bendy.setImageResource(R.drawable.body40);

                    int minBend, maxBend = -14;

                    switch (modeSelect) {
                        case 1:
                            minBend = -4;
                            break;
                        case 2:
                            minBend = -8;
                            break;
                        case -1:
                        default:
                            minBend = -4;
                            Toast.makeText(getActivity(), "Error in Setting Mode", Toast.LENGTH_SHORT).show();
                            Log.e("HomeFragment", "Mode selection failed");
                            break;
                    }
                    if (flex <= -400) {
                        new CountDownTimer(5000, 1000) {
                            float currentBend = ((ConnectionState.Connected) connectionState).getYVal();
                            float currentFlex = ((ConnectionState.Connected) connectionState).getZVal();

                            @Override
                            public void onTick(long millisUntilFinished) {
                                if ((currentBend >= minBend) || (currentBend <= maxBend) || (currentFlex >= -400)) cancel();
                            }

                            @Override
                            public void onFinish() {
                                if ((((currentBend <= minBend) && (currentBend >= maxBend)) || currentFlex <= -400) && !incrementationOccurred[0]) {
                                    badPostureCount++;
                                    editor.putInt("badCount", badPostureCount).commit();
                                    incrementationOccurred[0] = true;
                                    if (phoneVibrate == 1) v.vibrate(250);
                                }
                            }
                        }.start();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                incrementationOccurred[0] = false;
                            }
                        }, 5000);
                    }

                    txv.setText(Integer.toString(badPostureCount));
                }
            }
        });

        settingsButton = (ImageButton) view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("currentScreen", "settings");
                editor.commit();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.container, settingsFragment);
                fr.commit();
            }
        });

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

        /*
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

         */

        return view;
    }
}
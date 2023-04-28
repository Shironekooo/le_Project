package com.example.le_androidapp;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
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

//import com.example.finaldb.dao.UserDao;
import com.example.le_androidapp.data.DeviceResult;
import com.example.le_androidapp.data.ConnectionState;
import com.example.le_androidapp.data.DeviceReceiveManager;
import com.example.le_androidapp.data.ble.DeviceBLEReceiveManager;
import com.example.le_androidapp.util.Resource;

//import com.example.finaldb.source.AppDatabase;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.flow.MutableSharedFlow;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    @Inject DeviceViewModel deviceViewModel;

    SettingsFragment settingsFragment = new SettingsFragment();

    ImageButton settingsButton;

    Button bleButton;

    TextView txv;

    ImageView bendy;

    private int badPostureCount = 0;

    private int calibrateIncrement = 0;
    private int calibratedBend = 0;
    private boolean calibrationDone = false;
    private int currentCalibrate = 0;
    final private int reqCalibrate = 100;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Displays badPostureCount upon HomeFragment switch
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

        // SharedPreferences initialization for HomeFragment
        SharedPreferences sp = getActivity().getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        badPostureCount = sp.getInt("badCount", 0);
        int modeSelect = sp.getInt("mode", -1);
        int phoneVibrate = sp.getInt("phoneVibrate", -1);
        SharedPreferences.Editor editor = sp.edit();

        bendy = (ImageView) view.findViewById(R.id.bendy_guy);
        txv = (TextView) view.findViewById(R.id.bad_posture_count);
        final boolean[] incrementationOccurred = {false};

        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // TODO change this ASAP
        deviceViewModel.getConnectionState().observe(getViewLifecycleOwner(), new Observer<ConnectionState>() {
            @Override
            public void onChanged(ConnectionState connectionState) {
                if (connectionState instanceof ConnectionState.Connected) {
                    Log.e("HomeFragment", "Connected");


                    float roll = ((ConnectionState.Connected) connectionState).getYVal();
                    float flex = ((ConnectionState.Connected) connectionState).getZVal();

                    if (!calibrationDone) {
                        calibrateIncrement += roll;
                        currentCalibrate += 1;
                        if (currentCalibrate == reqCalibrate){
                            calibrationDone = true;
                            calibratedBend = calibrateIncrement / reqCalibrate;
                        }
                    } else {
//                        if (roll >= 0) bendy.setImageResource(R.drawable.body90);
//                        else if (roll >= -2) bendy.setImageResource(R.drawable.body85);
//                        else if (roll >= -4) bendy.setImageResource(R.drawable.body80);
//                        else if (roll >= -6) bendy.setImageResource(R.drawable.body75);
//                        else if (roll >= -8) bendy.setImageResource(R.drawable.body70);
//                        else if (roll >= -9) bendy.setImageResource(R.drawable.body65);
//                        else if (roll >= -10) bendy.setImageResource(R.drawable.body60);
//                        else if (roll >= -12) bendy.setImageResource(R.drawable.body55);
//                        else if (roll >= -14) bendy.setImageResource(R.drawable.body50);
//                        else if (roll >= -16) bendy.setImageResource(R.drawable.body45);
//                        else bendy.setImageResource(R.drawable.body40);
//
//                        int minRoll, maxRoll = -14;
//
//                        switch (modeSelect) {
//                            case 1:
//                                minRoll = -4;
//                                break;
//                            case 2:
//                                minRoll = -8;
//                                break;
//                            case -1:
//                            default:
//                                minRoll = -4;
//                                Toast.makeText(getActivity(), "Error in Setting Mode", Toast.LENGTH_SHORT).show();
//                                Log.e("HomeFragment", "Mode selection failed");
//                                break;
//                        }
//
//                        int maxFlex = -425;
//                        if (flex <= -maxFlex) {
//                            new CountDownTimer(5000, 1000) {
//                                float currentBend = ((ConnectionState.Connected) connectionState).getYVal();
//                                float currentFlex = ((ConnectionState.Connected) connectionState).getZVal();
//
//                                @Override
//                                public void onTick(long millisUntilFinished) {
//                                    if ((currentBend >= minRoll) || (currentBend <= maxRoll) || (currentFlex >= -maxFlex)) cancel();
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                    float lastBend = ((ConnectionState.Connected) connectionState).getYVal();
//                                    float lastFlex = ((ConnectionState.Connected) connectionState).getZVal();
//
//                                    if ((((lastBend <= minRoll) && (lastBend >= maxRoll)) && lastFlex <= -maxFlex) && !incrementationOccurred[0]) {
//                                        badPostureCount++;
//                                        editor.putInt("badCount", badPostureCount).commit();
//                                        incrementationOccurred[0] = true;
//                                        if (phoneVibrate == 1) v.vibrate(500);
//                                    }
//                                }
//                            }.start();
//                        } else {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    incrementationOccurred[0] = false;
//                                }
//                            }, 5000);
//                        }
//
//                        txv.setText(Integer.toString(badPostureCount));
                    }
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

        // TODO recheck device initialization timings
        // TODO change builder strings
        bleButton = (Button) view.findViewById(R.id.ble_scan_button);
        bleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalibrate = 0;
                calibrateIncrement = 0;
                if (!(deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Connected)){
                    deviceViewModel.initializeConnection();
                }
                // Create popup dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Change Title Later");
                builder.setMessage("What do I say here");

                // User has to click the cancel button specifically to avoid accidentally clicking away
                builder.setCancelable(false);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("Start", null);
                final AlertDialog dialog = builder.create();
                dialog.show();
                final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setEnabled(false);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (calibrationDone) {
                            positiveButton.setEnabled(true);
                        }
                    }
                }, 0, 1000);

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                    }
                });
//                builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Uninitialized) {
//                            deviceViewModel.initializeConnection();
//                            Log.e("HomeFragment", "InitializeConnection");
//                            Toast.makeText(getActivity(), "Initializing connection", Toast.LENGTH_SHORT).show();
//                        } else if ((deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Connected) || (deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Disconnected)){
//                            Toast.makeText(getActivity(), "Recalibrating", Toast.LENGTH_SHORT).show();
//                            deviceViewModel.disconnect();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    deviceViewModel.initializeConnection();
//                                }
//                            }, 3000);
//
//                        }
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
        });

        return view;
    }

}
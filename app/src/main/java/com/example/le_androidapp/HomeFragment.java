package com.example.le_androidapp;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

//import com.example.finaldb.dao.UserDao;
import com.example.le_androidapp.data.DeviceResult;
import com.example.le_androidapp.data.ConnectionState;
import com.example.le_androidapp.data.DeviceReceiveManager;
import com.example.le_androidapp.data.ble.DeviceBLEReceiveManager;
import com.example.le_androidapp.tables.ReadData;
import com.example.le_androidapp.util.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

//import com.example.finaldb.source.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.flow.MutableSharedFlow;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    @Inject
    DeviceViewModel deviceViewModel;

    SettingsFragment settingsFragment = new SettingsFragment();

    // View and Button Initializations
    ImageButton settingsButton;
    Button bleButton;
    SwitchCompat sensorSwitch;

    TextView txv;
    TextView sensorDisplay;

    ImageView bendy;

    // UI Thread Initializations
    private Handler handler;
    private Runnable runnable;
    private Timer timer;

    // Temporary BPC Value
    int badPostureCount = 0;

    // Standardization Values
    final private double pitchDifference = 26.67;
    final private double baseAngle = 90.0;
    final private double marginOfError = 10;
    final private double flexSensitivityAdjustment = 15;

    // Initialization for Calibration
    private float pitchSummation = 0;
    private float flexSummation = 0;

    private float calibratedPitch = 0;
    private float calibratedFlex = 0;
    private double calibratedBend = 0;

    private boolean calibrationDone = false;

    private int currentCalibrate = 0;
    final private int reqCalibrate = 100;

    private boolean active = false;
    private boolean alertClicked = false;

    private boolean isFlex = true;

    long currentTimeMillis = System.currentTimeMillis();

    DatabaseReference dataRef, userRef;


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
                if (isFlex) sensorDisplay.setText("Flex Sensor");
                else sensorDisplay.setText("Gyro-Pitch");
            }
        }, 0);

    }

    ReadData user = new ReadData();
    String userId = "defaultUserId";
    private void saveBadPostureCount(int badPostureCount) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Read Data").child(userId);
        dataRef.child("Daily Event").setValue(badPostureCount)
                .addOnSuccessListener(aVoid -> {
                    Log.d("BPCount", "Count = " + badPostureCount);
                })
                .addOnFailureListener(e -> {
                    Log.e("BPCount", "Failed to save count: " + e.getMessage());
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        final boolean[] isChecking = {false};
        final boolean[] incrementationOccurred = {false};

        // Retrieving values from SharedPreferences
        SharedPreferences sp = getActivity().getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        badPostureCount = sp.getInt("badCount", 0);
        isFlex = sp.getBoolean("isFlexSensor", true);
        int phoneVibrate = sp.getInt("phoneVibrate", -1);
        SharedPreferences.Editor editor = sp.edit();

        bendy = (ImageView) view.findViewById(R.id.bendy_guy);
        txv = (TextView) view.findViewById(R.id.bad_posture_count);

        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        deviceViewModel.getConnectionState().observe(getViewLifecycleOwner(), new Observer<ConnectionState>() {
            @Override
            public void onChanged(ConnectionState connectionState) {
                if (connectionState instanceof ConnectionState.Connected) {
                    //Log.e("HomeFragment", "Connected");

                    float pitch = ((ConnectionState.Connected) connectionState).getPitch();
                    float flex = ((ConnectionState.Connected) connectionState).getFlex();

                    if (!calibrationDone) {
                        pitchSummation += pitch;
                        flexSummation += flex;
                        currentCalibrate += 1;

                        Log.e("Calibration", "Current = " + currentCalibrate +
                                "| Pitch = " + pitch +
                                "| PitchSummation = " + pitchSummation +
                                "| Flex = " + flex +
                                "| FlexSummation = " + flexSummation);

                        if (currentCalibrate == reqCalibrate) {
                            calibrationDone = true;
                            alertClicked = false;

                            calibratedPitch = pitchSummation / reqCalibrate;
                            calibratedFlex = flexSummation / reqCalibrate;

                            calibratedBend = (calibratedFlex + Math.min(0, mathMap(calibratedPitch, 0,
                                    0 - pitchDifference, baseAngle, 0)) / 2) ;

                        }
                    }

                    // TODO
                    if (active) {
                        // Toast.makeText(getActivity(), "device actively working", Toast.LENGTH_SHORT).show();

                        // Conversion of pitch data
                        double currentPitch = Math.min(baseAngle, mathMap(pitch, 0, 0 - pitchDifference,
                                baseAngle, 0));
                        double currentFlex = Math.min(baseAngle, flex + (calibratedFlex - baseAngle));

                        double currentBend;
                        if (isFlex) {
//                            calibratedBend = Math.min(baseAngle, calibratedFlex);
                            currentBend = currentFlex;
                        } else {
//                            calibratedBend = mathMap(calibratedPitch, 0,
//                                    0 - pitchDifference, baseAngle, 0);
                            currentBend = currentPitch;
                        }

                        //double currentBend = (currentPitch + currentFlex) / 2;

                        Log.e("Active", "| Adjusted Pitch = " + currentPitch +
                                "| Adjusted Flex = " + currentFlex +
                                "| Average Bend = " + currentBend);

                        // Bendy guy display determination
                        if (currentBend + marginOfError >= 90) bendy.setImageResource(R.drawable.body90);
                        else if (currentBend + marginOfError >= 85) bendy.setImageResource(R.drawable.body85);
                        else if (currentBend + marginOfError >= 80) bendy.setImageResource(R.drawable.body80);
                        else if (currentBend + marginOfError >= 75) bendy.setImageResource(R.drawable.body75);
                        else if (currentBend + marginOfError >= 70) bendy.setImageResource(R.drawable.body70);
                        else if (currentBend + marginOfError >= 65) bendy.setImageResource(R.drawable.body65);
                        else if (currentBend + marginOfError >= 60) bendy.setImageResource(R.drawable.body60);
                        else if (currentBend + marginOfError >= 55) bendy.setImageResource(R.drawable.body55);
                        else if (currentBend + marginOfError >= 50) bendy.setImageResource(R.drawable.body50);
                        else if (currentBend + marginOfError >= 45) bendy.setImageResource(R.drawable.body45);
                        else bendy.setImageResource(R.drawable.body40);

                        // Bad Posture Determination Algorithm
                        // TODO
                        // Log.e("Bad Posture", "Diff = " + (averageBend - calibratedBend));

                        // Log.e("Back", "Bad is " + isBad);
                        boolean isBad;
                        if (isFlex) {
                            isBad = (calibratedBend - currentBend) > marginOfError + flexSensitivityAdjustment;
                        } else {
                            isBad = (calibratedBend - currentBend) > marginOfError;
                        }

                        if (isBad && !incrementationOccurred[0]) {
                            if (phoneVibrate == 1) v.vibrate(500);
                            incrementationOccurred[0] = true;
                            badPostureCount++;
                            Log.e("BPCount", "Count = " + badPostureCount);
                            editor.putInt("badCount", badPostureCount).commit();
                            saveBadPostureCount(badPostureCount);
                        } else if (!isBad) {
                            incrementationOccurred[0] = false;
                        }

                        txv.setText(Integer.toString(badPostureCount));
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

        sensorSwitch = (SwitchCompat) view.findViewById(R.id.sensorSwitch);
        sensorDisplay = (TextView) view.findViewById(R.id.currentSensor);
        sensorSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorSwitch.isChecked()) {
                    isFlex = true;
                    editor.putBoolean("isFlexSensor", true).commit();
                    sensorDisplay.setText("Flex Sensor");
                }
                else {
                    isFlex = false;
                    editor.putBoolean("isFlexSensor", false).commit();
                    sensorDisplay.setText("Gyro-Pitch");
                }
            }
        });

        // BLE Button Actions
        bleButton = (Button) view.findViewById(R.id.ble_scan_button);
        bleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active = false;
                alertClicked = true;
                currentCalibrate = 0;
                pitchSummation = 0;
                flexSummation = 0;

                if (!(deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Connected)) {
                    deviceViewModel.initializeConnection();
                    calibrationDone = false;
                } else if (deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Connected) {
                    deviceViewModel.disconnect();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deviceViewModel.initializeConnection();
                            calibrationDone = false;
                        }
                    }, 3000);
                }

                // AlertDialog Message
                String[] messages = {"Note that the device needs to be calibrated before use.",
                        " The device will be calibrated in a few seconds.",
                        " If the device is not calibrated, data will be inaccurate."};
                String alertMessage = "<ul>";
                for (String message : messages) {
                    alertMessage += "<li>" + message + "</li>";
                }
                alertMessage += "</ul>";

                // Create popup dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Calibration");
                builder.setMessage(HtmlCompat.fromHtml(alertMessage, HtmlCompat.FROM_HTML_MODE_LEGACY));

                // User has to click the cancel button specifically to avoid accidentally clicking away
                builder.setCancelable(false);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Cancelled. Device not calibrated.", Toast.LENGTH_SHORT).show();
                        active = true;
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Start", null);
                final AlertDialog dialog = builder.create();
                dialog.show();
                final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setEnabled(false);

                final int[] sampleCount = {0};

                handler = new Handler(Looper.getMainLooper());
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        positiveButton.setEnabled(true);
                    }
                };

                // Checks every second to determine whether positive button can be enabled
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (calibrationDone && !alertClicked
                                && positiveButton != null && runnable != null
                                && isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(runnable);
                        }
                    }
                }, 0, 1000);

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                        active = true;
                        dialog.dismiss();
                    }
                });

            }
        });

        return view;
    }

    // To avoid NullPointerError
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) timer.cancel();
        if (handler != null) handler.removeCallbacks(runnable);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (timer != null) timer.cancel();
        if (handler != null) handler.removeCallbacks(runnable);
    }

    // Math.map() function
    public static double mathMap(double value, double inputMin, double inputMax, double outputMin, double outputMax) {
        return (value - inputMin) / (inputMax - inputMin) * (outputMax - outputMin) + outputMin;
    }
}

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.ValueEventListener;

//import com.example.finaldb.source.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    private Handler handler;
    private Runnable runnable;
    private Timer timer;

    private View view;

    private int badPostureCount = 0;

    private float calibrateIncrement = 0;
    private float calibratedBend = 0;
    private boolean calibrationDone = false;
    private int currentCalibrate = 0;
    final private int reqCalibrate = 100;

    private boolean active = false;
    private boolean alertClicked = false;

    long currentTimeMillis = System.currentTimeMillis();

    DatabaseReference usersRef;
    DatabaseReference dataRef;

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
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // SharedPreferences initialization for HomeFragment
        SharedPreferences sp = getActivity().getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        badPostureCount = sp.getInt("badCount", 0);
        int modeSelect = sp.getInt("mode", -1);
        int phoneVibrate = sp.getInt("phoneVibrate", -1);
        SharedPreferences.Editor editor = sp.edit();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTime = dateFormat.format(new Date(currentTimeMillis));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("User Data");
        dataRef = database.getReference("Read Data");

        String userId = usersRef.push().getKey();

        // Write some data to the Firebase database
        String dataId = dataRef.push().getKey();
        ReadData readData = new ReadData(dataId, 10, formattedTime);
        dataRef.child(dataId).setValue(readData);

        // Query the data based on user ID
        Query query = dataRef.orderByChild("dataId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot readSnapshot : dataSnapshot.getChildren()) {
                    ReadData readData = readSnapshot.getValue(ReadData.class);
                    Log.d(TAG, "Read data: " + readData.getDataId() + ", " + readData.getEventBad());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });



        bendy = (ImageView) view.findViewById(R.id.bendy_guy);
        txv = (TextView) view.findViewById(R.id.bad_posture_count);
        final boolean[] incrementationOccurred = {false};

        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // TODO change this ASAP
        deviceViewModel.getConnectionState().observe(getViewLifecycleOwner(), new Observer<ConnectionState>() {
            @Override
            public void onChanged(ConnectionState connectionState) {
                if (connectionState instanceof ConnectionState.Connected) {
                    //Log.e("HomeFragment", "Connected");


                    float pitch = ((ConnectionState.Connected) connectionState).getPitch();
                    float flex = ((ConnectionState.Connected) connectionState).getFlex();

                    if (!calibrationDone) {
                        calibrateIncrement += pitch;
                        currentCalibrate += 1;
                        Log.e("Calibration", "Increment = " + calibrateIncrement +
                                "; Current = " + currentCalibrate);
                        if (currentCalibrate == reqCalibrate){
                            calibrationDone = true;
                            alertClicked = false;
                            calibratedBend = calibrateIncrement / reqCalibrate;
                            Log.e("Calibration", "Done: " + calibrationDone +
                                    "; Calibrated Bend = " + calibratedBend);
                        }
                    }

                    if (active) {
                        Toast.makeText(getActivity(), "device actively working", Toast.LENGTH_SHORT).show();
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
                active = false;
                alertClicked = true;
                currentCalibrate = 0;
                calibrateIncrement = 0;
                if (!(deviceViewModel.getConnectionState().getValue() instanceof ConnectionState.Connected)){
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

                String[] messages = {"Note that the device needs to be calibrated before use.",
                        " The device will be calibrated in a few seconds.",
                        " If the device is not calibrated, data will be inaccurate."};
                String alertMessage = "<ul>";
                for (String message: messages) {
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
}
package com.example.le_androidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

    ImageButton notificationsButton;
    ImageButton settingsButton;

    Button badPostureButtonResting;
    Button badPostureButtonWorking;

    TextView txv;

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

        return view;
    }
}
package com.example.le_androidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ImageButton notificationsButton;
    ImageButton settingsButton;

    Button badPostureButtonResting;
    Button badPostureButtonWorking;

    TextView txv;

    private int badPostureCount = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("modeAndScreen", Context.MODE_PRIVATE);
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
package com.example.le_androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton backButton;

    Switch notificationSwitch;
    Switch phoneVibrateSwitch;
    Switch deviceVibrateSwitch;

    Button exportDownload;
    Button supportDevs;

    SharedPreferences sp;
    FragmentTransaction fr;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        backButton = (ImageButton) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp = getActivity().getSharedPreferences("modeAndScreen", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("currentScreen", "home");
                editor.commit();

                fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.container, new HomeFragment());
                fr.commit();
            }
        });

        notificationSwitch = (Switch) view.findViewById(R.id.setting_switch1);
        notificationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notificationSwitch.isChecked())
                    Toast.makeText(getActivity(), "Notifications Turned On", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getActivity(), "Notifications Turned Off", Toast.LENGTH_SHORT).show();
            }
        });

        phoneVibrateSwitch = (Switch) view.findViewById(R.id.setting_switch2);
        phoneVibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneVibrateSwitch.isChecked())
                    Toast.makeText(getActivity(), "Phone Vibration Turned On", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getActivity(), "Phone Vibration Turned Off", Toast.LENGTH_SHORT).show();
            }
        });

        deviceVibrateSwitch = (Switch) view.findViewById(R.id.setting_switch3);
        deviceVibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deviceVibrateSwitch.isChecked())
                    Toast.makeText(getActivity(), "Device Vibration Turned On", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getActivity(), "Device Vibration Turned Off", Toast.LENGTH_SHORT).show();
            }
        });

        exportDownload = (Button) view.findViewById(R.id.export_button);
        exportDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Export and Download Information", Toast.LENGTH_SHORT).show();
            }
        });

        supportDevs = (Button) view.findViewById(R.id.dev_support);
        supportDevs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "The Devs Need Money", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.patreon.com"));
                startActivity(intent);
            }
        });
        return view;
    }
}
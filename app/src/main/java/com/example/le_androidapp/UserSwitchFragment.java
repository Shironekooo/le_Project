package com.example.le_androidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class UserSwitchFragment extends Fragment {

    SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(getContext());

    Button switchToMain;
    Button switchToGuest;

    public UserSwitchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_switch, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        FragmentTransaction fr = getFragmentManager().beginTransaction();

        switchToMain = (Button) view.findViewById(R.id.switchUserMain);
        switchToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("currentScreen", "profile").commit();
                fr.replace(R.id.container, new ProfileFragment()).commit();
                Toast.makeText(getActivity(), "Switched to Main User", Toast.LENGTH_SHORT).show();
            }
        });

        switchToGuest = (Button) view.findViewById(R.id.switchUserGuest);
        switchToGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("currentScreen", "profile").commit();
                fr.replace(R.id.container, new ProfileFragment()).commit();
                Toast.makeText(getActivity(), "Switched to Guest User", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
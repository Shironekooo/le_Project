package com.example.le_androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    Button switchUser;
    Button logout;

    SharedPreferences sp;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sp = getActivity().getSharedPreferences("modeAndScreen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        switchUser = (Button) view.findViewById(R.id.switch_user);
        switchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("currentScreen", "userlist");
                editor.commit();

                Intent intent = new Intent(getContext(), UserListActivity.class);
                startActivity(intent);
            }
        });

        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "To Logout", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
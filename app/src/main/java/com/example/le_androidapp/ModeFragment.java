package com.example.le_androidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//Mode 1 - Working
//Mode 2 - Resting

public class ModeFragment extends Fragment {

    Switch modeSwitch;

    Button faqButton;

    SharedPreferences sp;
    
    public ModeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mode, container, false);

        sp = getActivity().getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // Switch for mode selection
        modeSwitch = (Switch) view.findViewById(R.id.mode_change);
        modeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modeSwitch.isChecked()) {
                    editor.putInt("mode", 2);
                    editor.commit();
                    Toast.makeText(getActivity(), "Switched to Resting Mode", Toast.LENGTH_SHORT).show();
                }
                else {
                    editor.putInt("mode", 1);
                    editor.commit();
                    Toast.makeText(getActivity(), "Switched to Working Mode", Toast.LENGTH_SHORT).show();
                }
            }
        });

        faqButton = (Button) view.findViewById(R.id.faq_button);
        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("currentScreen", "faq");
                editor.commit();

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.container, new FaqFragment());
                fr.commit();
            }
        });

        return view;
    }
}
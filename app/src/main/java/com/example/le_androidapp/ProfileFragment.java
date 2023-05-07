package com.example.le_androidapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

public class ProfileFragment extends Fragment {

    Button switchUser;
    Button logout;

    SharedPreferences sp;
    TextView name, age, gender, contact;
    ImageView profile;



    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

       /* name = (TextView) view.findViewById(R.id.userName);
        age = (TextView) view.findViewById(R.id.profileAge);
        gender = (TextView) view.findViewById(R.id.profileGender);
        contact = (TextView) view.findViewById(R.id.profileContact);
        profile = (ImageView) view.findViewById(R.id.profileImage);

        Bundle bundle = getArguments();
        if(bundle != null){
            name.setText(bundle.getString("Name"));
            age.setText(bundle.getString("Age"));
            gender.setText(bundle.getString("Gender"));
            Glide.with(this).load(bundle.getString("Image")).into(profile);
        }*/

        switchUser = (Button) view.findViewById(R.id.switch_user);
        switchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrimaryActivity.class);
                startActivity(intent);
                /*
                editor.putString("currentScreen", "switchUser");
                editor.commit();

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.container, new UserSwitchFragment());
                fr.commit();

                 */
            }
        });

        return view;
    }
}
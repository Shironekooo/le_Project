package com.example.le_androidapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.le_androidapp.tables.UserClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    Button switchUser;
    Button logout;

    SharedPreferences sp;
    TextView nameProfile, ageProfile, genderProfile, contactProfile;
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

        nameProfile = (TextView) view.findViewById(R.id.userName);
        ageProfile = (TextView) view.findViewById(R.id.profileAge);
        genderProfile = (TextView) view.findViewById(R.id.profileGender);
        contactProfile = (TextView) view.findViewById(R.id.profileContact);
        profile = (ImageView) view.findViewById(R.id.profileImage);

        String userId = "-NUouRVxUtfSNPt7Tpin";

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("User Data").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("firstName").getValue(String.class);
                    String age = dataSnapshot.child("userAge").getValue(String.class);
                    String gender = dataSnapshot.child("userGender").getValue(String.class);
                    String contact = dataSnapshot.child("contactNumber").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("dataImage").getValue(String.class);

                    // Display the user's details in the UI
                    nameProfile.setText(name);
                    ageProfile.setText(age);
                    genderProfile.setText(gender);
                    contactProfile.setText(contact);
                    Glide.with(ProfileFragment.this).load(profileImageUrl).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

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
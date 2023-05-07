package com.example.le_androidapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.le_androidapp.adapts.ReadAdapter;
import com.example.le_androidapp.data.ConnectionState;
import com.example.le_androidapp.tables.ReadData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;


// TODO change to be able to show history or actionable data not raw data

@AndroidEntryPoint
public class OverviewFragment extends Fragment {

    // Inject your dependencies here

    TextView eventText;
    DatabaseReference dataRef;

    ValueEventListener eventListener;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        eventText = view.findViewById(R.id.total_event);

        RecyclerView recyclerView = view.findViewById(R.id.dataRecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        List<ReadData> readDataList = new ArrayList<>();

        // Initialize the adapter with the empty readDataList
        ReadAdapter readAdapter = new ReadAdapter(getContext(), readDataList);
        recyclerView.setAdapter(readAdapter);

        // Get the reference to the "Read Data" node in Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dataRef = FirebaseDatabase.getInstance().getReference("Read Data");



        // Query the data based on user ID
        String userId = "-NUnto4vMGTn3HlEScVx";
        eventListener = dataRef.child(userId).child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Integer eventCountValue = dataSnapshot.child("eventCount").getValue(Integer.class);
//                if (eventCountValue != null) {
//                    int eventCount = eventCountValue.intValue();
//                    eventText.setText(String.valueOf(eventCount));
//                }
//                // Retrieve the total event and event duration from Firebase
//                int totalEvent = dataSnapshot.child("Total Event").getValue(Integer.class);
//                long eventDuration = dataSnapshot.child("Event Duration (min)").getValue(Long.class);
//
//                // Display the total event and event duration in the TextView
//                //eventText.setText("Total Event: " + totalEvent + "\nEvent Duration (min): " + eventDuration);
//                eventText.setText("Event Duration (min): " + eventDuration);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove the event listener when the view is destroyed
        if (eventListener != null) {
            dataRef.removeEventListener(eventListener);
        }
    }

}

package com.example.le_androidapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.le_androidapp.adapts.ReadAdapter;
import com.example.le_androidapp.adapts.ReadViewHolder;
import com.example.le_androidapp.data.ConnectionState;
import com.example.le_androidapp.tables.ReadData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    TextView eventValue, timeValue;
    DatabaseReference dataRef;

    private FirebaseRecyclerAdapter<ReadData, ReadViewHolder> readAdapter;

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

        eventValue = view.findViewById(R.id.total_event);
        timeValue = view.findViewById(R.id.total_time);
        RecyclerView recyclerView = view.findViewById(R.id.dataView);


        // Get the reference to the "Read Data" node in Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dataRef = database.getReference("Read Data");

        // Query the data based on user ID
        String userId = "-NUouRVxUtfSNPt7Tpin";
        Query query = dataRef.child(userId).child("History");

        FirebaseRecyclerOptions<ReadData> options =
                new FirebaseRecyclerOptions.Builder<ReadData>()
                        .setQuery(query, ReadData.class)
                        .build();

        readAdapter = new FirebaseRecyclerAdapter<ReadData, ReadViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReadViewHolder holder, int position, @NonNull ReadData model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public ReadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
                return new ReadViewHolder(view);
            }
        };
        recyclerView.setAdapter(readAdapter);

        dataRef.child(userId).child("History").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve the total event from Firebase
                int totalEvent = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ReadData readData = child.getValue(ReadData.class);
                    if (readData != null) {
                        totalEvent += readData.getTotalEvent();
                    }
                }

                // Display the total event in the TextView
                eventValue.setText(String.valueOf(totalEvent));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Start listening for changes in the database and update the adapter
        readAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stop listening for changes in the database and update the adapter
        readAdapter.stopListening();
    }
}

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        // Remove the event listener when the view is destroyed
//        if (eventListener != null) {
//            dataRef.removeEventListener(eventListener);
//        }
//    }
//
//}

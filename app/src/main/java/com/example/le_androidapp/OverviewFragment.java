package com.example.le_androidapp;

import static android.content.ContentValues.TAG;

import static com.example.le_androidapp.tables.ReadData.totalEvent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.le_androidapp.adapts.ReadAdapter;
import com.example.le_androidapp.data.ConnectionState;
import com.example.le_androidapp.tables.ReadData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;


// TODO change to be able to show history or actionable data not raw data

@AndroidEntryPoint
public class OverviewFragment extends Fragment {

    RecyclerView recyclerView;
    public ReadAdapter readAdapter;
    TextView eventValue, timeValue;
    List<ReadData> readDataList = new ArrayList<>();
    DatabaseReference dataRef;
    private String userId = "flex15";
    ValueEventListener eventListener;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

//        eventValue = view.findViewById(R.id.total_event);
//        timeValue = view.findViewById(R.id.total_time);
        recyclerView = view.findViewById(R.id.dataView);

        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        readAdapter = new ReadAdapter(this, readDataList);
        recyclerView.setAdapter(readAdapter);


        // Get the reference to the "Read Data" node in Firebase
        dataRef = FirebaseDatabase.getInstance().getReference("Read Data");
        dialog.show();


        // Write some data to the Firebase database
        String userId = dataRef.getKey();
        ReadData readData = new ReadData();
        dataRef.child(userId).setValue(readData);

        // Query the data based on user ID
        eventListener = dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot readSnapshot : dataSnapshot.getChildren()) {
                    int totalEvent = 0;
                    ReadData theData = readSnapshot.getValue(ReadData.class);
                    readDataList.add(theData);
                    eventValue.setText(totalEvent);
                }



                readAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the sorting options
        RadioGroup sortGroup = view.findViewById(R.id.sort_group);
        sortGroup.setVisibility(View.VISIBLE);

        RadioButton eventAsc = view.findViewById(R.id.event_asc_btn);
        RadioButton eventDesc = view.findViewById(R.id.event_desc_btn);
        RadioButton timeAsc = view.findViewById(R.id.time_asc_btn);
        RadioButton timeDesc = view.findViewById(R.id.time_desc_btn);

        eventAsc.setOnClickListener(v -> {
            Collections.sort(readDataList, (Comparator<? super ReadData>) Comparator.comparingInt(ReadData::getTotalEvent));
            readAdapter.notifyDataSetChanged();
        });

        eventDesc.setOnClickListener(v -> {
            Collections.sort(readDataList, Comparator.comparingInt(ReadData::getTotalEvent).reversed());
            readAdapter.notifyDataSetChanged();
        });

        timeAsc.setOnClickListener(v -> {
            Collections.sort(readDataList, Comparator.comparingInt(ReadData::getTotalTime));
            readAdapter.notifyDataSetChanged();
        });

        timeDesc.setOnClickListener(v -> {
            Collections.sort(readDataList, Comparator.comparingInt(ReadData::getTotalTime).reversed());
            readAdapter.notifyDataSetChanged();
        });
    }
}

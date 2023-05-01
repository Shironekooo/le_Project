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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.le_androidapp.adapts.HistoryAdapter;
import com.example.le_androidapp.data.ConnectionState;
import com.example.le_androidapp.tables.History;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


// TODO change to be able to show history or actionable data not raw data

@AndroidEntryPoint
public class OverviewFragment extends Fragment {

    @Inject DeviceViewModel deviceViewModel;

    TextView pitchView;
    TextView flexView;

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private DatabaseReference historyRef, usersRef;
    long currentTimeMillis = System.currentTimeMillis();
    private List<History> historyList;


    public OverviewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        pitchView = (TextView) view.findViewById(R.id.pitchView);
        flexView = (TextView) view.findViewById(R.id.flexView);

        deviceViewModel.getConnectionState().observe(getViewLifecycleOwner(), new Observer<ConnectionState>() {
            @Override
            public void onChanged(ConnectionState connectionState) {
                // Log.e("OverviewFragment", "LifeCycleOnChanged");
                if (connectionState instanceof ConnectionState.Connected) {
                    pitchView.setText(Float.toString(((ConnectionState.Connected) connectionState).getPitch()));
                    flexView.setText(Float.toString(((ConnectionState.Connected) connectionState).getFlex()));
                }
            }
        });


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTime = dateFormat.format(new Date(currentTimeMillis));

        recyclerView = view.findViewById(R.id.recycleHisto);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        historyList = new ArrayList<>();

        // Initialize the adapter with the empty historyList
        historyAdapter = new HistoryAdapter(OverviewFragment.this, historyList);
        recyclerView.setAdapter(historyAdapter);

        // Get the reference to the "history" node in Firebase
        historyRef = FirebaseDatabase.getInstance().getReference("History");

        usersRef = FirebaseDatabase.getInstance().getReference("User Data");
        String userId = usersRef.push().getKey();

        // Write some data to the Firebase database
        String historyId = historyRef.push().getKey();
        History history = new History(historyId,"50", formattedTime, "Working", "123");
        historyRef.child(historyId).setValue(history);

        // Query the data based on user ID
        Query query = historyRef.orderByChild("historyId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot readSnapshot : dataSnapshot.getChildren()) {
                    History history1 = readSnapshot.getValue(History.class);
                    Log.d(TAG, "History: " + history1.getHistoryId() + ", " + history1.getTotalEvent());
                }
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });


        return view;
    }
}
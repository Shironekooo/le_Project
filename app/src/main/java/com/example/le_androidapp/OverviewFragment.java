package com.example.le_androidapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.le_androidapp.adapts.HistoryAdapter;
import com.example.le_androidapp.data.ConnectionState;
import com.example.le_androidapp.tables.History;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    TextView pitchView, eventAsc, eventDesc, dateAsc, dateDesc;
    TextView flexView;

    Button sortButton;
    private LinearLayout sortView;
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private DatabaseReference historyRef, usersRef;
    long currentTimeMillis = System.currentTimeMillis();
    private List<History> historyList;
    private SearchView searchView;

    boolean sortHidden = true;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        sortButton = (Button) view.findViewById(R.id.sortBtn);
        //initSearchWidgets();
        //initWidgets();
        /*setUpData();
        setUpList();
        setUpOnclickListener();*/
       /* pitchView = (TextView) view.findViewById(R.id.pitchView);
        flexView = (TextView) view.findViewById(R.id.flexView);*/

        hideSortTab();
        showSortTab();

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
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        historyList = new ArrayList<>();

        // Initialize the adapter with the empty historyList
        historyAdapter = new HistoryAdapter(getContext(), historyList);
        recyclerView.setAdapter(historyAdapter);

        // Get the reference to the "history" node in Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("User Data");
        historyRef = FirebaseDatabase.getInstance().getReference("History");
        dialog.show();

        // Write some data to the Firebase database
        String userId = usersRef.getKey();
        History history = new History(userId, "", formattedTime);
        historyRef.child(userId).setValue(history);

        // Query the data based on user ID
        eventListener = historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot readSnapshot : dataSnapshot.getChildren()) {
                    History history1 = readSnapshot.getValue(History.class);
                    historyList.add(history1);
                }
                historyAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        return view;
    }




    /*private void initSearchWidgets() {

        searchView.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }*/

   public void showSortTab() {
        sortView.setVisibility(View.VISIBLE);
        sortButton.setText("HIDE");
    }

    public void hideSortTab(){
        sortView.setVisibility(View.GONE);
        sortButton.setText("SORT");
    }

    /*public void eventAscOnly(View view) {
    }

    public void eventDescOnly(View view) {
    }

    public void dateAscOnly(View view) {
    }

    public void dateDescOnly(View view) {
    }*/

    public void sortTapped(View view) {
        if (sortHidden == true) {
            sortHidden = false;
            showSortTab();
        } else {
            sortHidden = true;
            hideSortTab();
        }
    }
}
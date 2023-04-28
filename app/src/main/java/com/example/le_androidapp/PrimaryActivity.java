
package com.example.le_androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrimaryActivity extends AppCompatActivity {

    Button regbutton;
    RecyclerView recyclerView;
    List<UserClass> userList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

    regbutton = findViewById(R.id.newbtn);
    recyclerView = findViewById(R.id.recyclerView);

    GridLayoutManager gridLayoutManager = new GridLayoutManager(PrimaryActivity.this,1);
    recyclerView.setLayoutManager(gridLayoutManager);

    AlertDialog.Builder builder = new AlertDialog.Builder(PrimaryActivity.this);
    builder.setCancelable(false);
    builder.setView(R.layout.loading_layout);
    AlertDialog dialog = builder.create();
    dialog.show();

    userList = new ArrayList<>();

    MyAdapter adapter = new MyAdapter(PrimaryActivity.this, userList);
    recyclerView.setAdapter(adapter);

    databaseReference = FirebaseDatabase.getInstance().getReference("User Data");
    dialog.show();

    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            userList.clear();
            for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                UserClass userClass=itemSnapshot.getValue(UserClass.class);
                userList.add(userClass);
            }
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            dialog.dismiss();
        }
    });


    regbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PrimaryActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    });

    }
}
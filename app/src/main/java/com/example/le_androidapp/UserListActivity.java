package com.example.le_androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class UserListActivity extends AppCompatActivity {

    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        initWidgets();
        setUserAdapter();
        loadFromDbToMemory();
        setOnClickListener();
    }

    private void setOnClickListener() {
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AppUser selectedUser = (AppUser) userListView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), UserDetailActivity.class);
                editNoteIntent.putExtra(AppUser.USER_EDIT_EXTRA, selectedUser.getId());
                startActivity(editNoteIntent);
            }
        });
    }

    private void loadFromDbToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateUserListArray();
    }

    private void initWidgets() {
        userListView = findViewById(R.id.userListView);
    }

    private void setUserAdapter() {
        UserAdapter userAdapter = new UserAdapter(getApplicationContext(), AppUser.nonDeletedNotes());
        userListView.setAdapter(userAdapter);
    }

    public void newUser(View view) {
        Intent newUserIntent = new Intent(this, UserDetailActivity.class);
        startActivity(newUserIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserAdapter();
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sp = getSharedPreferences("modeAndScreen",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("currentScreen", "profile");
        editor.commit();
        finish();
    }
}
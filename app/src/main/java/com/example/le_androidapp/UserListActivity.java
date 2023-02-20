package com.example.le_androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    }

    private void initWidgets() {
        userListView = findViewById(R.id.userListView);
    }

    private void setUserAdapter() {
        UserAdapter userAdapter = new UserAdapter(getApplicationContext(), AppUser.userArrayList);
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
}
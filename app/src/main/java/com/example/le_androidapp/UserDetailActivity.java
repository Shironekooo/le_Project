package com.example.le_androidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UserDetailActivity extends AppCompatActivity {
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initWidgets();
    }

    public void initWidgets() {
        nameEditText = findViewById(R.id.nameEditText);
    }

    public void saveUser(View view) {
        String name = String.valueOf(nameEditText.getText());

        int id = AppUser.userArrayList.size();
        AppUser newUser = new AppUser(id, name, 0);
        AppUser.userArrayList.add(newUser);
        finish();
    }
}
package com.example.le_androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class UserDetailActivity extends AppCompatActivity {

    private EditText nameEditText;
    private AppUser selectedUser;

    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initWidgets();
        checkForEditUser();
    }

    public void initWidgets() {
        nameEditText = findViewById(R.id.nameEditText);
        deleteButton = findViewById(R.id.deleteUserButton);
    }

    private void checkForEditUser() {
        Intent previousIntent = getIntent();

        int passedUserId = previousIntent.getIntExtra(AppUser.USER_EDIT_EXTRA, -1);
        selectedUser = AppUser.getUserForId(passedUserId);

        if (selectedUser != null) {
            nameEditText.setText(selectedUser.getUsername());
        }
        else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveUser(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String name = String.valueOf(nameEditText.getText());


        if (selectedUser == null) {
            int id = AppUser.userArrayList.size();
            AppUser newUser = new AppUser(id, name, 0);
            AppUser.userArrayList.add(newUser);
            sqLiteManager.addUserToDb(newUser);
        }
        else {
            selectedUser.setUsername(name);
            sqLiteManager.updateUserInDb(selectedUser);
        }

        finish();
    }

    public void deleteUser(View view) {
        selectedUser.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateUserInDb(selectedUser);
        finish();
    }
}

package com.example.le_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PrimaryActivity extends AppCompatActivity {

    Button regbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

    regbutton = findViewById(R.id.newbtn);

    regbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PrimaryActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    });

    }
}
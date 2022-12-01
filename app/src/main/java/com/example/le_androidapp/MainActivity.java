package com.example.le_androidapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.mode);
    }
    ArticlesFragment articlesFragment = new ArticlesFragment();
    ModeFragment modeFragment = new ModeFragment();
    OverviewFragment overviewFragment = new OverviewFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.articles:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, articlesFragment).commit();
                return true;

            case R.id.mode:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, modeFragment).commit();
                return true;

            case R.id.overview:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, overviewFragment).commit();
                return true;

            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
        }
        return false;
    }
}
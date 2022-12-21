package com.example.le_androidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("modeAndScreen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("mode", 1);
        editor.putString("currentScreen", "blank");
        editor.commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    HomeFragment homeFragment = new HomeFragment();
    ArticlesFragment articlesFragment = new ArticlesFragment();
    ModeFragment modeFragment = new ModeFragment();
    OverviewFragment overviewFragment = new OverviewFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        sp = getSharedPreferences("modeAndScreen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        switch(item.getItemId()) {
            case R.id.home:
                editor.putString("currentScreen", "home");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;

            case R.id.articles:
                editor.putString("currentScreen", "articles");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, articlesFragment).commit();
                return true;

            case R.id.mode:
                editor.putString("currentScreen", "mode");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, modeFragment).commit();
                return true;

            case R.id.overview:
                editor.putString("currentScreen", "overview");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, overviewFragment).commit();
                return true;

            case R.id.profile:
                editor.putString("currentScreen", "profile");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sp = getSharedPreferences("modeAndScreen", Context.MODE_PRIVATE);
        String screen = sp.getString("currentScreen", "blank");
        SharedPreferences.Editor editor = sp.edit();

        switch (screen) {
            case "articles":
            case "mode":
            case "overview":
            case "profile":
            case "settings":
            case "notifications":
                bottomNavigationView.setSelectedItemId(R.id.home);
                editor.putString("currentScreen", "home");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                break;
            case "faq":
                bottomNavigationView.setSelectedItemId(R.id.mode);
                editor.putString("currentScreen", "mode");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, modeFragment).commit();
                break;
            case "home":
                super.onBackPressed();
                break;
        }
    }
}
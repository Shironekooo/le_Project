package com.example.le_androidapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
@SuppressLint("MissingPermission")
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    SharedPreferences sp;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static final int REQUEST_ENABLE_BT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // SharedPreferences initialization
        sp = getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("badCount", 0);
        editor.putBoolean("isFlexSensor", true);
        editor.putInt("phoneVibrate", 1);
        editor.putString("currentScreen", "blank");
        editor.commit();

        // Bottom Navigation View initialization and color setting
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        ColorStateList colorStateList = getResources().getColorStateList(R.color.bottom_nav_icon_color);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setItemIconTintList(colorStateList);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    // Creation of fragments to avoid fragment destruction during fragment replacement
    HomeFragment homeFragment = new HomeFragment();
    ArticlesFragment articlesFragment = new ArticlesFragment();
    FaqFragment faqFragment = new FaqFragment();
    OverviewFragment overviewFragment = new OverviewFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        sp = getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // Bottom Navigation View Fragment Transactions
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

            case R.id.faq:
                editor.putString("currentScreen", "faq");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, faqFragment).commit();
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
    protected void onResume() {
        super.onResume();

        // Check for Bluetooth capabilities
        if(bluetoothAdapter == null) {
            Toast.makeText(this, "The device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        // Request Bluetooth enabling
        if(bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            // enableBtIntent requires permission that may be rejected by user
            // currently suppressing MissingPermission with annotation
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    // Fixes back button usage for all fragments rather than immediate app closure
    @Override
    public void onBackPressed() {

        SharedPreferences sp = getSharedPreferences("sharedData", Context.MODE_PRIVATE);
        String screen = sp.getString("currentScreen", "blank");
        SharedPreferences.Editor editor = sp.edit();

        switch (screen) {
            case "articles":
            case "overview":
            case "profile":
            case "settings":
            case "faq":
                bottomNavigationView.setSelectedItemId(R.id.home);
                editor.putString("currentScreen", "home");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                break;
            case "switchUser":
                bottomNavigationView.setSelectedItemId(R.id.profile);
                editor.putString("currentScreen", "profile");
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                break;
            case "home":
            default:
                super.onBackPressed();
                break;
        }
    }
}
package com.example.le_androidapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        sp = getSharedPreferences("sharedData", Context.MODE_PRIVATE);
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

        sp = getSharedPreferences("sharedData", Context.MODE_PRIVATE);
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
    protected void onResume() {
        super.onResume();

        if(bluetoothAdapter == null) {
            Toast.makeText(this, "The device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } //else Toast.makeText(this, "Bluetooth is supported", Toast.LENGTH_SHORT).show();

        if(bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            // enableBtIntent requires permission that may be rejected by user
            // currently suppressing MissingPermission with annotation
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onBackPressed() {

        SharedPreferences sp = getSharedPreferences("sharedData", Context.MODE_PRIVATE);
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
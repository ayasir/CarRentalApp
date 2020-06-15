package com.kashsoftwares.dashboardsplashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
    }
}

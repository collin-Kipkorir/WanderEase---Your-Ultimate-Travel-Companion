package com.wanderease.travelcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if user is already logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is logged in, redirect to MainActivity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // User is not logged in, redirect to LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
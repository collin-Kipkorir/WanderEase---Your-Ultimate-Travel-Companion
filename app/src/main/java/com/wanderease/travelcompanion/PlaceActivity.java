package com.wanderease.travelcompanion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PlaceActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView, rateTextView;
    private StarRatingView starRatingView;
    private TextView agentNameTextView, placeDescriptionTextView;

    private String agentNumber; // Added to store agent number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        // Initialize views
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.nameTextView);
        rateTextView = findViewById(R.id.rateTextView);
        starRatingView = findViewById(R.id.starRatingView);
        agentNameTextView = findViewById(R.id.agentNameTextView);
        ImageView agentNumberTextView = findViewById(R.id.agentNumberTextView);
        placeDescriptionTextView = findViewById(R.id.placeDescriptionTextView);

        // Retrieve the passed ImageView bitmap
        Bitmap bitmap = getIntent().getParcelableExtra("imageViewBitmap");

        // Set the bitmap to the ImageView
        imageView.setImageBitmap(bitmap);

        // Retrieve the textViewName and rateTextView strings
        String textViewName = getIntent().getStringExtra("textViewName");
        String rateString = getIntent().getStringExtra("rateTextView");

        // Set the text to the TextViews
        textView.setText(textViewName);
        rateTextView.setText(rateString);

        // Convert rateString to float
        float rating = 0;
        if (!rateString.isEmpty()) {
            rating = Float.parseFloat(rateString);
        }

        // Set the rating to the starRatingView
        starRatingView.setRating(rating);

        // Retrieve agent details from intent extras
        String agentName = getIntent().getStringExtra("agentName");
        agentNumber = getIntent().getStringExtra("agentNumber"); // Store agent number
        String placeDescription = getIntent().getStringExtra("placeDescription");

        // Set agent details to TextViews
        agentNameTextView.setText(agentName);
        placeDescriptionTextView.setText(placeDescription);

        // Set OnClickListener for the TextView to initiate call
        agentNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for call permission
                if (ContextCompat.checkSelfPermission(PlaceActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(PlaceActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            1001);
                } else {
                    // Permission is granted, initiate the call
                    initiateCall();
                }
            }
        });
    }

    // Method to initiate the call
    private void initiateCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(android.net.Uri.parse("tel:" + agentNumber));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to initiate call", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, initiate the call
            initiateCall();
        } else {
            // Permission denied, inform the user
            Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}

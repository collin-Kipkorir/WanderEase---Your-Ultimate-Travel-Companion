package com.wanderease.travelcompanion;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends AppCompatActivity {
    private static final String TRIPS_PREFS = "TripsPrefs";
    private static final String TRIPS_KEY = "TripsList";

    private RecyclerView tripRecyclerView;
    private List<Trip> tripList;
    private TripAdapter tripAdapter;
    private SharedPreferences sharedPreferences;
    private EditText tripNameEditText, destinationEditText, startDateEditText, endDateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        tripRecyclerView = findViewById(R.id.tripRecyclerView);
        tripList = new ArrayList<>();
        tripAdapter = new TripAdapter(tripList);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripRecyclerView.setAdapter(tripAdapter);

        sharedPreferences = getSharedPreferences(TRIPS_PREFS, Context.MODE_PRIVATE);

        loadTripsFromSharedPreferences();

        FloatingActionButton addTripButton = findViewById(R.id.addTripButton);
        addTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTripDialog();
            }
        });
    }

    private void showAddTripDialog() {
        // Inflate custom layout for dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_trip, null);

        // Initialize views
        tripNameEditText = dialogView.findViewById(R.id.tripNameEditText);
        destinationEditText = dialogView.findViewById(R.id.destinationEditText);
        startDateEditText = dialogView.findViewById(R.id.startDateEditText);
        endDateEditText = dialogView.findViewById(R.id.endDateEditText);
        Button addButton = dialogView.findViewById(R.id.addButton);

        // Create custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        // Handle Add button click
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tripName = tripNameEditText.getText().toString().trim();
                String destination = destinationEditText.getText().toString().trim();
                String startDate = startDateEditText.getText().toString().trim();
                String endDate = endDateEditText.getText().toString().trim();

                if (!tripName.isEmpty() && !destination.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()) {
                    Trip trip = new Trip(tripName, destination, startDate, endDate);
                    tripList.add(trip);
                    saveTripsToSharedPreferences();
                    tripAdapter.notifyDataSetChanged();
                    Toast.makeText(TripsActivity.this, "Trip added successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(TripsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Show dialog
        dialog.show();
    }

    private void loadTripsFromSharedPreferences() {
        String json = sharedPreferences.getString(TRIPS_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Trip>>() {
            }.getType();
            tripList = gson.fromJson(json, type);
            if (tripList != null) {
                tripAdapter.setTrips(tripList);
                tripAdapter.notifyDataSetChanged();
            }
        }
    }

    private void saveTripsToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tripList);
        editor.putString(TRIPS_KEY, json);
        editor.apply();
    }

}

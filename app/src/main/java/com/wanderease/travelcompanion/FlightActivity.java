package com.wanderease.travelcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FlightActivity extends AppCompatActivity {

    private EditText editDeparture, editArrival, editDate, editPassengers;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        // Initialize UI elements
        editDeparture = findViewById(R.id.editDeparture);
        editArrival = findViewById(R.id.editArrival);
        editDate = findViewById(R.id.editDate);
        editPassengers = findViewById(R.id.editPassengers);
        buttonSearch = findViewById(R.id.buttonSearch);

        // Set click listener for search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFlights();
            }
        });
    }

    private void searchFlights() {
        // Dummy implementation for demonstration
        String departure = editDeparture.getText().toString();
        String arrival = editArrival.getText().toString();
        String date = editDate.getText().toString();
        String passengers = editPassengers.getText().toString();

        // Validate inputs
        if (departure.isEmpty() || arrival.isEmpty() || date.isEmpty() || passengers.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform flight search (API integration would be done here)
        // For demonstration, just display a toast with search criteria
        String message = "Searching flights...\nDeparture: " + departure + "\nArrival: " + arrival
                + "\nDate: " + date + "\nPassengers: " + passengers;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

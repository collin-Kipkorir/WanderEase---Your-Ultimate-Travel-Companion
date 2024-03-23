package com.wanderease.travelcompanion;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomsActivity extends AppCompatActivity {

    private RecyclerView hotelRecyclerView;
    private HotelsAdapter hotelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.bgr));
        }

        // Get a reference to the Firebase database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("places");

        // Initialize a list to hold all hotel data
        List<HashMap<String, String>> hotelDataList = new ArrayList<>();

        // Attach a listener to read the data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the previous list
                hotelDataList.clear();

                // Iterate through all places
                for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                    // Get the child node containing hotels for each place
                    DataSnapshot hotelsSnapshot = placeSnapshot.child("hotels");

                    // Iterate through the hotels for each place
                    for (DataSnapshot hotelSnapshot : hotelsSnapshot.getChildren()) {
                        HashMap<String, String> hotelData = new HashMap<>();
                        hotelData.put("hotelId", hotelSnapshot.getKey());
                        hotelData.put("hotelName", hotelSnapshot.child("hotelName").getValue(String.class));
                        hotelData.put("hotelCost", hotelSnapshot.child("hotelCost").getValue(String.class));
                        hotelData.put("hotelRating", hotelSnapshot.child("hotelRating").getValue(String.class));
                        hotelData.put("hotelImage", hotelSnapshot.child("hotelImage").getValue(String.class)); // Fetch image URL
                        // Add other fields as needed
                        hotelDataList.add(hotelData);
                    }
                }

                // Update the RecyclerView with the new data
                updateRecyclerView(hotelDataList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
                Log.e("Firebase Error", databaseError.getMessage());
            }
        });
    }

    // Method to update the RecyclerView
    private void updateRecyclerView(List<HashMap<String, String>> hotelDataList) {
        // Initialize RecyclerView and HotelAdapter
        hotelRecyclerView = findViewById(R.id.hotelRecyclerView);
        hotelAdapter = new HotelsAdapter(this, hotelDataList);

        // Set GridLayoutManager with 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        hotelRecyclerView.setLayoutManager(layoutManager);

        hotelRecyclerView.setAdapter(hotelAdapter);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

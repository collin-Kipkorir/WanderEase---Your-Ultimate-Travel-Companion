package com.wanderease.travelcompanion;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HotelActivity extends AppCompatActivity implements OnMapReadyCallback {
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        // Retrieve data from intent
        String hotelName = getIntent().getStringExtra("hotelName");
        String hotelCost = getIntent().getStringExtra("hotelCost");
        String hotelRating = getIntent().getStringExtra("hotelRating");
        String hotelImage = getIntent().getStringExtra("hotelImage");
        // Retrieve hotel ID from intent
        String hotelId = getIntent().getStringExtra("hotelId");


        // Display item details
        TextView hotelNameTextView = findViewById(R.id.hotelNameTextView);
        TextView hotelCostTextView = findViewById(R.id.hotelCostTextView);
        TextView hotelRatingTextView = findViewById(R.id.hotelRatingTextView);
        StarRatingView starRatingView = findViewById(R.id.starRatingView);
        ImageView hotelImageView = findViewById(R.id.hotelImageView);
        TextView hotelAddressTextView = findViewById(R.id.hotelAddressTextView);

        hotelNameTextView.setText(hotelName);
        hotelCostTextView.setText(hotelCost);
        hotelRatingTextView.setText(hotelRating);
        Glide.with(this).load(hotelImage).into(hotelImageView);
//        hotelAddressTextView.setText(hotelId);

        // Convert the hotelRatingTextView value to a float
        float rating = Float.parseFloat(hotelRatingTextView.getText().toString());
        // Set the rating to the starRatingView
        starRatingView.setRating(rating);

        // Get a reference to the Firebase database
        DatabaseReference placesRef = FirebaseDatabase.getInstance().getReference().child("places");

        // Attach a listener to read the place data
        placesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Iterate through each place
                    for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                        // Check if the place contains the hotel ID
                        if (placeSnapshot.child("hotels").hasChild(hotelId)) {
                            // Retrieve latitude and longitude from the hotel node
                            DataSnapshot hotelSnapshot = placeSnapshot.child("hotels").child(hotelId);
                            if (hotelSnapshot.exists() && hotelSnapshot.hasChild("latitude") && hotelSnapshot.hasChild("longitude")) {
                                latitude = hotelSnapshot.child("latitude").getValue(Double.class);
                                longitude = hotelSnapshot.child("longitude").getValue(Double.class);

                                // Load the map fragment
                                SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.add(R.id.mapContainer, mapFragment).commit();
                                mapFragment.getMapAsync(HotelActivity.this);
                            }
                            // Exit the loop once the hotel is found
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add marker at the specified latitude and longitude
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location).title("Hotel Location"));

        // Zoom level for the map (adjust as needed)
        float zoomLevel = 17f;

        // Move camera to the specified location with custom zoom level
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }

}

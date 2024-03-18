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
        // Retrieve latitude and longitude from intent
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);


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
        hotelAddressTextView.setText("Hotel ID: " + hotelId);

        // Convert the hotelRatingTextView value to a float
        float rating = Float.parseFloat(hotelRatingTextView.getText().toString());
        // Set the rating to the starRatingView
        starRatingView.setRating(rating);

        // Load the map fragment
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mapContainer, mapFragment).commit();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Enable zoom controls
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                // Create LatLng object for hotel location
                LatLng hotelLocation = new LatLng(latitude, longitude);

                // Add marker for hotel location
                googleMap.addMarker(new MarkerOptions().position(hotelLocation).title("Hotel Location"));

                // Move camera to hotel location and zoom in
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelLocation, 15));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add marker at the specified latitude and longitude
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location).title("Hotel Location"));

        // Move camera to the specified location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
    }
}

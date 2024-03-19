package com.wanderease.travelcompanion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
    // Declare hotel details variables
    private String hotelName;
    private String hotelCost;
    private String hotelRating;
    private String hotelImage;
    private Button bookButton;

    @SuppressLint("MissingInflatedId")
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
        hotelName = getIntent().getStringExtra("hotelName");
        hotelCost = getIntent().getStringExtra("hotelCost");
        hotelRating = getIntent().getStringExtra("hotelRating");
        hotelImage = getIntent().getStringExtra("hotelImage");
        String hotelId = getIntent().getStringExtra("hotelId");

        // Initialize UI components
        TextView hotelNameTextView = findViewById(R.id.hotelNameTextView);
        TextView hotelCostTextView = findViewById(R.id.hotelCostTextView);
        TextView hotelRatingTextView = findViewById(R.id.hotelRatingTextView);
        ImageView hotelImageView = findViewById(R.id.hotelImageView);

        bookButton = findViewById(R.id.bookButton);

        // Set data to UI components
        hotelNameTextView.setText(hotelName);
        hotelCostTextView.setText(hotelCost);
        hotelRatingTextView.setText(hotelRating);
        Glide.with(this).load(hotelImage).into(hotelImageView);

        // Load hotel details and amenities
        loadHotelDetailsFromFirebase(hotelId);

        // Set click listener for bookButton
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingPopup();
            }
        });
    }

    private void showBookingPopup() {
        // Inflate the custom popup layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_booking, null);


        // Initialize UI components in the popup
        EditText daysEditText = popupView.findViewById(R.id.daysEditText);
        EditText nightsEditText = popupView.findViewById(R.id.nightsEditText);
        EditText checkInDateEditText = popupView.findViewById(R.id.checkInDateEditText);
        Button confirmButton = popupView.findViewById(R.id.confirmButton);

        // Create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Show the popup window
        popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);

        // Set click listener for confirmButton
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve booking details from the popup
                String daysText = daysEditText.getText().toString().trim();
                String nightsText = nightsEditText.getText().toString().trim();
                String checkInDate = checkInDateEditText.getText().toString().trim();

                // Check if any of the fields are empty
                if (daysText.isEmpty() || nightsText.isEmpty() || checkInDate.isEmpty()) {
                    // Display a toast or error message indicating that all fields are required
                    Toast.makeText(HotelActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method without proceeding further
                }

                // Convert text to integers
                int days = Integer.parseInt(daysText);
                int nights = Integer.parseInt(nightsText);

                // Save booking details to SharedPreferences
                saveBookingDetails(hotelImage, hotelName, hotelCost, hotelRating, days, nights, checkInDate);

                // Dismiss the popup window
                popupWindow.dismiss();
            }
        });


    }

    private void saveBookingDetails(String hotelImage, String hotelName, String hotelCost, String hotelRating, int days, int nights, String checkInDate) {
        SharedPreferences sharedPreferences = getSharedPreferences("BookingDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Increment the booking count
        int bookingCount = sharedPreferences.getInt("bookingCount", 0);
        editor.putInt("bookingCount", bookingCount + 1);

        // Save booking details with a unique key
        String bookingKey = "booking_" + bookingCount;
        editor.putString(bookingKey + "_hotelName", hotelName);
        editor.putString(bookingKey + "_hotelCost", hotelCost);
        editor.putString(bookingKey + "_hotelRating", hotelRating);
        editor.putString(bookingKey + "_hotelImage", hotelImage);
        editor.putInt(bookingKey + "_days", days);
        editor.putInt(bookingKey + "_nights", nights);
        editor.putString(bookingKey + "_checkInDate", checkInDate);

        editor.apply();
    }

    private void loadHotelDetailsFromFirebase(String hotelId) {
        DatabaseReference placesRef = FirebaseDatabase.getInstance().getReference().child("places");
        placesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                        if (placeSnapshot.child("hotels").hasChild(hotelId)) {
                            DataSnapshot hotelSnapshot = placeSnapshot.child("hotels").child(hotelId);
                            if (hotelSnapshot.exists() && hotelSnapshot.hasChild("latitude") && hotelSnapshot.hasChild("longitude")) {
                                latitude = hotelSnapshot.child("latitude").getValue(Double.class);
                                longitude = hotelSnapshot.child("longitude").getValue(Double.class);
                                String hotelCategory = hotelSnapshot.child("hotelCategory").getValue(String.class);
                                String hotelAbout = hotelSnapshot.child("hotelAbout").getValue(String.class);
                                TextView hotelAboutTextView = findViewById(R.id.hotelAboutTextView);
                                TextView hotelCategoryTextView = findViewById(R.id.hotelCategoryTextView);
                                hotelAboutTextView.setText(hotelAbout);
                                hotelCategoryTextView.setText(hotelCategory);
                                populateAmenities(hotelSnapshot.child("amenities"));
                                loadMapFragment();
                            }
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

    private void populateAmenities(DataSnapshot amenitiesSnapshot) {
        if (amenitiesSnapshot.exists()) {
            LinearLayout amenitiesLayout = findViewById(R.id.amenitiesLayout);
            amenitiesLayout.removeAllViews(); // Clear previous amenities (if any)

            for (DataSnapshot amenitySnapshot : amenitiesSnapshot.getChildren()) {
                String amenityTitle = amenitySnapshot.getKey();
                boolean hasAmenity = amenitySnapshot.getValue(Boolean.class);

                // Create a new TextView for each amenity
                TextView amenityTextView = new TextView(HotelActivity.this);
                amenityTextView.setText(amenityTitle);
                amenityTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow, 0, 0, 0);
                amenityTextView.setCompoundDrawablePadding(8);
                amenitiesLayout.addView(amenityTextView);
            }
        }
    }

    private void loadMapFragment() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mapContainer, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location).title("Hotel Location"));
        float zoomLevel = 17f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }
}

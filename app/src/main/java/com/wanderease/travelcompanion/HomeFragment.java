package com.wanderease.travelcompanion;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView tvUserLocation;
    private RecyclerView recyclerView;
    private HotelAdapter mAdapter;
    private final List<Hotel> hotelList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private static final int REQUEST_LOCATION_PERMISSION = 1001;
    private TextView tvWelcomeMessage;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Get the activity's Window
            Window window = requireActivity().getWindow();
            // Set the status bar color
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.bgr));
        }
        tvUserLocation = view.findViewById(R.id.tv_user_location);
        tvWelcomeMessage = view.findViewById(R.id.tv_welcome_message);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in, set the welcome message
            String userName = user.getDisplayName();
            if (userName != null && !userName.isEmpty()) {
                tvWelcomeMessage.setText("Welcome back, " + userName + "!");
            } else {
                // User's name is not available, set a default welcome message
                tvWelcomeMessage.setText("Welcome back!");
            }
            Log.d("HomeFragment", "User display name: " + userName);
        } else {
            // User is not signed in, set a default welcome message
            tvWelcomeMessage.setText("Welcome!");
            Log.d("HomeFragment", "User is not signed in");
        }

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        mAdapter = new HotelAdapter(requireContext(), hotelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("places");

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, proceed to get user location
            getUserLocation();
        } else {
            // Request location permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        // Fetch hotel data from Firebase Realtime Database
        fetchHotelData();

        // Set click listener for hotel items
        mAdapter.setOnItemClickListener(new HotelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Hotel hotel, ImageView imageView, TextView textViewName, TextView rateTextView, StarRatingView starRatingView, LatLng latLng) {
                Intent intent = new Intent(getActivity(), PlaceActivity.class);
                imageView.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                imageView.setDrawingCacheEnabled(false);

                // Pass hotel details
                intent.putExtra("imageViewBitmap", bitmap);
                intent.putExtra("textViewName", textViewName.getText().toString());
                intent.putExtra("rateTextView", rateTextView.getText().toString());

                // Pass agent details
                intent.putExtra("agentName", hotel.getAgentName());
                intent.putExtra("agentNumber", hotel.getAgentNumber());
                intent.putExtra("placeDescription", hotel.getPlaceDescription());

                // Pass latitude and longitude
                intent.putExtra("latitude", latLng.latitude);
                intent.putExtra("longitude", latLng.longitude);

                // Pass the placeId
                intent.putExtra("placeId", hotel.getPlaceId());

                startActivity(intent);
            }
        });


        return view;
    }

    private void fetchHotelData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hotelList.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Hotel hotel = snapshot.getValue(Hotel.class);
                    // Retrieve placeId from the snapshot key
                    String placeId = snapshot.getKey();
                    // Set the placeId to the hotel object
                    hotel.setPlaceId(placeId);
                    hotelList.add(hotel);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


    private void getUserLocation() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            // Location service is not available
            tvUserLocation.setText("Location service not available");
            return;
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Location permission not granted
            tvUserLocation.setText("Location permission not granted");
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();

            // Use Geocoder to get the address from latitude and longitude asynchronously
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (!addresses.isEmpty()) {
                    String addressLine = addresses.get(0).getAddressLine(0); // Get the first address line
                    // Display the address
                    tvUserLocation.setText(addressLine);
                } else {
                    tvUserLocation.setText("Location not found");
                }
            } catch (IOException e) {
                e.printStackTrace();
                tvUserLocation.setText("Error retrieving location");
            }
        } else {
            tvUserLocation.setText("Location not available");
        }
    }


    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, proceed to get user location
            getUserLocation();
        } else {
            // Permission denied, handle accordingly
            tvUserLocation.setText("Location permission denied");
        }
    }
}
package com.wanderease.travelcompanion;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView tvUserLocation;
    private RecyclerView recyclerView;
    private HotelAdapter mAdapter;
    private List<Hotel> hotelList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvUserLocation = view.findViewById(R.id.tv_user_location);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        mAdapter = new HotelAdapter(requireContext(), hotelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("hotels");

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
            public void onItemClick(Hotel hotel) {
                // Serialize the selected hotel to JSON string
                Gson gson = new Gson();
                String selectedHotelJson = gson.toJson(hotel);

                // Create a new Bundle and put the selected hotel JSON string into it
                Bundle bundle = new Bundle();
                bundle.putString("selectedHotel", selectedHotelJson);

                // Create HotelDetailsFragment instance and set arguments
                HotelDetailsFragment hotelDetailsFragment = new HotelDetailsFragment();
                hotelDetailsFragment.setArguments(bundle);

                // Replace the current fragment with HotelDetailsFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, hotelDetailsFragment)
                        .addToBackStack(null)
                        .commit();
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();

            // Use Geocoder to get the address from latitude and longitude
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
            tvUserLocation.setText("Nairobi");
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
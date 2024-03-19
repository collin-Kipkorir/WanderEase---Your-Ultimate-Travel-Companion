package com.wanderease.travelcompanion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    private List<Booking> bookingList;
    private BookingAdapter bookingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Get the activity's Window
            Window window = requireActivity().getWindow();
            // Set the status bar color
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.bgr));
        }
        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(getContext(), bookingList); // Pass context to adapter
        recyclerView.setAdapter(bookingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Retrieve booking details from SharedPreferences
        retrieveBookingDetails();

        return view;
    }

    private void retrieveBookingDetails() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BookingDetails", Context.MODE_PRIVATE);

        // Retrieve the total number of bookings
        int bookingCount = sharedPreferences.getInt("bookingCount", 0);

        // Loop through each booking and retrieve its details
        for (int i = bookingCount - 1; i >= 0; i--) { // Start from the last booking to add the most recent one first
            String bookingKey = "booking_" + i;
            String hotelName = sharedPreferences.getString(bookingKey + "_hotelName", "");
            String hotelCost = sharedPreferences.getString(bookingKey + "_hotelCost", "");
            String hotelRating = sharedPreferences.getString(bookingKey + "_hotelRating", "");
            String hotelImage = sharedPreferences.getString(bookingKey + "_hotelImage", "");
            int days = sharedPreferences.getInt(bookingKey + "_days", 0);
            int nights = sharedPreferences.getInt(bookingKey + "_nights", 0);
            String checkInDate = sharedPreferences.getString(bookingKey + "_checkInDate", "");

            // Create Booking object and add it to the beginning of the list
            Booking booking = new Booking(hotelName, hotelCost, hotelRating, hotelImage, days, nights, checkInDate);
            bookingList.add(0, booking);
        }

        // Notify adapter of data change
        bookingAdapter.notifyDataSetChanged();
    }

}

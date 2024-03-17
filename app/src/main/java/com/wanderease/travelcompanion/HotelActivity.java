package com.wanderease.travelcompanion;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class HotelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        // Retrieve data from intent
        String hotelName = getIntent().getStringExtra("hotelName");
        String hotelCost = getIntent().getStringExtra("hotelCost");
        String hotelRating = getIntent().getStringExtra("hotelRating");
        String hotelImage = getIntent().getStringExtra("hotelImage");

        // Display item details
        TextView hotelNameTextView = findViewById(R.id.hotelNameTextView);
        TextView hotelCostTextView = findViewById(R.id.hotelCostTextView);
        TextView hotelRatingTextView = findViewById(R.id.hotelRatingTextView);
        ImageView hotelImageView = findViewById(R.id.hotelImageView);

        hotelNameTextView.setText(hotelName);
        hotelCostTextView.setText(hotelCost);
        hotelRatingTextView.setText(hotelRating);
        Glide.with(this).load(hotelImage).into(hotelImageView);
    }
}

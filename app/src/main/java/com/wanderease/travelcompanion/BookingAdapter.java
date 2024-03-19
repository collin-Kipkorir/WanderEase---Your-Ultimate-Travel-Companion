package com.wanderease.travelcompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final Context context;
    private final List<Booking> bookingList;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item_layout, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Bind booking data to views
        holder.hotelNameTextView.setText(booking.getHotelName());
        holder.hotelCostTextView.setText(booking.getHotelCost());
        holder.hotelRatingTextView.setText(booking.getHotelRating());
        holder.daysTextView.setText(String.valueOf(booking.getDays()));
        holder.nightsTextView.setText(String.valueOf(booking.getNights()));
        holder.checkInDateTextView.setText(booking.getCheckInDate());

        // Load hotel image using Glide
        Glide.with(context).load(booking.getHotelImage()).into(holder.hotelImageView);

        // Set the rating for StarRatingView
        float rating = Float.parseFloat(booking.getHotelRating());
        holder.starRatingView.setRating(rating);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView hotelNameTextView, hotelCostTextView, hotelRatingTextView, daysTextView, nightsTextView, checkInDateTextView;
        ImageView hotelImageView;
        StarRatingView starRatingView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            hotelCostTextView = itemView.findViewById(R.id.hotelCostTextView);
            hotelRatingTextView = itemView.findViewById(R.id.hotelRatingTextView);
            daysTextView = itemView.findViewById(R.id.daysTextView);
            nightsTextView = itemView.findViewById(R.id.nightsTextView);
            checkInDateTextView = itemView.findViewById(R.id.checkInDateTextView);
            starRatingView = itemView.findViewById(R.id.starRatingview);
        }
    }
}


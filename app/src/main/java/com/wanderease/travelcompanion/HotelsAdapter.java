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

import java.util.HashMap;
import java.util.List;

public class HotelsAdapter extends RecyclerView.Adapter<HotelsAdapter.HotelViewHolder> {

    private final Context context;
    private final List<HashMap<String, String>> hotelsList;

    public HotelsAdapter(Context context, List<HashMap<String, String>> hotelsList) {
        this.context = context;
        this.hotelsList = hotelsList;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotels_item, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        HashMap<String, String> hotelData = hotelsList.get(position);

        // Bind data to views
        holder.hotelNameTextView.setText(hotelData.get("hotelName"));
        holder.hotelCostTextView.setText(hotelData.get("hotelCost"));
        holder.hotelRatingTextView.setText(hotelData.get("hotelRating"));


        // Set star rating
        String ratingString = hotelData.get("hotelRating");
        float rating = Float.parseFloat(ratingString);
        holder.hotelRatingView.setRating(rating);

        // Load image using Glide
        String imageUrl = hotelData.get("hotelImage");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.image) // Placeholder image while loading (optional)
                    .into(holder.hotelImageView);
        } else {
            // If imageUrl is null or empty, you may want to set a default image or handle it as needed
            holder.hotelImageView.setImageResource(R.drawable.image);
        }
    }


    @Override
    public int getItemCount() {
        return hotelsList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView hotelNameTextView, hotelCostTextView, hotelRatingTextView;
        ImageView hotelImageView;
        StarRatingView hotelRatingView; // Add this line

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            hotelCostTextView = itemView.findViewById(R.id.hotelCostTextView);
            hotelRatingTextView = itemView.findViewById(R.id.hotelRateTextView);
            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            hotelRatingView = itemView.findViewById(R.id.hotelRatingView); // Initialize the hotelRatingView
        }
    }

}

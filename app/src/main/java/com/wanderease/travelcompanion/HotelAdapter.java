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
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    private final Context context;
    private final List<Hotel> hotelList;
    private OnItemClickListener mListener;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.bind(hotel);

        // Load hotel image using Glide (or any other image loading library)
        Glide.with(context)
                .load(hotel.getImageUrl())
                .placeholder(R.drawable.image)
                .into(holder.imageViewPlace);
        // Set the rating for StarRatingView
        float rating = Float.parseFloat(hotel.getRating());
        holder.starRatingView.setRating(rating);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public HotelAdapter(Context context, List<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_cardview, parent, false);
        return new ViewHolder(view, mListener);
    }

    public interface OnItemClickListener {
        void onItemClick(Hotel hotel, ImageView imageView, TextView textViewName, TextView rateTextView, StarRatingView starRatingView, LatLng latLng);
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView rateTextView;
        private final TextView textViewDistance;
        private final ImageView imageViewPlace;
        private final StarRatingView starRatingView; // Add this line

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.titleTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            textViewDistance = itemView.findViewById(R.id.distance);
            imageViewPlace = itemView.findViewById(R.id.imageView);
            starRatingView = itemView.findViewById(R.id.starRatingView); // Initialize the StarRatingView

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Hotel hotel = hotelList.get(position);
                            LatLng latLng = new LatLng(hotel.getLatitude(), hotel.getLongitude());
                            listener.onItemClick(hotel, imageViewPlace, textViewName, rateTextView, starRatingView, latLng);
                        }
                    }
                }
            });
        }

        public void bind(Hotel hotel) {
            textViewName.setText(hotel.getName());
            rateTextView.setText(hotel.getRating());
            textViewDistance.setText(hotel.getDistance());
        }
    }
}

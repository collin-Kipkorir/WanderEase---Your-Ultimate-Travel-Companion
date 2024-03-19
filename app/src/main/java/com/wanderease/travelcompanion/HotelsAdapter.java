package com.wanderease.travelcompanion;

import android.content.Context;
import android.content.Intent;
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
        holder.bind(hotelData);

    }

    @Override
    public int getItemCount() {
        return hotelsList.size();
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hotelNameTextView, hotelCostTextView, hotelRatingTextView;
        ImageView hotelImageView;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            hotelCostTextView = itemView.findViewById(R.id.hotelCostTextView);
            hotelRatingTextView = itemView.findViewById(R.id.hotelRateTextView);
            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            itemView.setOnClickListener(this);
        }

        public void bind(HashMap<String, String> hotelData) {
            hotelNameTextView.setText(hotelData.get("hotelName"));
            hotelCostTextView.setText(hotelData.get("hotelCost"));
            hotelRatingTextView.setText(hotelData.get("hotelRating"));
            Glide.with(context).load(hotelData.get("hotelImage")).into(hotelImageView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                HashMap<String, String> hotelData = hotelsList.get(position);
                Intent intent = new Intent(context, HotelActivity.class);
                intent.putExtra("hotelName", hotelData.get("hotelName"));
                intent.putExtra("hotelCost", hotelData.get("hotelCost"));
                intent.putExtra("hotelRating", hotelData.get("hotelRating"));
                intent.putExtra("hotelImage", hotelData.get("hotelImage"));

                //Correctly use "hotelId" instead of "HotelId"
                intent.putExtra("hotelId", hotelData.get("hotelId")); // Change here
                intent.putExtra("latitude", hotelData.get("latitude"));
                intent.putExtra("longitude", hotelData.get("longitude"));
                context.startActivity(intent);
            }
        }

    }
}


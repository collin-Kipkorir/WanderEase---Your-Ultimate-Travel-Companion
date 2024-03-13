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
import com.bumptech.glide.request.RequestOptions;
import com.wanderease.travelcompanion.R;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    private Context context;
    private List<Hotel> hotelList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Hotel hotel);
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.bind(hotel);

        // Load hotel image using Glide (or any other image loading library)
        Glide.with(context)
                .load(hotel.getImageUrl())
                .placeholder(R.drawable.image)
                .into(holder.imageViewHotel);
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName,textViewCost,rateTextView, textViewDistance;
        private ImageView imageViewHotel;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.titleTextView);
            textViewCost = itemView.findViewById(R.id.costTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            textViewDistance = itemView.findViewById(R.id.distance);
            imageViewHotel = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(hotelList.get(position));
                        }
                    }
                }
            });
        }

        public void bind(Hotel hotel) {
            textViewName.setText(hotel.getName());
            textViewCost.setText(hotel.getCost());
            rateTextView.setText("Ratings: " + hotel.getRating());
            textViewDistance.setText(hotel.getDistance());

        }
    }
}

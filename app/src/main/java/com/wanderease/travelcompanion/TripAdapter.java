package com.wanderease.travelcompanion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<Trip> tripList;

    public TripAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.tripNameTextView.setText(trip.getTripName());
        holder.destinationTextView.setText(trip.getDestination());
        holder.dateTextView.setText(trip.getStartDate() + " - " + trip.getEndDate());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void setTrips(List<Trip> tripList) {
        this.tripList = tripList;
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tripNameTextView, destinationTextView, dateTextView;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTextView = itemView.findViewById(R.id.tripNameTextView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}

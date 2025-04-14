package com.example.vango_mobile;
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
        holder.txtRoute.setText(trip.getOrigin() + " → " + trip.getDestination());
        holder.txtArrivalTime.setText("Arrival: " + trip.getArrivalTime());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView txtRoute, txtArrivalTime;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRoute = itemView.findViewById(R.id.txtRoute);
            txtArrivalTime = itemView.findViewById(R.id.txtArrivalTime);
        }
    }
}

package com.example.vango_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DriverTripAdapter extends RecyclerView.Adapter<DriverTripAdapter.TripViewHolder> {

    private List<DriverTrip> tripList;
    private Context context;

    public DriverTripAdapter(Context context, List<DriverTrip> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_driver_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        DriverTrip trip = tripList.get(position);
        holder.txtRouteLabels.setText(trip.getDepartureLabel() + " → " + trip.getArrivalLabel());
        holder.txtDepartureTime.setText("Horário de Saída: " + trip.getDepartureTime());

        // Carrega imagem da van (caso seja necessário via recurso local)
        int imageResId = context.getResources().getIdentifier(
                trip.getPngRoute().replace(".png", ""), "drawable", context.getPackageName());

        if (imageResId != 0) {
            holder.imgVan.setImageResource(imageResId);
        } else {
            holder.imgVan.setImageResource(R.drawable.ic_van); // fallback
        }
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView txtRouteLabels, txtDepartureTime;
        ImageView imgVan;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRouteLabels = itemView.findViewById(R.id.txtRouteLabels);
            txtDepartureTime = itemView.findViewById(R.id.txtDepartureTime);
            imgVan = itemView.findViewById(R.id.imgVan);
        }
    }
}

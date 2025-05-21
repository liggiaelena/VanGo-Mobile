package com.example.vango_mobile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> tripList;
    private Class<?> destinationActivity;

    public TripAdapter(List<Trip> tripList, Class<?> destinationActivity) {
        this.tripList = tripList;
        this.destinationActivity = destinationActivity;
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

        holder.txtRoute.setText(trip.origin + " → " + trip.destination);
        holder.txtArrivalTime.setText("Chegada: " + trip.arrivalTime);

        // Se você tiver esses TextViews no layout, descomente e use:
        holder.txtSpots.setText("Vagas disponíveis: " + trip.spotsAvailable);
        holder.txtPrice.setText("Preço: R$ " + trip.price);
        holder.txtTripType.setText(trip.tripType.equals("one-way") ? "Só ida" : "Ida e volta");
        //holder.txtAddress.setText(trip.destinyTo.getFullAddress());

        // E se tiver ImageViews, pode usar:
        holder.imgVan.setImageResource(R.drawable.ic_van);
        holder.imgDriver.setImageResource(R.drawable.ic_person);
        // Glide.with(holder.itemView.getContext()).load("URL_DA_IMAGEM" + trip.photoVan).into(holder.vanImageView);
        // Glide.with(holder.itemView.getContext()).load("URL_DA_IMAGEM" + trip.photoDriver).into(holder.driverImageView);
        holder.itemView.setOnClickListener(v -> {
            if (destinationActivity != null) {
                Intent intent = new Intent(holder.itemView.getContext(), destinationActivity);
                // Aqui você pode passar dados, se quiser
                intent.putExtra("route", trip.origin + " → " + trip.destination);
                intent.putExtra("arrivalTime", trip.arrivalTime);
                intent.putExtra("spots", trip.spotsAvailable);
                intent.putExtra("price", trip.price);
                intent.putExtra("tripType", trip.tripType);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView txtRoute, txtArrivalTime, txtSpots, txtPrice, txtTripType, txtAddress;
        ImageView imgVan, imgDriver;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRoute = itemView.findViewById(R.id.txtRoute);
            txtArrivalTime = itemView.findViewById(R.id.txtArrivalTime);
            txtSpots = itemView.findViewById(R.id.txtSpots);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTripType = itemView.findViewById(R.id.txtTripType);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            imgVan = itemView.findViewById(R.id.imgVan);
            imgDriver = itemView.findViewById(R.id.imgDriver);

        }
    }
}

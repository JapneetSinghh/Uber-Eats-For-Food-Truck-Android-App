package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.AllTrucksGridModel;

import java.util.List;

public class AllTrucksGridAdapter extends RecyclerView.Adapter<AllTrucksGridAdapter.ViewHolder> {

    List<AllTrucksGridModel> list;
    Context context;

    public AllTrucksGridAdapter(Context context, List<AllTrucksGridModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AllTrucksGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_truck_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTrucksGridAdapter.ViewHolder holder, int position) {
        // Reset the image to avoid displaying recycled content
        holder.imageView.setImageDrawable(null);

        // Get the current truck model
        AllTrucksGridModel model = list.get(position);
        Bitmap truckImageBitmap = model.getTruckImageBitmap();

        // Set the truck image
        if (truckImageBitmap != null) {
            holder.imageView.setImageBitmap(truckImageBitmap);
            Log.d("Adapter", "Image set for truck: " + model.getAllTruckName());
        } else {
            holder.imageView.setImageResource(R.drawable.truck1); // Default placeholder image
            Log.e("Adapter", "Using placeholder image for truck: " + model.getAllTruckName());
        }

        // Set other truck details
        holder.truckName.setText(model.getAllTruckName());
//        holder.ratings.setText(model.getAllTruckRatings());
//        holder.starRating.setText(String.valueOf(model.getAllTruckNumRating()));
//        holder.starIcon.setImageResource(model.getAllTruckImageStar());
        holder.locationIcon.setImageResource(model.getAllTruckLocationIcon());
        holder.locationText.setText(model.getAllTruckLocationAddress());

        // Set click listener to open DetailActivity
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("truckName", model.getAllTruckName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView truckName;
        TextView ratings;
        TextView starRating;
        ImageView starIcon;

        // New views for location
        ImageView locationIcon;
        TextView locationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.allTruckImage);
            truckName = itemView.findViewById(R.id.allTruckTitle);

            // Initialize new location views
            locationIcon = itemView.findViewById(R.id.allTruckLocationIcon);
            locationText = itemView.findViewById(R.id.allTruckAddressText);
        }
    }
}

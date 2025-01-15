package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.SearchTruckModel;

import java.util.List;

public class SearchTruckAdapter extends RecyclerView.Adapter<SearchTruckAdapter.SearchTruckViewHolder> {

    private final Context context;
    private final List<SearchTruckModel> truckList;
    private final OnTruckClickListener listener;

    public SearchTruckAdapter(Context context, List<SearchTruckModel> truckList, OnTruckClickListener listener) {
        this.context = context;
        this.truckList = truckList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchTruckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_truck_viewholder, parent, false);
        return new SearchTruckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTruckViewHolder holder, int position) {
        SearchTruckModel truck = truckList.get(position);

        // Load truck image
//        Glide.with(context).load(truck.getImage()).into(holder.truckImage);

        Log.d("SearchTruckAdapter", "Truck address: " + truck.getAddress());
        // Set truck details
        holder.truckName.setText(truck.getName());
        holder.truckLocation.setText(truck.getAddress());

        holder.truckReviews.setText(truck.getReviewCount() + " Reviews");
        // Handle button click for viewing details
        holder.viewDetailsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTruckDetailsClick(truck);
            }

            // Open DetailsActivity and pass truckName as an extra
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("truckName",truck.getName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return truckList.size();
    }

    // ViewHolder class
    public static class SearchTruckViewHolder extends RecyclerView.ViewHolder {
        ImageView truckImage;
        TextView truckName, truckLocation, truckReviews, truckRating;
        Button viewDetailsButton;

        public SearchTruckViewHolder(@NonNull View itemView) {
            super(itemView);
            truckName = itemView.findViewById(R.id.searchTruckName);
            truckLocation = itemView.findViewById(R.id.searchTruckLocation);
            truckReviews = itemView.findViewById(R.id.searchTruckReviews);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }

    // Listener interface for truck item clicks
    public interface OnTruckClickListener {
        void onTruckDetailsClick(SearchTruckModel truckModel);
    }
}

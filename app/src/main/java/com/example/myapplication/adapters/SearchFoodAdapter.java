// FoodTruckAdapter.java
package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.SearchFoodModel;
import com.example.myapplication.models.SearchTruckModel;

import java.util.List;


public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.ViewHolder> {

    private final Context context;
    private final List<SearchFoodModel> foodTruckList;

    public SearchFoodAdapter(Context context, List<SearchFoodModel> foodTruckList) {
        this.context = context;
        this.foodTruckList = foodTruckList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (CardView)
        View view = LayoutInflater.from(context).inflate(R.layout.search_food_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (holder.truckNameTextView == null) {
            Log.e("ViewHolder", "searchTruckName is null");
        }
        if (holder.truckLocationTextView == null) {
            Log.e("ViewHolder", "searchTruckLocation is null");
        }
        if (holder.truckReviewsTextView == null) {
            Log.e("ViewHolder", "searchTruckReviews is null");
        }
        // Bind data to the view
        SearchFoodModel foodTruck = foodTruckList.get(position);
        holder.truckNameTextView.setText(foodTruck.getName());
        holder.truckLocationTextView.setText(foodTruck.getLocation());
        holder.truckReviewsTextView.setText(foodTruck.getReviewsCount() + " Reviews");

    //     Optionally handle button clicks
        holder.viewDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("truckName", foodTruck.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodTruckList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView truckNameTextView, truckLocationTextView, truckReviewsTextView;
        Button viewDetailsButton;

        public ViewHolder(View itemView) {
            super(itemView);
            truckNameTextView = itemView.findViewById(R.id.searchFoodTruckName);
            truckLocationTextView = itemView.findViewById(R.id.searchFoodTruckLocation);
            truckReviewsTextView = itemView.findViewById(R.id.searchFoodTruckReviews);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }
}

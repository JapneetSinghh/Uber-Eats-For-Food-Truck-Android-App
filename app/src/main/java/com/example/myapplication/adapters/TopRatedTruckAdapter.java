package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailActivity;
import com.example.myapplication.models.TopRatedTruckModel;
import com.example.myapplication.R;

import java.util.List;

public class TopRatedTruckAdapter extends RecyclerView.Adapter<TopRatedTruckAdapter.ViewHolder> {

    Context context;
    List<TopRatedTruckModel> list;

    public TopRatedTruckAdapter(Context context, List<TopRatedTruckModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_truck_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopRatedTruckModel model = list.get(position);

        if (model.getTopRatedImage() != null) {
            holder.imageView.setImageBitmap(model.getTopRatedImage());
        } else {
            holder.imageView.setImageResource(R.drawable.truck3);
        }

        holder.truckName.setText(model.getTopRatedTruckName());
        holder.address.setText(model.getTopRatedLocation());
//        holder.starRating.setText(String.valueOf(model.getTopRatedRating()));
//        holder.numRatings.setText("(" + model.getTopRatedNumRatings() + " reviews)"); // Display number of ratings
//        holder.starIcon.setImageResource(model.getTopRatedImageStar());

        // Pass truckName to DetailActivity
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("truckName", model.getTopRatedTruckName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView truckName;
        TextView starRating;
        TextView numRatings; // Added TextView for number of ratings
        TextView address;
        ImageView starIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.topRatedImg);
            truckName = itemView.findViewById(R.id.truckTitleText);
            address = itemView.findViewById(R.id.addressText);

        }
    }
}
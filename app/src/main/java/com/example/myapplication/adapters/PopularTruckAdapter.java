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
import com.example.myapplication.models.PopularTruckModel;
import com.example.myapplication.R;

import java.util.List;

public class PopularTruckAdapter extends RecyclerView.Adapter<PopularTruckAdapter.ViewHolder> {

    Context context;
    List<PopularTruckModel> list;

    public PopularTruckAdapter(Context context, List<PopularTruckModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_truck_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PopularTruckModel model = list.get(position);


        if (model.getPopularImage() != null) {
            holder.imageView.setImageBitmap(model.getPopularImage());
        } else {
            holder.imageView.setImageResource(R.drawable.truck1);
        }
        holder.truckName.setText(model.getPopularTruckName());
        holder.address.setText(model.getPopularLocation());
//        holder.starRating.setText(String.valueOf(model.getPopularRating()));
//        holder.numRatings.setText("(" + model.getPopularNumRatings() + " reviews)"); // Display number of ratings
//        holder.starIcon.setImageResource(model.getPopularImageStar());


        // Pass truckName to DetailActivity
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("truckName", model.getPopularTruckName());
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

            imageView = itemView.findViewById(R.id.popularTruckImg);
            truckName = itemView.findViewById(R.id.popularTruckTitle);
            address = itemView.findViewById(R.id.popularAddressText);
        }
    }
}

package com.example.myapplication.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.FoodHorModel;

import java.util.List;

public class FoodHorRecAdapter extends RecyclerView.Adapter<FoodHorRecAdapter.ViewHolder> {

    Context context;
    List<FoodHorModel> list;

    public FoodHorRecAdapter(Context context, List<FoodHorModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FoodHorRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fastfood_horizontal_item1, parent));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fastfood_horizontal_item1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHorRecAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.hor_img1);
            name = itemView.findViewById(R.id.hor_text1);
        }
    }
}

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

import com.example.myapplication.R;
import com.example.myapplication.SearchFoodActivity;
import com.example.myapplication.models.FoodVerModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class FoodVerRecAdapter extends RecyclerView.Adapter<FoodVerRecAdapter.ViewHolder> {

    Context context;
    List<FoodVerModel> list;

    public FoodVerRecAdapter(Context context, List<FoodVerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FoodVerRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fastfood_vertical_item1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodVerRecAdapter.ViewHolder holder, int position) {
        // Set data for the image and name
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());

        // Set OnClickListener for each itemView
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to open SearchFoodActivity
            Intent intent = new Intent(context, SearchFoodActivity.class);

            // Pass the food name to the activity
            intent.putExtra("food_name", list.get(position).getName());

            // Start SearchFoodActivity
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void showBottomSheet(FoodVerModel model) {
        // Create a BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        // Inflate the bottom sheet layout
        View bottomSheetView = LayoutInflater.from(context).inflate(
                R.layout.bottom_sheet_layout,
                null
        );

        // Customize the bottom sheet with data (if needed)
        TextView textView = bottomSheetView.findViewById(R.id.textView5);
        textView.setText("Details for: " + model.getName());

        // Set the view to the dialog and show it
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ver_text1);
            imageView = itemView.findViewById(R.id.vert_img1);
        }
    }
}

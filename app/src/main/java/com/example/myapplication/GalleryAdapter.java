package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// GalleryAdapter.java
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private final Context context;
    private final List<GalleryModel> galleryList;

    public GalleryAdapter(Context context, List<GalleryModel> galleryList) {
        this.context = context;
        this.galleryList = galleryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GalleryModel model = galleryList.get(position);
        holder.imageView.setImageResource(model.getImageResource());

        holder.imageView.setOnClickListener(v -> {
            showImagePopup(context, model.getImageResource());
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.galleryImageView);
        }
    }

    private void showImagePopup(Context context, int imageResource) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageResource);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        imageView.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(imageView);
        dialog.show();
    }
}

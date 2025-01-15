package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    private final List<OfferModel> offerList;
    private final Context context;

    public OfferAdapter(Context context, List<OfferModel> offerList) {
        this.context = context;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the offer card layout
        View view = LayoutInflater.from(context).inflate(R.layout.offer_card, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        OfferModel offer = offerList.get(position);

        // Bind data to the offer card views
        holder.tvOfferType.setText(offer.getTitle());
        holder.tvDiscount.setText(offer.getDescription());
        holder.tvValidity.setText(offer.getTerms());

        // Apply alternating background styles based on position
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.offer_background_white);
            holder.tvOfferType.setTextColor(context.getColor(R.color.primary_text));
            holder.tvDiscount.setTextColor(context.getColor(R.color.primary_text));
            holder.tvValidity.setTextColor(context.getColor(R.color.primary_text));
        } else {
            holder.itemView.setBackgroundResource(R.drawable.offer_background);
            holder.tvOfferType.setTextColor(context.getColor(R.color.white));
            holder.tvDiscount.setTextColor(context.getColor(R.color.white));
            holder.tvValidity.setTextColor(context.getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return offerList.size(); // Return the total number of offers
    }

    // ViewHolder class to hold the views for each offer card
    static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView tvOfferType, tvDiscount, tvValidity;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOfferType = itemView.findViewById(R.id.tvOfferType);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvValidity = itemView.findViewById(R.id.tvValidity);
        }
    }
}

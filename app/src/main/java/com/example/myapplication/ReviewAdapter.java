package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;
    private final Context context;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        // Set user name or default value
        holder.userNameText.setText(review.getUserName() != null ? review.getUserName() : "Anonymous");

        // Set review text or default value
        holder.reviewText.setText(review.getReviewText() != null ? review.getReviewText() : "No review text provided.");

        // Format and display the rating
        holder.ratingText.setText(String.format("%.1f ★", review.getRating()));

        // Customize the text color based on the rating value
        if (review.getRating() >= 4.0) {
            holder.ratingText.setTextColor(Color.parseColor("#4CAF50")); // Green for positive ratings
        } else if (review.getRating() >= 2.0) {
            holder.ratingText.setTextColor(Color.parseColor("#FFC107")); // Yellow for neutral ratings
        } else {
            holder.ratingText.setTextColor(Color.parseColor("#F44336")); // Red for negative ratings
        }

        // Set sentiment or default value
        holder.sentimentText.setText(review.getSentiment() != null ? review.getSentiment() : "Unknown sentiment");

        // Customize sentiment text color based on the sentiment
        switch (review.getSentiment().toLowerCase()) {
            case "positive":
                holder.sentimentText.setTextColor(Color.parseColor("#4CAF50")); // Green for positive
                break;
            case "neutral":
                holder.sentimentText.setTextColor(Color.parseColor("#FFC107")); // Yellow for neutral
                break;
            case "negative":
                holder.sentimentText.setTextColor(Color.parseColor("#F44336")); // Red for negative
                break;
            default:
                holder.sentimentText.setTextColor(Color.parseColor("#888888")); // Default gray for unknown
                break;
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView userNameText;
        TextView reviewText;
        TextView ratingText;
        TextView sentimentText; // Added sentiment text field

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameText = itemView.findViewById(R.id.userNameText);
            reviewText = itemView.findViewById(R.id.reviewText);
            ratingText = itemView.findViewById(R.id.ratingText);
            sentimentText = itemView.findViewById(R.id.sentimentText); // Initialize sentiment text
        }
    }

    /**
     * Update the data and refresh the RecyclerView.
     * @param newReviews The new list of reviews.
     */
    public void updateReviews(List<Review> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }
}

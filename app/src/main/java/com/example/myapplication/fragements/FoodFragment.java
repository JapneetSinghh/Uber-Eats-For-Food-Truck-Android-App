package com.example.myapplication.fragements;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.MyFoodTruck;
import com.example.myapplication.R;
import com.example.myapplication.SearchFoodActivity;
import com.example.myapplication.adapters.FoodHorRecAdapter;
import com.example.myapplication.adapters.FoodVerRecAdapter;
import com.example.myapplication.models.FoodHorModel;
import com.example.myapplication.models.FoodVerModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class FoodFragment extends Fragment {

    RecyclerView foodHorizontalRec;
    List<FoodHorModel> foodHorModelList;
    FoodHorRecAdapter foodHorRecAdapter;

    RecyclerView foodVerticalRec;
    List<FoodVerModel> foodVerModelList;
    FoodVerRecAdapter foodVerRecAdapter;

    private TextView textViewUsername;
    private DatabaseReference databaseReference;

    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {


        // Inflate the Horizontal layout for this fragment
        View root = inflater.inflate(R.layout.fragment_food, container, false);
        ImageView profileImageView = root.findViewById(R.id.profileImageView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        textViewUsername = root.findViewById(R.id.text_view1);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // Set an OnClickListener for the ImageView
        profileImageView.setOnClickListener(v -> {
            // Navigate to MyFoodTruck activity
            Intent intent = new Intent(requireActivity(), MyFoodTruck.class);
            startActivity(intent);
        });
        foodHorizontalRec = root.findViewById(R.id.food_hor_rec);
        foodVerticalRec = root.findViewById(R.id.food_ver_rec);

        //Horizontal Recycler View

        foodHorModelList = new ArrayList<>();

        foodHorModelList.add(new FoodHorModel(R.drawable.pizza, "Pizza"));
        foodHorModelList.add(new FoodHorModel(R.drawable.hamburger, "Hamburger"));
        foodHorModelList.add(new FoodHorModel(R.drawable.donut, "Donut"));
        foodHorModelList.add(new FoodHorModel(R.drawable.ramen, "Ramen"));
        foodHorModelList.add(new FoodHorModel(R.drawable.vegan, "Chipotle"));
        foodHorModelList.add(new FoodHorModel(R.drawable.ice_cream, "Ice-Cream"));

        foodHorRecAdapter = new FoodHorRecAdapter(getActivity(), foodHorModelList);
        foodHorizontalRec.setAdapter(foodHorRecAdapter);
        foodHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
        foodHorizontalRec.setHasFixedSize(true);
        foodHorizontalRec.setNestedScrollingEnabled(false);

        //Vertical Recycler View
        foodVerModelList = new ArrayList<>();

        foodVerModelList.add(new FoodVerModel(R.drawable.burger, "Burger"));
        foodVerModelList.add(new FoodVerModel(R.drawable.nacho, "Nacho"));
        foodVerModelList.add(new FoodVerModel(R.drawable.taco, "Taco"));
        foodVerModelList.add(new FoodVerModel(R.drawable.poutine, "Poutine"));
        foodVerModelList.add(new FoodVerModel(R.drawable.burrito, "Burrito"));
        foodVerModelList.add(new FoodVerModel(R.drawable.momos, "Momos"));

        foodVerRecAdapter = new FoodVerRecAdapter(getActivity(), foodVerModelList);

        foodVerticalRec.setAdapter(foodVerRecAdapter);
        foodVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        foodVerticalRec.setHasFixedSize(true);
        foodVerticalRec.setNestedScrollingEnabled(false);

        fetchUserName(userId);

        EditText searchFood = root.findViewById(R.id.searchByFood);
        searchFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), SearchFoodActivity.class);
                startActivity(intent);
            }
        });

        return root;

        //Inflate the Vertical Layout for the Fragment
    }
    private void fetchUserName(String userId) {
        // Retrieve the user's name from the "Users" node in Firebase
        databaseReference.child("Users").child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the name exists
                String username = snapshot.getValue(String.class);
                if (username != null) {
                    // Set the retrieved username to the TextView
                    textViewUsername.setText(username);
                } else {
                    // Set a default value if the username is not found
                    textViewUsername.setText("User");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
                textViewUsername.setText("Error fetching username");
            }
        });
    }
}
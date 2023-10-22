package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mobileapp.Model.BusinessModel;
import com.example.mobileapp.Model.DiscountModel;
import com.example.mobileapp.Model.OnItemClickListener;
import com.example.mobileapp.Model.ReviewModel;
import com.example.mobileapp.Model.UserModel;
import com.example.mobileapp.Util.AndroidUtil;
import com.example.mobileapp.Util.FirebaseUtil;
import com.example.mobileapp.adapter.BusinessRecyclerAdapter;
import com.example.mobileapp.adapter.DisplayUsersRecyclerAdapter;
import com.example.mobileapp.adapter.ReviewRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ReviewBusinessActivity extends AppCompatActivity  {

    EditText reviewInput;
    ImageButton sentReview;
    RecyclerView review_recycler_view;
    ReviewRecyclerAdapter reviewRecyclerAdapter;
    ArrayList<ReviewModel> reviewList;

    ReviewModel review;
    TextView txtCode;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_business);

        reviewInput=findViewById(R.id.review_input);
        sentReview=findViewById(R.id.sent_review);
        review_recycler_view=findViewById(R.id.review_recycler_view);
        txtCode=findViewById(R.id.txtCode);


        Intent intent = getIntent();
        String BusinessId = intent.getStringExtra("BusinessId");

        GetDiscountFromFirebase(BusinessId);




        reviewList =new ArrayList<>();

        setupReviewRecyclerView(BusinessId);

        reviewRecyclerAdapter = new ReviewRecyclerAdapter(getApplicationContext(),reviewList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        review_recycler_view.setLayoutManager(layoutManager);
        review_recycler_view.setAdapter(reviewRecyclerAdapter);

        sentReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                review =new ReviewModel();
                getUsernameAndCreateReview(review,BusinessId);


            }
        });

    }

    void getUsernameAndCreateReview(ReviewModel review,String BusinessId) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            UserModel currentUserModel= task.getResult().toObject(UserModel.class);
            review.setUserName(currentUserModel.getUsername());
            review.setBusinessId(BusinessId);
            review.setReview(reviewInput.getText().toString());
            review.setUserId(FirebaseUtil.currentUserId());
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            String formattedDateTime = currentDateTime.format(formatter);
            review.setDate(formattedDateTime);
            CreateReview(review);
        });
    }
    private  void CreateReview(ReviewModel review){
        FirebaseUtil.databaseReference().child("Review").push().setValue(review, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Review was successfully written to the database
                } else {
                    // Handle the error
                }
            }
        });
    }
    private void setupReviewRecyclerView(String BusinessId ) {

        DatabaseReference query = FirebaseUtil.databaseReference().child("Review");

        query.orderByChild("businessId").equalTo(BusinessId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClearAll();
                Log.d("FirebaseData", "Data received");
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ReviewModel review = new ReviewModel();
                    if (snapshot1.child("userName").exists()) {
                        review.setUserName(snapshot1.child("userName").getValue().toString());
                    }
                    if (snapshot1.child("review").exists()) {
                        review.setReview(snapshot1.child("review").getValue().toString());
                    }
                    if (snapshot1.child("date").exists()) {
                        review.setDate(snapshot1.child("date").getValue().toString());
                    }
                    if (snapshot1.child("userId").exists()) {
                        review.setUserId(snapshot1.child("userId").getValue().toString());
                    }
                    if (snapshot1.child("businessId").exists()) {
                        review.setBusinessId(snapshot1.child("businessId").getValue().toString());
                    }
                    reviewList.add(review);
                }
                reviewRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if necessary
            }

        });
    }
    private void GetDiscountFromFirebase(String BusinessId) {
        Query query = FirebaseUtil.databaseReference().child("Discount")
                .orderByChild("BusinessId")
                .equalTo(BusinessId);
        query.addValueEventListener(new ValueEventListener() {

        @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    DiscountModel discount = new DiscountModel();
                    if (snapshot1.child("DiscoutCode").exists()) {
                        discount.setDiscountCode(snapshot1.child("DiscoutCode").getValue().toString());
                    }
                   txtCode.setText(discount.getDiscountCode());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });
    }

    private void ClearAll() {
        reviewList.clear();
        reviewRecyclerAdapter.notifyDataSetChanged();
    }
}
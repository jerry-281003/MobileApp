package com.example.mobileapp;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobileapp.Model.BusinessModel;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.BusinessRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.mobileapp.Util.FirebaseUtil;

import java.util.ArrayList;

public class BusinessInformationActivity extends AppCompatActivity {

    RecyclerView recycler_business_information;
    private ArrayList<BusinessModel> businessList;
    private BusinessRecyclerAdapter businessRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_information);

        recycler_business_information = findViewById(R.id.Business_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_business_information.setLayoutManager(layoutManager);
        recycler_business_information.setHasFixedSize(true);

        businessList = new ArrayList<>();
        businessRecyclerAdapter = new BusinessRecyclerAdapter(getApplicationContext(), businessList);
        recycler_business_information.setAdapter(businessRecyclerAdapter);

        GetDataFromFirebase();
    }

    private void GetDataFromFirebase() {
        Query query = FirebaseUtil.databaseReference().child("Business");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClearAll();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    BusinessModel business = new BusinessModel();
                    if (snapshot1.child("photoUrl").exists()) {
                        business.setPhotoUrl(snapshot1.child("photoUrl").getValue().toString());
                    }
                    if (snapshot1.child("Description").exists()) {
                        business.setDescription(snapshot1.child("Description").getValue().toString());
                    }
                    if (snapshot1.child("Phone").exists()) {
                        business.setPhone(snapshot1.child("Phone").getValue().toString());
                    }
                    if (snapshot1.child("Address").exists()) {
                        business.setAddress(snapshot1.child("Address").getValue().toString());
                    }
                    if (snapshot1.child("BusinessName").exists()) {
                        business.setBusinessName(snapshot1.child("BusinessName").getValue().toString());
                    }
                    if (snapshot1.child("BusinessId").exists()) {
                        business.setBusinessId(snapshot1.child("BusinessId").getValue().toString());
                    }

                    businessList.add(business);
                }
                businessRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });
    }

    private void ClearAll() {
        businessList.clear();
        businessRecyclerAdapter.notifyDataSetChanged();
    }
}

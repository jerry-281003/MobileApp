package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobileapp.Model.BusinessModel;
import com.example.mobileapp.Model.DataTransferListener;
import com.example.mobileapp.Model.OnItemClickListener;
import com.example.mobileapp.Model.UserModel;
import com.example.mobileapp.Util.AndroidUtil;
import com.example.mobileapp.Util.FirebaseUtil;
import com.example.mobileapp.adapter.DisplayUsersRecyclerAdapter;
import com.example.mobileapp.adapter.EventRecyclerAdapter;
import com.example.mobileapp.adapter.SearchUsersRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class CreateGroupChatActivity extends AppCompatActivity  implements DataTransferListener, OnItemClickListener {
    EditText searchUserInput;
    ImageButton searchUserButton;

    RecyclerView recyclerViewSearchUser;

    EditText searchEventInput;
    ImageButton searchEventButton;
    RecyclerView recyclerViewSearchEvent;


    DisplayUsersRecyclerAdapter displayUsersAdapter;
    RecyclerView displayUsersRecyclerView;


    SearchUsersRecyclerAdapter adapter;
    EventRecyclerAdapter eventAdapter;
    ArrayList<BusinessModel> businessList;
    ArrayList<UserModel> userList;

    TextView txtNameBusiness;
    TextView txtDescription;
    TextView txtAddressBusiness;
    TextView txtPhoneBusiness;
    ImageView imageBusiness;
    Button btnCreateGroupChat;

    @Override
    public void onItemClick(BusinessModel businessModel) {
        VisibleViewCardEvent();
        InVisibleRecyclerEventView();
        SetTextCardView(businessModel);

    }
    @Override
    public void onDataTransfer(UserModel user) {
        boolean userExists = false;
        InVisibleRecyclerUserView();
        for (UserModel existingUser : userList) {
            if (existingUser.getUserId().equals(user.getUserId())) {
                userExists = true;
                break;
            }
        }

        // If the user doesn't exist in the list, add them
        if (!userExists) {
            userList.add(user);
            displayUsersAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chat);
        InVisibleCardEventView();

        searchUserInput = findViewById(R.id.seach_username_input);
        searchUserButton = findViewById(R.id.search_user_btn);
        recyclerViewSearchUser = findViewById(R.id.search_user_recycler_view);
        searchUserInput.requestFocus();

        searchEventInput=findViewById(R.id.seach_event_input);
        searchEventButton=findViewById(R.id.search_event_btn);
        recyclerViewSearchEvent=findViewById(R.id.search_event_recycler_view);

        txtNameBusiness= findViewById(R.id.txtNameBusiness);
        txtDescription= findViewById(R.id.txtDescription);
        txtAddressBusiness= findViewById(R.id.txtAddressBusiness);
        txtPhoneBusiness= findViewById(R.id.txtPhoneBusiness);
        imageBusiness=findViewById(R.id.imageBusiness);

        btnCreateGroupChat= findViewById(R.id.btn_create_group_chat);


        searchUserButton.setOnClickListener(v -> {
            VisibleRecyclerUserView();
            String searchTerm = searchUserInput.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length()<3){
                searchUserInput.setError("Invalid Username");
                return;
            }
            setupSearchUsersRecyclerView(searchTerm);

        });

        searchEventButton.setOnClickListener(v -> {
            VisibleRecyclerEventView();
            String searchTerm = searchEventInput.getText().toString();


            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerViewSearchEvent.setLayoutManager(layoutManager);


            businessList = new ArrayList<>();

            setupSearchEventRecyclerView();
            for (BusinessModel business : businessList) {
                if (business.getBusinessName().equals(searchTerm)) {
                    // Phần tử này có thuộc tính BusinessName bằng giá trị bạn đang tìm
                    businessList.add(business);
                }
            }
            eventAdapter = new EventRecyclerAdapter(getApplicationContext(), businessList);
            recyclerViewSearchEvent.setAdapter(eventAdapter);
            eventAdapter.setOnItemClickListener(this);
        });


        displayUsersRecyclerView = findViewById(R.id.display_user_recycler_view);
        userList = new ArrayList<>();
        displayUsersAdapter = new DisplayUsersRecyclerAdapter(userList, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        displayUsersRecyclerView.setLayoutManager(layoutManager);
        displayUsersRecyclerView.setAdapter(displayUsersAdapter);

        btnCreateGroupChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            for (UserModel user : userList) {
                AndroidUtil.passUserModelAsIntent(intent,user);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });



    }
    void setupSearchUsersRecyclerView(String searchTerm){


        Query query = FirebaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username",searchTerm)
                .whereLessThanOrEqualTo("username",searchTerm+'\uf8ff');

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        adapter = new SearchUsersRecyclerAdapter(options,getApplicationContext());
        recyclerViewSearchUser.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchUser.setAdapter(adapter);
        adapter.setDataTransferListener(this);
        adapter.startListening();

    }
    private void setupSearchEventRecyclerView() {

        DatabaseReference query = FirebaseUtil.databaseReference().child("Business");

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
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if necessary
            }

        });
    }

    
    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }
    private void ClearAll() {
        businessList.clear();
        eventAdapter.notifyDataSetChanged();
    }
    private void InVisibleCardEventView() {
        CardView cardEventChoose = findViewById(R.id.card_event_choose);
        cardEventChoose.setVisibility(View.GONE);
        cardEventChoose.invalidate();
    }
    private void VisibleViewCardEvent() {
        CardView cardEventChoose = findViewById(R.id.card_event_choose);
        cardEventChoose.setVisibility(View.VISIBLE);
    }
    void VisibleRecyclerUserView() {
        RecyclerView RecyclerUserView = findViewById(R.id.search_user_recycler_view);
        RecyclerUserView.setVisibility(View.VISIBLE);
    }
    void InVisibleRecyclerUserView() {
        RecyclerView RecyclerUserView = findViewById(R.id.search_user_recycler_view);
        RecyclerUserView.setVisibility(View.GONE);
    }
    void VisibleRecyclerEventView() {
        RecyclerView RecyclerEventView = findViewById(R.id.search_event_recycler_view);
        RecyclerEventView.setVisibility(View.VISIBLE);
    }
    void InVisibleRecyclerEventView() {
        RecyclerView RecyclerEventView = findViewById(R.id.search_event_recycler_view);
        RecyclerEventView.setVisibility(View.GONE);
    }
    private void SetTextCardView(BusinessModel business){

        txtNameBusiness.setText(business.getBusinessName());
        txtDescription.setText(business.getDescription());
        txtAddressBusiness.setText(business.getAddress());
        txtPhoneBusiness.setText(business.getPhone());

        Glide.with(this)
                .load(business.getPhotoUrl())
                .into(imageBusiness);
    }
}

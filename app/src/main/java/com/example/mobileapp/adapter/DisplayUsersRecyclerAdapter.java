package com.example.mobileapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.Model.BusinessModel;
import com.example.mobileapp.Model.DataTransferListener;
import com.example.mobileapp.Model.UserModel;
import com.example.mobileapp.R;
import com.example.mobileapp.Util.AndroidUtil;
import com.example.mobileapp.Util.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class DisplayUsersRecyclerAdapter extends RecyclerView.Adapter<DisplayUsersRecyclerAdapter.UserModelViewHolder> {
    Context context;
    private List<UserModel> userList;
    public DisplayUsersRecyclerAdapter(List<UserModel> userList, Context context) {
        this.context = context;
        this.userList=userList;
    }

    @Override
    public void onBindViewHolder(@NonNull UserModelViewHolder holder, int position) {
        FirebaseUtil.getOtherProfilePicStorageRef(userList.get(position).getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                    }
                });
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }



    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_profile_search,parent, false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
package com.example.mobileapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileapp.ChatActivity;
import com.example.mobileapp.Model.BusinessModel;
import com.example.mobileapp.Model.OnItemClickListener;
import com.example.mobileapp.Model.ReviewModel;
import com.example.mobileapp.Model.UserModel;
import com.example.mobileapp.R;
import com.example.mobileapp.Util.AndroidUtil;
import com.example.mobileapp.Util.FirebaseUtil;

import java.util.List;



public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewHolder> {
    private Context context;
    private List<ReviewModel> reviewModel;


    public ReviewRecyclerAdapter(Context context, List<ReviewModel> reviewModel) {
        this.context = context;
        this.reviewModel = reviewModel;
    }


    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerAdapter.ReviewHolder holder, int position) {

        holder.userName.setText(reviewModel.get(position).getUserName());
        holder.reviewTxt.setText(reviewModel.get(position).getReview());
        holder.Date.setText(reviewModel.get(position).getDate().toString());
        FirebaseUtil.getOtherProfilePicStorageRef(reviewModel.get(position).getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.imageUser);
                    }
                });
    }
    @Override
    public int getItemCount() {
        return reviewModel.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView reviewTxt;
        TextView Date;
        ImageView imageUser;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_text);
            reviewTxt = itemView.findViewById(R.id.review_text);
            Date = itemView.findViewById(R.id.date_text);
            imageUser = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}

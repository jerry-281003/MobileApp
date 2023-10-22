package com.example.mobileapp.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.mobileapp.R;
import com.example.mobileapp.ReviewBusinessActivity;
import com.example.mobileapp.Util.AndroidUtil;

import java.util.ArrayList;

public class BusinessRecyclerAdapter extends RecyclerView.Adapter<BusinessRecyclerAdapter.ViewHolder>
{

    private Context mContext;
    private ArrayList<BusinessModel> businessList;


    public BusinessRecyclerAdapter(Context mContext, ArrayList<BusinessModel> businessList) {
        this.mContext = mContext;
        this.businessList = businessList;
    }

    @NonNull
    @Override
    public BusinessRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.business_item,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessRecyclerAdapter.ViewHolder holder, int position) {
        holder.txtDescription.setText(businessList.get(position).getDescription());
        holder.txtNameBusiness.setText(businessList.get(position).getBusinessName());
        holder.txtPhoneBusiness.setText(businessList.get(position).getPhone());
        holder.txtAddressBusiness.setText(businessList.get(position).getAddress());
        Glide.with(mContext)
                .load(businessList.get(position).getPhotoUrl())
                .override(200,150)
                .into(holder.BusinessImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ReviewBusinessActivity.class);
            intent.putExtra("BusinessId", businessList.get(position).getBusinessId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView BusinessImage;
        TextView txtDescription;
        TextView txtNameBusiness;
        TextView txtAddressBusiness;
        TextView txtPhoneBusiness;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            BusinessImage=itemView.findViewById(R.id.imageBusiness);
            txtDescription=itemView.findViewById(R.id.txtDescription);
            txtNameBusiness=itemView.findViewById(R.id.txtNameBusiness);
            txtAddressBusiness=itemView.findViewById(R.id.txtAddressBusiness);
            txtPhoneBusiness=itemView.findViewById(R.id.txtPhoneBusiness);
        }
    }
}

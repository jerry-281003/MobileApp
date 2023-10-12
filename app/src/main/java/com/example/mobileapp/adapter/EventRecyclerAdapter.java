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

import com.bumptech.glide.Glide;
import com.example.mobileapp.Model.BusinessModel;
import com.example.mobileapp.Model.OnItemClickListener;
import com.example.mobileapp.Model.UserModel;
import com.example.mobileapp.R;
import com.example.mobileapp.Util.AndroidUtil;
import com.example.mobileapp.Util.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventViewHolder> {
    private Context context;
    private List<BusinessModel> eventList;
    private OnItemClickListener onItemClickListener;

    public EventRecyclerAdapter(Context context, List<BusinessModel> eventList) {
        this.context = context;
        this.eventList = eventList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.business_card_view, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapter.EventViewHolder holder, int position) {
        holder.txtDescription.setText(eventList.get(position).getDescription());
        holder.txtNameBusiness.setText(eventList.get(position).getBusinessName());
        holder.txtPhoneBusiness.setText(eventList.get(position).getPhone());
        holder.txtAddressBusiness.setText(eventList.get(position).getAddress());


        Glide.with(context)
                .load(eventList.get(position).getPhotoUrl())
                .into(holder.imageBusiness);



        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(eventList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameBusiness;
        TextView txtDescription;
        TextView txtPhoneBusiness;
        TextView txtAddressBusiness;
        ImageView imageBusiness;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameBusiness = itemView.findViewById(R.id.txtNameBusiness);
            txtPhoneBusiness = itemView.findViewById(R.id.txtPhoneBusiness);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtAddressBusiness = itemView.findViewById(R.id.txtAddressBusiness);
            imageBusiness = itemView.findViewById(R.id.imageBusiness);
        }
    }
}


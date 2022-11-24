package com.example.blog.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.NotificationRO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.AdapterMemberViewHolder> {
    private List<NotificationRO> mMemberList;
    private OnNotificationClickListener mOnNotificationClickListener;

    public AdapterNotification(List<NotificationRO> mMemberList, OnNotificationClickListener listener) {
        this.mMemberList = mMemberList;
        this.mOnNotificationClickListener = listener;
    }

    @NonNull
    @Override
    public AdapterMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification, parent, false);
        return new AdapterMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMemberViewHolder holder, int position) {
        NotificationRO f = mMemberList.get(position);
        if(f == null) {
            return;
        }

        if(f.getHas_seen() == 0) {
            holder.iconHasSeen.setVisibility(View.GONE);
        } else {
            holder.iconHasSeen.setVisibility(View.VISIBLE);
        }
        holder.txtContent.setText(f.getContent());
        holder.txtCreated.setText(f.getCreated_at());

        holder.onNotificationClickListener = mOnNotificationClickListener;
        holder.txtContent.setOnClickListener(view -> holder.onNotificationClickListener.onNotificationClickListener(f));
        holder.txtCreated.setOnClickListener(view -> holder.onNotificationClickListener.onNotificationClickListener(f));
    }

    @Override
    public int getItemCount() {
        if(mMemberList != null) {
            return mMemberList.size();
        }
        return 0;
    }

    class AdapterMemberViewHolder extends RecyclerView.ViewHolder {
        private TextView txtContent;
        private TextView txtCreated;
        private ImageButton iconHasSeen;
        private OnNotificationClickListener onNotificationClickListener;

        public AdapterMemberViewHolder(@NonNull View item) {
            super(item);
            txtContent = (TextView) item.findViewById(R.id.txtContent);
            txtCreated = (TextView) item.findViewById(R.id.txtCreated);
            iconHasSeen = (ImageButton) item.findViewById(R.id.iconHasSeen);
        }
    }
}

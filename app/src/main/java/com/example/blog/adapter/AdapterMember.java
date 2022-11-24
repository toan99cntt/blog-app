package com.example.blog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.MemberRO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMember extends RecyclerView.Adapter<AdapterMember.AdapterMemberViewHolder> {
    private List<MemberRO> mMemberList;
    private OnMemberClickListener mOnMemberClickListener;

    public AdapterMember(List<MemberRO> mMemberList, OnMemberClickListener listener) {
        this.mMemberList = mMemberList;
        this.mOnMemberClickListener = listener;
    }

    @NonNull
    @Override
    public AdapterMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_member, parent, false);
        return new AdapterMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMemberViewHolder holder, int position) {
        MemberRO f = mMemberList.get(position);
        if(f == null) {
            return;
        }
        if(f.getAvatar() != null && f.getAvatar().length> 0) {
            String url = f.getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
            Picasso.get().load(url).into(holder.imgAvatar);
        }
        holder.txtName.setText(f.getName());
        holder.txtEmail.setText(f.getEmail());
        holder.onMemberClickListener = mOnMemberClickListener;
        holder.txtName.setOnClickListener(view -> holder.onMemberClickListener.onMemberClickListener(f));
    }

    @Override
    public int getItemCount() {
        if(mMemberList != null) {
            return mMemberList.size();
        }
        return 0;
    }

    class AdapterMemberViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView txtName;
        private TextView txtEmail;
        private OnMemberClickListener onMemberClickListener;

        public AdapterMemberViewHolder(@NonNull View item) {
            super(item);
            imgAvatar = (ImageView) item.findViewById(R.id.imgAvatar);
            txtName = (TextView) item.findViewById(R.id.txtName);
            txtEmail = (TextView) item.findViewById(R.id.txtEmail);
        }
    }
}

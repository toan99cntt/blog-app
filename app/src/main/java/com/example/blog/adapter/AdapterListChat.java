package com.example.blog.adapter;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.MemberRO;
import com.example.blog.model.MessageRO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterListChat extends RecyclerView.Adapter<AdapterListChat.AdapterMemberViewHolder> {
    private List<MessageRO> mMemberList;
    private OnMemberChatClickListener mOnMemberChatClickListener;
    private int id;

    public AdapterListChat(List<MessageRO> mMemberList, OnMemberChatClickListener listener, int id) {
        this.mMemberList = mMemberList;
        this.mOnMemberChatClickListener = listener;
        this.id = id;
    }

    @NonNull
    @Override
    public AdapterMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_member_chat, parent, false);
        return new AdapterMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMemberViewHolder holder, int position) {
        MessageRO f = mMemberList.get(position);
        if(f == null || f.getSender() == null || f.getReceiver() == null) {
            return;
        }
        MemberRO receiver;
        if(id == f.getSender().getId()) {
            receiver = f.getReceiver();
        } else {
            receiver = f.getSender();
        }
        if(receiver.getAvatar() != null && receiver.getAvatar().length> 0) {
            String url = receiver.getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
            Picasso.get().load(url).into(holder.imgAvatar);
        }
        holder.txtName.setText(receiver.getName());
        holder.txtContent.setText(f.getType().equals("text") ? f.getContent() : "Image" );
        holder.txtCreated.setText(f.getCreated_at());
        holder.onMemberChatClickListener = mOnMemberChatClickListener;
        holder.txtName.setOnClickListener(view -> holder.onMemberChatClickListener.onMemberChatClickListener(receiver));
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
        private TextView txtContent;
        private TextView txtCreated;
        private OnMemberChatClickListener onMemberChatClickListener;

        public AdapterMemberViewHolder(@NonNull View item) {
            super(item);
            imgAvatar = (ImageView) item.findViewById(R.id.imgAvatar);
            txtName = (TextView) item.findViewById(R.id.txtName);
            txtContent = (TextView) item.findViewById(R.id.txtMessage);
            txtCreated = (TextView) item.findViewById(R.id.txtCreated);
        }
    }
}

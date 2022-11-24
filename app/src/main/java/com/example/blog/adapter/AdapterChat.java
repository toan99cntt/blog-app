package com.example.blog.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.MessageRO;
import com.example.blog.model.MessageRO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.AdapterChatViewHolder> {
    private List<MessageRO> mMessageList;
    private int id_receiver;

    public AdapterChat(List<MessageRO> mMessageList, int id_receiver) {
        this.mMessageList = mMessageList;
        this.id_receiver = id_receiver;
    }

    @NonNull
    @Override
    public AdapterChat.AdapterChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message, parent, false);
        return new AdapterChat.AdapterChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChatViewHolder holder, int position) {
        MessageRO f = mMessageList.get(position);
        if(f == null) {
            return;
        }
        if(f.getAttachments() != null && f.getAttachments().length> 0) {
            String url = f.getAttachments()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
            Picasso.get().load(url).into(holder.image);
            holder.textView3.setVisibility(View.GONE);
        } else {
            if(f.getContent() != null) holder.textView3.setText(f.getContent());
        }
        if(f.getSender().getId() != id_receiver) {
            holder.layout.setGravity(Gravity.RIGHT);
            holder.layoutMessage.setGravity(Gravity.RIGHT);
        } else {
            holder.layout.setGravity(Gravity.LEFT);
            holder.layoutMessage.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public int getItemCount() {
        if(mMessageList != null) {
            return mMessageList.size();
        }
        return 0;
    }

    class AdapterChatViewHolder extends RecyclerView.ViewHolder {

        private TextView textView3;
        private ImageView image;
        private TextView textView4;
        private LinearLayout layout;
        private LinearLayout layoutMessage;

        public AdapterChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textView3 = (TextView) itemView.findViewById(R.id.textView3);
            image = (ImageView) itemView.findViewById(R.id.image);
            layout = itemView.findViewById(R.id.layout);
            layoutMessage = itemView.findViewById(R.id.layoutMessage);
//            textView4 = (TextView) itemView.findViewById(R.id.textView4);
        }
    }

}

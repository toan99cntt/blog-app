package com.example.blog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.CommentRO;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.AdapterInfoViewHolder> {
    private List<CommentRO> mComment;

    public AdapterComment(List<CommentRO> mComment) {
        this.mComment = mComment;
    }

    @NonNull
    @Override
    public AdapterComment.AdapterInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);
        return new AdapterComment.AdapterInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterInfoViewHolder holder, int position) {
        CommentRO f = mComment.get(position);
        if(f == null) {
            return;
        }
        if(f.getMember().getAvatar() != null && f.getMember().getAvatar().length> 0) {
            String url = f.getMember().getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
            Picasso.get().load(url).into(holder.imgAvatar);
        }
        holder.txtName.setText(f.getMember().getName());
        holder.txtContent.setText(f.getContent());
        holder.txtCreated.setText(f.getCreated_at());
    }

    @Override
    public int getItemCount() {
        if(mComment != null) {
            return mComment.size();
        }
        return 0;
    }

    class AdapterInfoViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgAvatar;
        private TextView txtName;
        private TextView txtContent;
        private TextView txtCreated;

        public AdapterInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = (CircleImageView) itemView.findViewById(R.id.imgAvatar);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            txtCreated = (TextView) itemView.findViewById(R.id.txtCreated);
        }
    }

}

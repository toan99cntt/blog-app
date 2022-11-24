package com.example.blog.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.Blog;
import com.example.blog.model.BlogRO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterInfo extends RecyclerView.Adapter<AdapterInfo.AdapterInfoViewHolder> {
    private List<BlogRO> mBlogList;

    public AdapterInfo(List<BlogRO> mBlogList) {
        this.mBlogList = mBlogList;
    }

    @NonNull
    @Override
    public AdapterInfo.AdapterInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_blog, parent, false);
        return new AdapterInfo.AdapterInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterInfoViewHolder holder, int position) {
        BlogRO f = mBlogList.get(position);
        if(f == null) {
            return;
        }
        if(f.getMember().getAvatar() != null && f.getMember().getAvatar().length> 0) {
            String url = f.getMember().getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
            Picasso.get().load(url).into(holder.imgAvatar);
        }
        holder.txtName.setText(f.getMember().getName());
        holder.txtCreated.setText(f.getCreated_at());
        holder.txtTitle.setText(f.getTitle());
        holder.txtContent.setText(f.getContent());
        holder.txtNumberLike.setText(f.getLike_count() + " like  " + f.getComments().size() + " comment");
    }

    @Override
    public int getItemCount() {
        if(mBlogList != null) {
            return mBlogList.size();
        }
        return 0;
    }

    class AdapterInfoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView txtName;
        private TextView txtCreated;
        private TextView txtTitle;
        private TextView txtContent;
        private ImageView imgContent;
        private TextView txtNumberLike;

        public AdapterInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCreated = (TextView) itemView.findViewById(R.id.txtCreated);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            imgContent = (ImageView) itemView.findViewById(R.id.imgContent);
            txtNumberLike = (TextView) itemView.findViewById(R.id.txtNumberLike);
        }
    }

}

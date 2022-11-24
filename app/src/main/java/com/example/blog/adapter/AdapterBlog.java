package com.example.blog.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.example.blog.model.Blog;
import com.example.blog.model.BlogRO;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBlog extends RecyclerView.Adapter<AdapterBlog.AdapterBlogViewHolder> {

    private List<BlogRO> mBlogList;
    OnBlogClickListener mClickListener;
    private String screen;

    public AdapterBlog(List<BlogRO> mBlogList, OnBlogClickListener listener, String screen) {
        this.mBlogList = mBlogList;
        this.mClickListener = listener;
        this.screen = screen;
    }

    @NonNull
    @Override
    public AdapterBlog.AdapterBlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_blog, parent, false);
        return new AdapterBlog.AdapterBlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBlogViewHolder holder, int position) {
        OnBlogClickListener clickListener;
        BlogRO f = mBlogList.get(position);
        if(f == null) {
            return;
        }
        if(f.getMember().getAvatar() != null && f.getMember().getAvatar().length> 0) {
            String urlAvatar = f.getMember().getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
            Picasso.get().load(urlAvatar).into(holder.imgAvatar);
        }
        if(f.getImage() != null && f.getImage().length> 0) {
            String url = f.getImage()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
            Picasso.get().load(url).into(holder.imgContent);
        }

        if(f.isLikes()) {
            Drawable img = holder.btnLike.getContext().getResources().getDrawable( R.drawable.ic_like_blue );
            holder.btnLike.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            holder.btnLike.setTextColor(Color.parseColor("#3F51B5"));
        }

        holder.txtName.setText(f.getMember().getName());
        holder.txtCreated.setText(f.getCreated_at());
        holder.txtTitle.setText(f.getTitle());
        holder.txtContent.setText(f.getContent());
        holder.txtView.setText(f.getView_count() + "  view  ");
        holder.txtNumberLike.setText(f.getLike_count() + " like  " + f.getComments().size() + " comment");
        holder.onBlogClickListener = mClickListener;
        if(screen.equals("myself")) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnEdit.setOnClickListener(view -> holder.onBlogClickListener.onBlogClickSettingListener(f));
        }
        if(screen.equals("home")) {
            holder.btnLike.setOnClickListener(view -> holder.onBlogClickListener.onBlogClickLikeListener(f));
            holder.txtName.setOnClickListener(view -> holder.onBlogClickListener.onBlogClickListener(f));
        }
        holder.btnComment.setOnClickListener(view -> holder.onBlogClickListener.onBlogClickCommentListener(f));
    }

    @Override
    public int getItemCount() {
        if(mBlogList != null) {
            return mBlogList.size();
        }
        return 0;
    }

    class AdapterBlogViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView txtName;
        private TextView txtCreated;
        private TextView txtTitle;
        private TextView btnComment;
        private TextView btnLike;
        private TextView txtContent;
        private ImageView imgContent;
        private TextView txtNumberLike;
        private TextView txtView;
        private ImageButton btnEdit;
        private OnBlogClickListener onBlogClickListener;

        public AdapterBlogViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCreated = (TextView) itemView.findViewById(R.id.txtCreated);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            btnLike = (TextView) itemView.findViewById(R.id.btnLike);
            btnComment = (TextView) itemView.findViewById(R.id.btnComment);
            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            imgContent = (ImageView) itemView.findViewById(R.id.imgContent);
            txtNumberLike = (TextView) itemView.findViewById(R.id.txtNumberLike);
            txtView = (TextView) itemView.findViewById(R.id.txtView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}

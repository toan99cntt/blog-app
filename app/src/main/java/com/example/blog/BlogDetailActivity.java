package com.example.blog;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.adapter.AdapterComment;
import com.example.blog.api.ApiService;
import com.example.blog.fragment.HomeFragment;
import com.example.blog.model.BlogDetail;
import com.example.blog.model.BlogRO;
import com.example.blog.model.Comment;
import com.example.blog.model.CommentRO;
import com.example.blog.model.Like;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogDetailActivity extends AppCompatActivity {
    private CircleImageView imgAvatar;
    private TextView txtName;
    private TextView txtCreated;
    private TextView txtTitle;
    private TextView txtContent;
    private ImageView imgContent;
    private TextView txtNumberLike;
    private TextView btnLike;
    private TextView txtView;
    private ImageButton btnSend;
    private EditText edtMessage;
    private TextView btnComment;
    private RecyclerView recyclerView;
    private List<CommentRO> mLst;
    private AdapterComment mAdapterComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        init();
    }

    protected void onResume() {
        super.onResume();
        init();
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        mLst = new ArrayList<>();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPref.getString("token", null);
        if(token == null) return;
        fetchData(id, token);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.createComment(id, edtMessage.getText().toString(), token).enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.body().getStatus() == 200) {
                            edtMessage.setText("");
                            fetchData(id, token);
                        } else {
                            Log.d(TAG, "onResponse: " + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Log.d(TAG, t.getMessage());
                    }
                });
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLike(token, id);
            }
        });
    }

    public void onClickLike(String token, int id) {
        ApiService.apiService.setLikeOrUnlike(id, token).enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                Like res = response.body();
                if (res.getStatus() == 200) {
                    fetchData(id, token);
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                onClickLike(token, id);
            }
        });
    }

    private void fetchData(int id, String token) {
        ApiService.apiService.getBlogDetail(id, token).enqueue(new Callback<BlogDetail>() {
            @Override
            public void onResponse(Call<BlogDetail> call, Response<BlogDetail> response) {
                BlogDetail res = response.body();
                if(res.getStatus() == 200) {
                    BlogRO blog = res.getData();
                    if(blog.getImage() != null && blog.getImage().length> 0) {
                        String url = blog.getImage()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
                        Picasso.get().load(url).into(imgContent);
                    }
                    if(blog.getMember().getAvatar() != null && blog.getMember().getAvatar().length> 0) {
                        String url = blog.getMember().getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
                        Picasso.get().load(url).into(imgAvatar);
                    }
                    if(res.getData().isLikes()) {
                        Drawable img = btnLike.getContext().getResources().getDrawable( R.drawable.ic_like_blue );
                        btnLike.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                        btnLike.setTextColor(Color.parseColor("#3F51B5"));
                    } else {
                        Drawable img = btnLike.getContext().getResources().getDrawable( R.drawable.ic_like );
                        btnLike.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                        btnLike.setTextColor(Color.parseColor("#5C5C5C"));
                    }
                    txtName.setText(res.getData().getMember().getName());
                    txtCreated.setText(res.getData().getCreated_at());
                    txtContent.setText(res.getData().getContent());
                    txtTitle.setText(res.getData().getTitle());
                    txtNumberLike.setText(res.getData().getLike_count() + " like  " + res.getData().getComments().size() + " comment");
                    txtView.setText(res.getData().getView_count() + "  view  ");
                    mLst = res.getData().getComments();
                    mAdapterComment = new AdapterComment(mLst);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BlogDetailActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(mAdapterComment);

                } else {
                    Toast.makeText(BlogDetailActivity.this, "Error!" + res.getStatus(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BlogDetail> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                fetchData(id, token);
            }
        });

    }

    private void init() {
        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        txtName = (TextView) findViewById(R.id.txtName);
        txtCreated = (TextView) findViewById(R.id.txtCreated);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtContent = (TextView) findViewById(R.id.txtContent);
        imgContent = (ImageView) findViewById(R.id.imgContent);
        txtNumberLike = (TextView) findViewById(R.id.txtNumberLike);
        btnLike = (TextView) findViewById(R.id.btnLike);
        btnComment = (TextView) findViewById(R.id.btnComment);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnSend = findViewById(R.id.btnSend);
        edtMessage = findViewById(R.id.edtMessage);
        txtView = findViewById(R.id.txtView);
    }
}
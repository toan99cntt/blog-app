package com.example.blog;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.adapter.AdapterBlog;
import com.example.blog.adapter.AdapterBlogOrMember;
import com.example.blog.adapter.OnBlogClickListener;
import com.example.blog.api.ApiService;
import com.example.blog.model.Blog;
import com.example.blog.model.BlogRO;
import com.example.blog.model.Like;
import com.example.blog.model.User;
import com.example.blog.model.UserRO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends AppCompatActivity implements OnBlogClickListener {
    private CircleImageView imgAvatar;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtPhone;
    private TextView txtDob;
    private TextView txtGender;
    private Button btnChat;
    private RecyclerView recyclerView;
    private List<BlogRO> mLst;
    private AdapterBlogOrMember adapterBlogOrMember;
    private AdapterBlog mAdapterBlog;
    private BottomNavigationView bottomNavigationView;
    private String token;
    private int myId;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        mLst = new ArrayList<>();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPref.getString("token", null);
        myId = sharedPref.getInt("id", 0);
        if(myId == id) btnChat.setVisibility(View.GONE);
        if(token == null) return;
        getInfoUserById();
    }

    private void getInfoUserById() {
        ApiService.apiService.getMemberById(id, token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User res = response.body();
                if(res.getStatus() == 200) {
                    UserRO user = res.getData();
                    if(user.getAvatar() != null && user.getAvatar().length> 0) {
                        String url = user.getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
                        Picasso.get().load(url).into(imgAvatar);
                    }

                    txtName.setText(res.getData().getName());
                    txtEmail.setText("Email address: " + res.getData().getEmail());
                    txtGender.setText("Gender: -");
                    txtDob.setText("Birthday: " + res.getData().getDob());
                    txtPhone.setText("Phone: " + res.getData().getPhone_number());
                    mLst = res.getData().getBlogs();
                    adapterBlogOrMember = new AdapterBlogOrMember(mLst, InfoActivity.this, res.getData());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InfoActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapterBlogOrMember);
                    btnChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(InfoActivity.this, ChatActivity.class);
                            intent.putExtra("id_receiver", res.getData().getId());
                            intent.putExtra("name", res.getData().getName());
                            intent.putExtra("avatar", user.getAvatar().length> 0 ? user.getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000") : "");
                            startActivity(intent);
                        }
                    });
                    mLst = new ArrayList<>();

                } else {
                    Toast.makeText(InfoActivity.this, "Error!" + res.getStatus(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                getInfoUserById();
            }
        });

    }

    private void init() {
        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtDob = (TextView) findViewById(R.id.txtDob);
        txtGender = (TextView) findViewById(R.id.txtGender);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnChat = findViewById(R.id.btnChat);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
    }

    @Override
    public void onBlogClickListener(BlogRO b) {
//        Log.d("XXXXX", b.getTitle());
    }

    @Override
    public void onBlogClickCommentListener(BlogRO b) {
        Intent intent = new Intent(InfoActivity.this, BlogDetailActivity.class);
        intent.putExtra("id",b.getId());
        startActivity(intent);
    }

    @Override
    public void onBlogClickLikeListener(BlogRO b) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPref.getString("token", null);
        if(token == null) return;
        ApiService.apiService.setLikeOrUnlike(b.getId(), token).enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                Like res = response.body();
                if (res.getStatus() == 200) {
                    getInfoUserById();
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onBlogClickSettingListener(BlogRO b) {

    }
}
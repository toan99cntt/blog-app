package com.example.blog;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.blog.adapter.OnBlogClickListener;
import com.example.blog.adapter.OnMemberClickListener;
import com.example.blog.adapter.OnNotificationClickListener;
import com.example.blog.api.ApiService;
import com.example.blog.fragment.FriendFragment;
import com.example.blog.fragment.HomeFragment;
import com.example.blog.fragment.MyselfFragment;
import com.example.blog.fragment.NotificationFragment;
import com.example.blog.model.UpdateStatusNotify;
import com.example.blog.model.BlogRO;
import com.example.blog.model.Like;
import com.example.blog.model.MemberRO;
import com.example.blog.model.Notification;
import com.example.blog.model.NotificationRO;
import com.example.blog.model.UserRO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListBlogActivity extends AppCompatActivity implements OnBlogClickListener, OnMemberClickListener, OnNotificationClickListener {
    SharedPreferences sharedPref;
    protected String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPref.getString("token", null);
        if(token == null) return;
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment seFragment = null;

                    switch (item.getItemId()) {
                        case R.id.icon_home:
                            seFragment = new HomeFragment();
                            break;
                        case R.id.icon_friend:
                            seFragment =  new FriendFragment();
                            break;
                        case R.id.icon_setting:
                            seFragment =  new MyselfFragment();
                            break;
                        case R.id.icon_logout:
                            showDialog(ListBlogActivity.this);
                            break;
                        case R.id.icon_notify:
                            seFragment =  new NotificationFragment();
                            break;
                        default:
                            break;
                    }
                    if(seFragment != null) getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, seFragment).commit();
                    return true;
                }
            };

    private void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_dialog_confirm);

        ImageButton btnClose = (ImageButton) dialog.findViewById(R.id.btnClose);
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);

        btnClose.setOnClickListener(v ->
        {
            dialog.dismiss();
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.edit().clear().apply();
                Intent intent = new Intent(ListBlogActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    @Override
    public void onBlogClickListener(BlogRO b) {
        Intent intent = new Intent(ListBlogActivity.this, InfoActivity.class);
        intent.putExtra("id", b.getMember().getId());
        startActivity(intent);
     }

    @Override
    public void onBlogClickCommentListener(BlogRO b) {
        Intent intent = new Intent(ListBlogActivity.this, BlogDetailActivity.class);
        intent.putExtra("id",b.getId());
        startActivity(intent);
    }

    @Override
    public void onBlogClickLikeListener(BlogRO b) {
        ApiService.apiService.setLikeOrUnlike(b.getId(), token).enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                Like res = response.body();
                if (res.getStatus() == 200) {
                    HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                    fragment.fetchData();
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                onBlogClickLikeListener(b);
            }
        });
    }

    @Override
    public void onBlogClickSettingListener(BlogRO b) {
        Intent intent = new Intent(ListBlogActivity.this, CreateBlogActivity.class);
        intent.putExtra("id_blog",b.getId());
        startActivity(intent);
    }

    @Override
    public void onMemberClickListener(MemberRO b) {
        Intent intent = new Intent(ListBlogActivity.this, InfoActivity.class);
        intent.putExtra("id", b.getId());
        startActivity(intent);
    }

    @Override
    public void onNotificationClickListener(NotificationRO b) {
        ApiService.apiService.setStatusNotification(b.getId(), token).enqueue(new Callback<UpdateStatusNotify>() {
            @Override
            public void onResponse(Call<UpdateStatusNotify> call, Response<UpdateStatusNotify> response) {
                if(response.body().getStatus() == 200) {
                    Intent intent = new Intent(ListBlogActivity.this, BlogDetailActivity.class);
                    intent.putExtra("id",b.getBlog_id());
                    startActivity(intent);
                } else {
                    Log.d(TAG, "onResponse: "+ response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<UpdateStatusNotify> call, Throwable t) {
                onNotificationClickListener(b);
            }
        });

    }
}
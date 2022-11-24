package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

import com.example.blog.api.ApiService;
import com.example.blog.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                sharedPref = PreferenceManager.getDefaultSharedPreferences(LoadingActivity.this);
                String token = sharedPref.getString("token", null);
                if(token == null) {
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    getInfo(token);
                }
            }
        }.start();
    }

    private void getInfo(String token) {
        ApiService.apiService.getInfoUser(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User res = response.body();
                if(response.body().getStatus() == 200) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("id",res.getData().getId());
                    editor.apply();
                    Intent intent = new Intent(LoadingActivity.this, ListBlogActivity.class);
                    startActivity(intent);
                } else if(res.getStatus() == 401) {
                    sharedPref.edit().clear().apply();
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                getInfo(token);
            }
        });
    }
}
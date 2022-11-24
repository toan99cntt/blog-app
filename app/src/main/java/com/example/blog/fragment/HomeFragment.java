package com.example.blog.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.CreateBlogActivity;
import com.example.blog.ListBlogActivity;
import com.example.blog.MainActivity;
import com.example.blog.R;
import com.example.blog.adapter.AdapterBlog;
import com.example.blog.api.ApiService;
import com.example.blog.model.Blog;
import com.example.blog.model.BlogRO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListBlogActivity mMainActivity;
    View view;
    private List<BlogRO> mLst;
    private AdapterBlog mAdapter;
    private SharedPreferences sharedPref;
    private FloatingActionButton btnAdd;
    private String token;

    public HomeFragment () { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        view.findViewById(R.id.recyclerView);
        mMainActivity = (ListBlogActivity) getActivity();
        recyclerView = view.findViewById(R.id.recyclerView);
        btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mMainActivity, CreateBlogActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mMainActivity);
        token = sharedPref.getString("token", null);
        mLst = new ArrayList<>();
        fetchData();
    }

    public void fetchData() {
        ApiService.apiService.getListBlogs(token).enqueue(new Callback<Blog>() {
            @Override
            public void onResponse(Call<Blog> call, Response<Blog> response) {
                Blog res = response.body();
                if(res.getStatus() == 200) {
                    mLst = res.getData();
                    setAdapter();
                } else if(res.getStatus() == 401) {
                    sharedPref.edit().clear().apply();
                    Intent intent = new Intent(mMainActivity, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Blog> call, Throwable t) {
                fetchData();
            }
        });
    }

    private void setAdapter() {
        mAdapter = new AdapterBlog(mLst, mMainActivity, "home");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mLst = new ArrayList<>();
    }
}

package com.example.blog.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.ListBlogActivity;
import com.example.blog.R;
import com.example.blog.adapter.AdapterMember;
import com.example.blog.adapter.AdapterNotification;
import com.example.blog.api.ApiService;
import com.example.blog.model.Member;
import com.example.blog.model.MemberRO;
import com.example.blog.model.Notification;
import com.example.blog.model.NotificationRO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private ListBlogActivity mMainActivity;
    View view;
    private List<NotificationRO> mLst;
    private AdapterNotification mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        mMainActivity = (ListBlogActivity) getActivity();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mMainActivity);
        String token = sharedPref.getString("token", null);
        mLst = new ArrayList<>();
        fetchData(token);
    }

    private void setDataAdapter(List<NotificationRO> data) {
        mAdapter = new AdapterNotification(data, mMainActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void fetchData(String token) {
        ApiService.apiService.getNotifications(token).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                recyclerView = view.findViewById(R.id.recyclerView);
                Notification res = response.body();
                mLst = res.getData();
                setDataAdapter(mLst);
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                fetchData(token);
            }
        });
    }
}

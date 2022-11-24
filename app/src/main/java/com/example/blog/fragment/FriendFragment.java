package com.example.blog.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.EditMemberActivity;
import com.example.blog.InfoActivity;
import com.example.blog.ListBlogActivity;
import com.example.blog.R;
import com.example.blog.adapter.AdapterMember;
import com.example.blog.api.ApiService;
import com.example.blog.model.BlogRO;
import com.example.blog.model.Member;
import com.example.blog.model.MemberRO;
import com.example.blog.model.User;
import com.example.blog.model.UserRO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {
    private RecyclerView recyclerView;
    private ListBlogActivity mMainActivity;
    View view;
    private List<MemberRO> mLst;
    private AdapterMember mAdapter;
    private EditText edtSearch;
    private TextView btnSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        mMainActivity = (ListBlogActivity) getActivity();
        btnSearch = view.findViewById(R.id.btnSearch);
        edtSearch = view.findViewById(R.id.edtSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MemberRO> list = new ArrayList<>();
                for (MemberRO ls: mLst) {
                    if (ls.getName().contains(edtSearch.getText().toString()) || ls.getEmail().contains(edtSearch.getText().toString())) {
                        list.add(ls);
                    }
                }
                setDataAdapter(list);
            }
        });

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

    private void setDataAdapter(List<MemberRO> data) {
        mAdapter = new AdapterMember(data, mMainActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void fetchData(String token) {
        ApiService.apiService.getListMember(token).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                recyclerView = view.findViewById(R.id.recyclerView);
                Member res = response.body();
                mLst = res.getData();
                setDataAdapter(mLst);
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                fetchData(token);
            }
        });
    }
}

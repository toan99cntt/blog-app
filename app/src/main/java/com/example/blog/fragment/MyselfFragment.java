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
import com.example.blog.ListMemberChatActivity;
import com.example.blog.R;
import com.example.blog.adapter.AdapterBlog;
import com.example.blog.api.ApiService;
import com.example.blog.model.Blog;
import com.example.blog.model.BlogRO;
import com.example.blog.model.User;
import com.example.blog.model.UserRO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyselfFragment extends Fragment {
    private RecyclerView recyclerView;
    private ListBlogActivity mMainActivity;
    View view;
    private List<BlogRO> mLst;
    private AdapterBlog mAdapter;
    private CircleImageView imgAvatar;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtPhone;
    private TextView txtDob;
    private TextView txtGender;
    private TextView txtEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myself, container, false);
        mLst = new ArrayList<>();
        mMainActivity = (ListBlogActivity) getActivity();
        imgAvatar = (CircleImageView) view.findViewById(R.id.imgAvatar);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        txtDob = (TextView) view.findViewById(R.id.txtDob);
        txtGender = (TextView) view.findViewById(R.id.txtGender);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        txtEdit = view.findViewById(R.id.txtEdit);

        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mMainActivity, ListMemberChatActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mMainActivity);
        String token = sharedPref.getString("token", null);
        fetchData(token);
    }

    public void fetchData(String token) {
        ApiService.apiService.getInfoUser(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User res = response.body();
                if(res.getStatus() == 200) {
                    UserRO user = res.getData();
                    if(user.getAvatar() != null && user.getAvatar().length> 0) {
                        String url = user.getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
                        Picasso.get().load(url).into(imgAvatar);
                    }
                    txtName.setText(user.getName());
                    txtEmail.setText("Email address: " + user.getEmail());
                    txtGender.setText("Gender: " + (user.getGender() == 1 ? "Male" : "Female"));
                    txtDob.setText("Birthday: " + user.getDob());
                    txtPhone.setText("Phone: " + user.getPhone());
                    mLst = user.getBlogs();
                    mAdapter = new AdapterBlog(mLst, mMainActivity, "myself");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                    txtEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mMainActivity, EditMemberActivity.class);
                            intent.putExtra("id", user.getId());
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(mMainActivity, "Error!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                fetchData(token);
            }
        });
    }
}
